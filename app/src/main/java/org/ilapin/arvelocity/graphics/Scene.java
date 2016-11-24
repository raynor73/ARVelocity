package org.ilapin.arvelocity.graphics;

public interface Scene {

	void onOpenGlReady(int viewPortWidth, int viewPortHeight);

	void render();
}
