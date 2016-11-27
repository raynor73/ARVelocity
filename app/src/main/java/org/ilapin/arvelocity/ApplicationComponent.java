package org.ilapin.arvelocity;

import org.ilapin.arvelocity.free.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { SystemModule.class, GraphicsModule.class})
public interface ApplicationComponent {

	void inject(MainActivity activity);
}
