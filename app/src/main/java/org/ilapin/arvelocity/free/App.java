package org.ilapin.arvelocity.free;

import android.app.Application;

import org.ilapin.arvelocity.ApplicationComponent;
import org.ilapin.arvelocity.DaggerApplicationComponent;
import org.ilapin.arvelocity.SystemModule;

public class App extends Application {

	private static ApplicationComponent sApplicationComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		sApplicationComponent = DaggerApplicationComponent.builder()
				.systemModule(new SystemModule(this))
				.build();
	}

	public static ApplicationComponent getApplicationComponent() {
		return sApplicationComponent;
	}
}
