package org.ilapin.arvelocity.graphics;

import android.opengl.GLSurfaceView;
import android.util.Log;
import org.ilapin.arvelocity.graphics.camerapreview.CameraPreview;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

public class MainRenderer implements GLSurfaceView.Renderer {

	private final CameraPreview mCameraPreview;
	private final MainScene mMainScene;

	public MainRenderer(final CameraPreview cameraPreview, final MainScene mainScene) {
		mCameraPreview = cameraPreview;
		mMainScene = mainScene;
	}

	@Override
	public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
		// do nothing
	}

	@Override
	public void onSurfaceChanged(final GL10 gl10, final int width, final int height) {
		glClearColor(0, 0, 0, 0);
		glViewport(0, 0, width, height);
		Log.d("!@#", "Viewport width: " + width + "; height: " + height);
		mCameraPreview.setWidth(width);
		mCameraPreview.setHeight(height);
		mCameraPreview.onOpenGlReady();
		mMainScene.onOpenGlReady(width, height);
	}

	@Override
	public void onDrawFrame(final GL10 gl10) {
		glClear(GL_COLOR_BUFFER_BIT);
		//mCameraPreview.render();
		mMainScene.render();
	}
}
