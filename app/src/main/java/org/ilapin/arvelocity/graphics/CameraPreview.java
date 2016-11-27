package org.ilapin.arvelocity.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.Surface;
import android.widget.Toast;

import org.ilapin.arvelocity.free.R;
import org.ilapin.arvelocity.sensor.Sensor;
import org.ilapin.common.android.CameraUtils;
import org.ilapin.common.android.opengl.IndexBuffer;
import org.ilapin.common.android.opengl.VertexBuffer;

import java.io.IOException;
import java.nio.IntBuffer;

import static android.opengl.GLES10.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;
import static org.ilapin.common.Constants.BYTES_PER_FLOAT;

@SuppressWarnings("deprecation")
public class CameraPreview implements Renderable, Sensor {

	private static final int NUMBER_OF_VERTEX_COMPONENTS = 2;
	private static final int NUMBER_OF_TEXTURE_COMPONENTS = 2;
	private static final int STRIDE = (NUMBER_OF_VERTEX_COMPONENTS + NUMBER_OF_TEXTURE_COMPONENTS) * BYTES_PER_FLOAT;

	private final float[] mVertices = new float[] {
			-1, 1, 0, 0,
			-1, -1, 0, 0,
			1, -1, 0, 0,
			1, 1, 0, 0
	};
	private final short[] mIndices = new short[] {
			0, 1, 2,
			2, 3, 0
	};

	private final Context mContext;

	private Activity mActivity;
	private VertexBuffer mVertexBuffer;
	private IndexBuffer mIndexBuffer;
	private CameraPreviewShaderProgram mShaderProgram;
	private Camera mCamera;
	private int mTextureUnitLocation;
	private SurfaceTexture mSurfaceTexture;

	public CameraPreview(final Context context) {
		mContext = context;
	}

	@Override
	public void onOpenGlReady() {
		recalculateTextureCoordinates();

		mVertexBuffer = new VertexBuffer(mVertices);
		mIndexBuffer = new IndexBuffer(mIndices);
		mShaderProgram = new CameraPreviewShaderProgram(mContext);
		mTextureUnitLocation = initTextureUnit();
	}

	@Override
	public void render() {
		mSurfaceTexture.updateTexImage();

		mShaderProgram.useProgram();

		mVertexBuffer.setVertexAttribPointer(
				0,
				mShaderProgram.getPositionAttributeLocation(),
				NUMBER_OF_VERTEX_COMPONENTS,
				STRIDE
		);
		mVertexBuffer.setVertexAttribPointer(
				NUMBER_OF_VERTEX_COMPONENTS,
				mShaderProgram.getTextureCoordinateAttributeLocation(),
				NUMBER_OF_TEXTURE_COMPONENTS,
				STRIDE
		);

		mShaderProgram.setUniforms(mTextureUnitLocation);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBuffer.getBufferId());
		glDrawElements(GL_TRIANGLES, mIndices.length, GL_UNSIGNED_SHORT, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public void start() {
		mCamera = Camera.open();

		if (mCamera != null) {
			final Camera.Parameters cameraParameters = mCamera.getParameters();
			final Camera.Size size = CameraUtils.calculateLargestPreviewSize(mCamera);
			cameraParameters.setPreviewSize(size.width, size.height);
			mCamera.setParameters(cameraParameters);

			mSurfaceTexture = new SurfaceTexture(mTextureUnitLocation);

			try {
				mCamera.setPreviewTexture(mSurfaceTexture);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}

			mCamera.startPreview();
		} else {
			Toast.makeText(mActivity, R.string.error_can_not_open_camera, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void stop() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	public void setActivity(final Activity activity) {
		mActivity = activity;
	}

	private int initTextureUnit() {
		final IntBuffer intBuffer = IntBuffer.allocate(1);
		glGenTextures(1, intBuffer);
		glBindTexture(GL_TEXTURE_EXTERNAL_OES, intBuffer.get(0));
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		return intBuffer.get(0);
	}

	private void recalculateTextureCoordinates() {
		switch (mActivity.getWindowManager().getDefaultDisplay().getRotation()) {
			case Surface.ROTATION_0:
				// top left
				mVertices[2] = 0.0f;
				mVertices[3] = 1.0f;
				// bottom left
				mVertices[6] = 1.0f;
				mVertices[7] = 1.0f;
				// bottom right
				mVertices[10] = 1.0f;
				mVertices[11] = 0.0f;
				// top right
				mVertices[14] = 0.0f;
				mVertices[15] = 0.0f;
				break;

			case Surface.ROTATION_90:
				// top left
				mVertices[2] = 0.0f;
				mVertices[3] = 0.0f;
				// bottom left
				mVertices[6] = 0.0f;
				mVertices[7] = 1.0f;
				// bottom right
				mVertices[10] = 1.0f;
				mVertices[11] = 1.0f;
				// top right
				mVertices[14] = 1.0f;
				mVertices[15] = 0.0f;
				break;

			case Surface.ROTATION_180:
				// top left
				mVertices[2] = 1.0f;
				mVertices[3] = 0.0f;
				// bottom left
				mVertices[6] = 0.0f;
				mVertices[7] = 0.0f;
				// bottom right
				mVertices[10] = 0.0f;
				mVertices[11] = 1.0f;
				// top right
				mVertices[14] = 1.0f;
				mVertices[15] = 1.0f;
				break;

			case Surface.ROTATION_270:
				// top left
				mVertices[2] = 1.0f;
				mVertices[3] = 1.0f;
				// bottom left
				mVertices[6] = 1.0f;
				mVertices[7] = 0.0f;
				// bottom right
				mVertices[10] = 0.0f;
				mVertices[11] = 0.0f;
				// top right
				mVertices[14] = 0.0f;
				mVertices[15] = 1.0f;
				break;

			default:
				Toast.makeText(mActivity, "Unknown orientation", Toast.LENGTH_SHORT).show();
				// top left
				mVertices[2] = 0.0f;
				mVertices[3] = 1.0f;
				// bottom left
				mVertices[6] = 1.0f;
				mVertices[7] = 1.0f;
				// bottom right
				mVertices[10] = 1.0f;
				mVertices[11] = 0.0f;
				// top right
				mVertices[14] = 0.0f;
				mVertices[15] = 0.0f;
		}
	}
}
