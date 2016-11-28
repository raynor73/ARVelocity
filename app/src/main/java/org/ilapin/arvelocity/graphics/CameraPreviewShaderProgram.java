package org.ilapin.arvelocity.graphics;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import org.ilapin.arvelocity.free.R;
import org.ilapin.common.android.opengl.ShaderProgram;

import static android.opengl.GLES20.*;

public class CameraPreviewShaderProgram extends ShaderProgram {

	private static final String A_POSITION = "a_Position";
	private static final String A_TEXTURE_COORDINATE = "a_TextureCoordinate";
	private static final String U_TEXTURE_UNIT = "u_TextureUnit";
	private static final String U_TEXTURE_COORDINATE_MATRIX = "u_TextureCoordinateMatrix";

	private final int mPositionAttributeLocation;
	private final int mTextureCoordinateAttributeLocation;
	private final int mTextureUnitUniformLocation;
	private final int mTextureCoordinateMatrixUniformLocation;

	protected CameraPreviewShaderProgram(final Context context) {
		super(context, R.raw.camera_preview_vertex_shader, R.raw.camera_preview_fragment_shader);

		mPositionAttributeLocation = glGetAttribLocation(mProgram, A_POSITION);
		mTextureCoordinateAttributeLocation = glGetAttribLocation(mProgram, A_TEXTURE_COORDINATE);
		mTextureUnitUniformLocation = glGetUniformLocation(mProgram, U_TEXTURE_UNIT);
		mTextureCoordinateMatrixUniformLocation = glGetUniformLocation(mProgram, U_TEXTURE_COORDINATE_MATRIX);
	}

	public void setUniforms(final int textureUnitLocation, final float[] textureCoordinateMatrix) {
		glUniformMatrix4fv(mTextureCoordinateMatrixUniformLocation, 1, false, textureCoordinateMatrix, 0);

		glActiveTexture(GLES20.GL_TEXTURE0);
		glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureUnitLocation);
		glUniform1i(mTextureUnitUniformLocation, 0);
	}

	public int getPositionAttributeLocation() {
		return mPositionAttributeLocation;
	}

	public int getTextureCoordinateAttributeLocation() {
		return mTextureCoordinateAttributeLocation;
	}
}
