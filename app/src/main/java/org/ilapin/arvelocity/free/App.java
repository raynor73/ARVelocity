package org.ilapin.arvelocity.free;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;
import org.ilapin.arvelocity.*;

import static org.ilapin.common.Constants.CM_PER_INCH;

public class App extends Application {

	private static final String TAG = "App";

	private static final float PREFERRED_UNIT_SIZE_CM = 10f;

	private static ApplicationComponent sApplicationComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		final int smallestWidthPixels = displayMetrics.widthPixels < displayMetrics.heightPixels ?
				displayMetrics.widthPixels : displayMetrics.heightPixels;
		final float xdpi = displayMetrics.xdpi;
		final float smallestWidthCm = (float) smallestWidthPixels / xdpi * CM_PER_INCH;
		final float physicalSizeFactor = PREFERRED_UNIT_SIZE_CM / smallestWidthCm;
		Log.d(TAG, "Physical Size Factor: " + physicalSizeFactor);

		sApplicationComponent = DaggerApplicationComponent.builder()
				.systemModule(new SystemModule(this, physicalSizeFactor))
				.build();
	}

	public static ApplicationComponent getApplicationComponent() {
		return sApplicationComponent;
	}
}
