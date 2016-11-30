package org.ilapin.arvelocity.graphics;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import org.ilapin.arvelocity.graphics.squaresurface.SquareSurface;
import org.ilapin.common.geometry.Point;

import static android.opengl.Matrix.*;

public class MainScene implements Scene {

	private final SquareSurface mSquareSurface;

	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	private final float[] mViewProjectionMatrix = new float[16];

	private Point mPosition = new Point();

	public MainScene(final Context context) {
		mSquareSurface = new SquareSurface(context, this, 1, 10);
		mSquareSurface.setRotation(1, 0, 0, -90);
		mSquareSurface.setScale(new Point(2, 2, 2));
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

	private long mLastTimestamp = -1;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public void render() {
		final long currentTimestamp = SystemClock.elapsedRealtimeNanos();

		setLookAtM(mViewMatrix, 0, 0, 10, 10, 0, 0, 0, 0, 1, 0);
		multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		if (mLastTimestamp > 0) {
			final long dt = currentTimestamp - mLastTimestamp;
			float y = mPosition.getY() - 1f / 1000000000 * dt;
			if (y < -5) {
				y = 0;
			}
			mPosition = new Point(mPosition.getX(), y, mPosition.getZ());
		}
		mLastTimestamp = currentTimestamp;
		mSquareSurface.setPosition(mPosition);

		mSquareSurface.render();
	}
}
