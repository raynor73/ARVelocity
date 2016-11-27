package org.ilapin.arvelocity.graphics;

import android.content.Context;

import org.ilapin.common.geometry.Point;

import static android.opengl.GLES20.glClearColor;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.setLookAtM;

public class MainScene implements Scene {

	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];

	private Camera mCamera;
//	private final CameraPreview mCameraPreview;

	public MainScene(final Context context) {
		//mCameraPreview = new CameraPreview(context);
	}

	/*public void setActivity(final Activity activity) {
		mCameraPreview.setActivity(activity);
	}*/

	@Override
	public Camera getActiveCamera() {
		return mCamera;
	}

	@Override
	public void onOpenGlReady(final int viewPortWidth, final int viewPortHeight) {
		mCamera = new Camera(viewPortWidth, viewPortHeight);
		mCamera.setLookAt(new Point(0, 0, -1));

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		//glViewport(0, 0, viewPortWidth, viewPortHeight);
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

		//mCameraPreview.onOpenGlReady();
	}

	@Override
	public void render() {
		if (mCamera != null) {
		//	mCameraPreview.render();
		}
	}

	/*public CameraPreview getCameraPreview() {
		return mCameraPreview;
	}*/
}
