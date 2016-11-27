package org.ilapin.arvelocity.graphics;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SceneRenderer implements GLSurfaceView.Renderer {

//	private final Scene mScene;
	private final CameraPreview mCameraPreview;
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];

	public SceneRenderer(final Scene scene, final CameraPreview cameraPreview) {
//		mScene = scene;
		mCameraPreview = cameraPreview;
	}

	@Override
	public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
		// do nothing
	}

	@Override
	public void onSurfaceChanged(final GL10 gl10, final int width, final int height) {
		//glViewport(0, 0, width, height);
		mCameraPreview.onOpenGlReady();
//		mScene.onOpenGlReady(width, height);
	}

	@Override
	public void onDrawFrame(final GL10 gl10) {
		mCameraPreview.render();
//		mScene.render();
	}
}
