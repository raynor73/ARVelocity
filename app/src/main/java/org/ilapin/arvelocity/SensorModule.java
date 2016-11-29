package org.ilapin.arvelocity;

import android.hardware.SensorManager;
import dagger.Module;
import dagger.Provides;
import org.ilapin.common.sensor.AverageCompassSensor;
import org.ilapin.common.sensor.RawCompassSensor;

import javax.inject.Singleton;

@Module
public class SensorModule {

	@Provides
	@Singleton
	public AverageCompassSensor provideAverageCompassSensor(final SensorManager sensorManager) {
		return new AverageCompassSensor(sensorManager);
	}

	@Provides
	@Singleton
	public RawCompassSensor provideRawCompassSensor(final SensorManager sensorManager) {
		return new RawCompassSensor(sensorManager);
	}
}
