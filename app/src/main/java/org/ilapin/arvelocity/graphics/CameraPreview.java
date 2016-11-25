package org.ilapin.arvelocity.graphics;

import android.app.Activity;

import org.ilapin.arvelocity.sensor.Sensor;

public class CameraPreview implements Renderable, Sensor {

	private static final int NUMBER_OF_VERTICES = 4;
	private static final int NUMBER_OF_VERTEX_COMPONENTS = 3;
	private static final int NUMBER_OF_TEXTURE_COMPONENTS = 2;

	private final float[] mVertices =
			new float[NUMBER_OF_VERTICES * (NUMBER_OF_VERTEX_COMPONENTS + NUMBER_OF_TEXTURE_COMPONENTS)];
	private final int[] mIndices = new int[] {
			0, 1, 2,
			2, 3, 0
	};

	private boolean mIsTextureCoordinatesRecalculationRequired;
	private Activity mActivity;

	@Override
	public void onOpenGlReady() {

	}

	@Override
	public void render(final Scene scene) {

	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	public void setActivity(final Activity activity) {
		mActivity = activity;
	}
}
