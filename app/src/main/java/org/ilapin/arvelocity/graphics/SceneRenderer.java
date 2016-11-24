package org.ilapin.arvelocity.graphics;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glViewport;

public class SceneRenderer implements GLSurfaceView.Renderer {

	private final Scene mScene;
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];

	public SceneRenderer(final Scene scene) {
		mScene = scene;
	}

	@Override
	public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
		// do nothing
	}

	@Override
	public void onSurfaceChanged(final GL10 gl10, final int width, final int height) {
		glViewport(0, 0, width, height);
		mScene.onOpenGlReady(width, height);
	}

	@Override
	public void onDrawFrame(final GL10 gl10) {
		mScene.render();
	}
}
