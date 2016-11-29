package org.ilapin.arvelocity.graphics.squaresurface;

import android.content.Context;
import org.ilapin.arvelocity.free.R;
import org.ilapin.common.android.opengl.ShaderProgram;

public class SquareSurfaceShaderProgram extends ShaderProgram {

	private static final String A_POSITION = "a_Position";
	private static final String U_MVP_MATRIX = "u_MvpMatrix";
	private static final String U_COLOR = "u_Color";


	public SquareSurfaceShaderProgram(final Context context) {
		super(context, R.raw.square_surface_vertex_shader, R.raw.square_surface_fragment_shader);
	}


}
