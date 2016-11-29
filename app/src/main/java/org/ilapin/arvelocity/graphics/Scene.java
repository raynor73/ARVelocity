package org.ilapin.arvelocity.graphics;

public interface Scene {

	float[] getViewProjectionMatrix();

	void onOpenGlReady(int viewPortWidth, int viewPortHeight);

	void render();
}
