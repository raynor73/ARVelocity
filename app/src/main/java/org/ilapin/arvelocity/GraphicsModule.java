package org.ilapin.arvelocity;

import android.content.Context;

import org.ilapin.arvelocity.graphics.MainScene;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GraphicsModule {

	@Provides
	@Singleton
	public MainScene provideMainScene(final Context context) {
		return new MainScene(context);
	}
}
