package org.ilapin.arvelocity.graphics;

public interface Renderable {

	void onOpenGlReady();

	void render(Scene scene);
}
