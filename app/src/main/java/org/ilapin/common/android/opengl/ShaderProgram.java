package org.ilapin.common.android.opengl;

import android.content.Context;

import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {

	protected final int mProgram;

	public ShaderProgram(final Context context, final int vertexShaderResourceId,
							final int fragmentShaderResourceId) {
		mProgram = ShaderHelper.buildProgram(
				TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
				TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId)
		);
	}

	public void useProgram() {
		glUseProgram(mProgram);
	}
}
