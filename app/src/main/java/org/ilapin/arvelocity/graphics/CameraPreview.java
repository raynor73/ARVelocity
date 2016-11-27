package org.ilapin.arvelocity.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import org.ilapin.arvelocity.free.R;
import org.ilapin.arvelocity.sensor.Sensor;
import org.ilapin.common.android.CameraUtils;
import org.ilapin.common.android.opengl.IndexBuffer;
import org.ilapin.common.android.opengl.VertexBuffer;

import java.io.IOException;
import java.nio.IntBuffer;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
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
	private final float[] mProjectionMatrix = new float[16];

	private final Context mContext;

	private boolean mIsTextureCoordinatesRecalculationRequired;
	private Activity mActivity;
	private Camera mCamera;
	private SurfaceTexture mSurfaceTexture;
	private VertexBuffer mVertexBuffer;
	private IndexBuffer mIndexBuffer;
	private CameraPreviewShaderProgram mShaderProgram;
	private int mTextureId;

	public CameraPreview(final Context context) {
		mContext = context;
	}

	@Override
	public void onOpenGlReady() {
		recalculateTextureCoordinates();

		mVertexBuffer = new VertexBuffer(mVertices);
		mIndexBuffer = new IndexBuffer(mIndices);
		mShaderProgram = new CameraPreviewShaderProgram(mContext);

		final IntBuffer intBuffer = IntBuffer.allocate(1);
		glGenTextures(1, intBuffer);
		glBindTexture(GL_TEXTURE_EXTERNAL_OES, intBuffer.get(0));
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glBindTexture(GL_TEXTURE_EXTERNAL_OES, 0);
		mTextureId = intBuffer.get(0);

		mSurfaceTexture = new SurfaceTexture(mTextureId);

		Matrix.setIdentityM(mProjectionMatrix, 0);
	}

	@Override
	public void render() {
		/*if (mIsTextureCoordinatesRecalculationRequired) {
			mIsTextureCoordinatesRecalculationRequired = false;

		}*/

		/*final org.ilapin.arvelocity.graphics.Camera camera = scene.getActiveCamera();
		final float width = camera.getWidth();
		final float height = camera.getHeight();
		final float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
		if (width > height) {
			// Landscape
			orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
		} else {
			// Portrait or square
			orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
		}*/

		mShaderProgram.useProgram();
		mShaderProgram.setUniforms(mProjectionMatrix, mTextureId);
		mVertexBuffer.setVertexAttribPointer(0, mShaderProgram.getPositionAttributeLocation(),
				NUMBER_OF_VERTEX_COMPONENTS, STRIDE);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBuffer.getBufferId());
		glDrawElements(GL_TRIANGLES, mIndices.length / 3, GL_UNSIGNED_SHORT, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public void start() {
		mIsTextureCoordinatesRecalculationRequired = true;

		mCamera = android.hardware.Camera.open();
		if (mCamera == null) {
			Toast.makeText(mActivity, R.string.error_can_not_open_camera, Toast.LENGTH_SHORT).show();
			return;
		}

		final Camera.Parameters cameraParameters = mCamera.getParameters();
		final Camera.Size previewSize = CameraUtils.calculateLargestPreviewSize(mCamera);
		Log.d("CameraPreview", String.format("Preview width: %d; height: %d", previewSize.width, previewSize.height));
		cameraParameters.setPreviewSize(previewSize.width, previewSize.height);
		mCamera.setParameters(cameraParameters);

		try {
			mCamera.setPreviewTexture(mSurfaceTexture);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		mCamera.startPreview();
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
