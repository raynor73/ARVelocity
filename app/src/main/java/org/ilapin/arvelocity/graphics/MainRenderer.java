package org.ilapin.arvelocity.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import org.ilapin.arvelocity.free.R;
import org.ilapin.common.android.opengl.IndexBuffer;
import org.ilapin.common.android.opengl.ShaderProgram;
import org.ilapin.common.android.opengl.VertexBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glViewport;
import static org.ilapin.arvelocity.graphics.CameraPreviewShaderProgram.A_POSITION;

public class MainRenderer implements GLSurfaceView.Renderer {

	//private final CameraPreview mCameraPreview;

	private final static int NUMBER_OF_VERTEX_COMPONENTS = 2;

	private VertexBuffer mVertexBuffer;
	private IndexBuffer mIndexBuffer;
	private final float[] mVertices = new float[] {
			-1, 1,
			-1, -1,
			1, -1,
			1, 1
	};
	private final short[] mIndices = new short[] {
			0, 1, 2,
			2, 3, 0
	};

	private final Context mContext;

	private ShaderProgram mShaderProgram;
	private int mPositionAttributeLocation;
	private int mMatrixUniformLocation;
	private final float[] mMatrix = new float[16];

	public MainRenderer(final CameraPreview cameraPreview, final Context context) {
		//mCameraPreview = cameraPreview;
		mContext = context;

		Matrix.setIdentityM(mMatrix, 0);
		//orthoM(mMatrix, 0, -1, 1, -1, 1, -1, 1);
	}

	@Override
	public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
		// do nothing
	}

	@Override
	public void onSurfaceChanged(final GL10 gl10, final int width, final int height) {
		glViewport(0, 0, width, height);
		//mCameraPreview.onOpenGlReady();

		mShaderProgram = new ShaderProgram(
				mContext,
				R.raw.camera_preview_vertex_shader,
				R.raw.camera_preview_fragment_shader
		);

		mVertexBuffer = new VertexBuffer(mVertices);
		mIndexBuffer = new IndexBuffer(mIndices);

		final int program = mShaderProgram.getProgram();
		mPositionAttributeLocation = glGetAttribLocation(program, A_POSITION);
		//mMatrixUniformLocation = glGetUniformLocation(program, U_MATRIX);
	}

	@Override
	public void onDrawFrame(final GL10 gl10) {
		glClearColor(0, 1, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT);

		mShaderProgram.useProgram();

		mVertexBuffer.setVertexAttribPointer(
				0,
				mPositionAttributeLocation,
				NUMBER_OF_VERTEX_COMPONENTS,
				0
		);
		//glUniformMatrix4fv(mMatrixUniformLocation, 1, false, mMatrix, 0);

		//glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBuffer.getBufferId());
		glDrawElements(GL_TRIANGLES, mIndices.length, GL_UNSIGNED_SHORT, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		//mCameraPreview.render();
	}
}
