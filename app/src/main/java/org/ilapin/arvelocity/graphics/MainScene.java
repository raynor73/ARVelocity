package org.ilapin.arvelocity.graphics;

import android.content.Context;
import org.ilapin.arvelocity.graphics.squaresurface.SquareSurface;

import static android.opengl.Matrix.*;

public class MainScene implements Scene {

	private final SquareSurface mSquareSurface;

	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	private final float[] mViewProjectionMatrix = new float[16];

	public MainScene(final Context context) {
		mSquareSurface = new SquareSurface(context, this, 10, 10);
	}

	@Override
	public float[] getViewProjectionMatrix() {
		return mViewProjectionMatrix;
	}

	@Override
	public void onOpenGlReady(final int viewPortWidth, final int viewPortHeight) {
		perspectiveM(mProjectionMatrix, 0, 45, (float) viewPortWidth / viewPortHeight, 1, 10000);

		mSquareSurface.onOpenGlReady();
	}

	@Override
	public void render() {
		setLookAtM(mViewMatrix, 0, 0, 10, 10, 0, 0, 0, 0, 1, 0);
		multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		mSquareSurface.render();
	}
}
