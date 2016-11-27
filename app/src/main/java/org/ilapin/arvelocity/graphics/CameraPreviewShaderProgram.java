package org.ilapin.arvelocity.graphics;

import android.content.Context;

import org.ilapin.arvelocity.free.R;
import org.ilapin.common.android.opengl.ShaderProgram;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class CameraPreviewShaderProgram extends ShaderProgram {

	private final int mMatrixUniformLocation;
	private final int mTextureUnitUniformLocation;
	private final int mPositionAttributeLocation;

	protected CameraPreviewShaderProgram(final Context context) {
		super(context, R.raw.camera_preview_vertex_shader, R.raw.camera_preview_fragment_shader);

		mMatrixUniformLocation = glGetUniformLocation(mProgram, U_MATRIX);
		mTextureUnitUniformLocation = glGetUniformLocation(mProgram, U_TEXTURE_UNIT);
		mPositionAttributeLocation = glGetAttribLocation(mProgram, A_POSITION);
	}

	public void setUniforms(final float[] matrix, final int textureId) {
		glUniformMatrix4fv(mMatrixUniformLocation, 1, false, matrix, 0);

		/*glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureId);
		glUniform1i(mTextureUnitUniformLocation, 0);*/
	}

	public int getPositionAttributeLocation() {
		return mPositionAttributeLocation;
	}
}
