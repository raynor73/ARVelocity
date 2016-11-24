package org.ilapin.arvelocity.graphics;

import org.ilapin.common.geometry.Point;

import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.setLookAtM;

public class MainScene implements Scene {

	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];

	private Camera mCamera;

	@Override
	public void onOpenGlReady(final int viewPortWidth, final int viewPortHeight) {
		mCamera = new Camera(viewPortWidth, viewPortHeight);
		mCamera.setLookAt(new Point(0, 0, -1));

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		glViewport(0, 0, viewPortWidth, viewPortHeight);
		perspectiveM(mProjectionMatrix, 0, 45, (float) viewPortWidth / (float) viewPortHeight, 1f, 1000f);
		final Point cameraPosition = mCamera.getPosition();
		final Point cameraLookAt = mCamera.getLookAt();
		setLookAtM(
				mViewMatrix, 0,
				cameraPosition.getX(),
				cameraPosition.getY(),
				cameraPosition.getZ(),
				cameraLookAt.getX(),
				cameraLookAt.getY(),
				cameraLookAt.getZ(),
				0f, 1f, 0f
		);
	}

	@Override
	public void render() {
		if (mCamera != null) {
			// render
		}
	}
}
