package org.ilapin.arvelocity.graphics;

import android.content.Context;

import org.ilapin.arvelocity.free.R;
import org.ilapin.common.android.opengl.ShaderProgram;

import static android.opengl.GLES20.glGetAttribLocation;

public class CameraPreviewShaderProgram extends ShaderProgram {

	protected static final String A_POSITION = "a_Position";

	private final int mPositionAttributeLocation;

	protected CameraPreviewShaderProgram(final Context context) {
		super(context, R.raw.camera_preview_vertex_shader, R.raw.camera_preview_fragment_shader);

		mPositionAttributeLocation = glGetAttribLocation(mProgram, A_POSITION);
	}

	public int getPositionAttributeLocation() {
		return mPositionAttributeLocation;
	}
}
