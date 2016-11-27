package org.ilapin.arvelocity.graphics;

public interface Scene {

	Camera getActiveCamera();

	void onOpenGlReady(int viewPortWidth, int viewPortHeight);

	void render();
}
