package org.ilapin.arvelocity.graphics;

import android.app.Activity;
import android.content.Context;
import android.view.Surface;
import android.widget.Toast;

import org.ilapin.arvelocity.sensor.Sensor;
import org.ilapin.common.android.opengl.IndexBuffer;
import org.ilapin.common.android.opengl.VertexBuffer;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;
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

	public CameraPreview(final Context context) {
		mContext = context;
	}

	@Override
	public void onOpenGlReady() {
		recalculateTextureCoordinates();

		mVertexBuffer = new VertexBuffer(mVertices);
		mIndexBuffer = new IndexBuffer(mIndices);
		mShaderProgram = new CameraPreviewShaderProgram(mContext);
	}

	@Override
	public void render() {
		mShaderProgram.useProgram();

		mVertexBuffer.setVertexAttribPointer(
				0,
				mShaderProgram.getPositionAttributeLocation(),
				NUMBER_OF_VERTEX_COMPONENTS,
				STRIDE
		);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBuffer.getBufferId());
		glDrawElements(GL_TRIANGLES, mIndices.length, GL_UNSIGNED_SHORT, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public void start() {}

	@Override
	public void stop() {}

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
