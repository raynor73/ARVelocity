package org.ilapin.arvelocity.graphics.squaresurface;

import android.content.Context;
import org.ilapin.arvelocity.free.R;
import org.ilapin.common.android.opengl.ShaderProgram;

import static android.opengl.GLES20.*;

public class SquareSurfaceShaderProgram extends ShaderProgram {

	private static final String A_POSITION = "a_Position";
	private static final String U_MVP_MATRIX = "u_MvpMatrix";
	private static final String U_COLOR = "u_Color";

	private final int mPositionAttributeLocation;
	private final int mMvpMatrixUniformLocation;
	private final int mColorUniformLocation;

	public SquareSurfaceShaderProgram(final Context context) {
		super(context, R.raw.square_surface_vertex_shader, R.raw.square_surface_fragment_shader);

		mPositionAttributeLocation = glGetAttribLocation(mProgram, A_POSITION);
		mMvpMatrixUniformLocation = glGetUniformLocation(mProgram, U_MVP_MATRIX);
		mColorUniformLocation = glGetUniformLocation(mProgram, U_COLOR);
	}

	public void setUniforms(final float[] mvpMatrix, final float r, final float g, final float b, final float a) {
		glUniformMatrix4fv(mMvpMatrixUniformLocation, 1, false, mvpMatrix, 0);
		glUniform4f(mColorUniformLocation, r, g, b, a);
	}

	public int getPositionAttributeLocation() {
		return mPositionAttributeLocation;
	}
}
