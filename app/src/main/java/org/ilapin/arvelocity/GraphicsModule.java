package org.ilapin.arvelocity;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import org.ilapin.arvelocity.graphics.MainRenderer;
import org.ilapin.arvelocity.graphics.MainScene;
import org.ilapin.arvelocity.graphics.camerapreview.CameraPreview;

import javax.inject.Singleton;

@Module
public class GraphicsModule {

	@Provides
	@Singleton
	public MainScene provideMainScene(final Context context) {
		return new MainScene(context);
	}

	@Provides
	public MainRenderer provideMainRenderer(final CameraPreview cameraPreview, final MainScene mainScene) {
		return new MainRenderer(cameraPreview, mainScene);
	}

	@Provides
	@Singleton
	public CameraPreview provideCameraPreview(final Context context) {
		return new CameraPreview(context);
	}
}
