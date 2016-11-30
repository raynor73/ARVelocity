package org.ilapin.arvelocity;

import android.content.Context;
import android.hardware.SensorManager;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class SystemModule {

	private final Context mContext;
	private final float mPhysicalSizeFactor;

	public SystemModule(final Context context, final float physicalSizeFactor) {
		mContext = context;
		mPhysicalSizeFactor = physicalSizeFactor;
	}

	@Provides
	@Singleton
	@PhysicalSizeFactor
	public float providePhysicalSizeFactor() {
		return mPhysicalSizeFactor;
	}

	@Provides
	@Singleton
	public Context provideContext() {
		return mContext;
	}

	@Provides
	@Singleton
	public SensorManager provideSensorManager(final Context context) {
		return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}
}
