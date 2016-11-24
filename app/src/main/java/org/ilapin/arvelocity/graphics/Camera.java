package org.ilapin.arvelocity.graphics;

import org.ilapin.common.geometry.Point;

public class Camera {

	private Point mPosition = new Point();
	private Point mLookAt = new Point();

	private final float mWidth;
	private final float mHeight;

	public Camera(final float width, final float height) {
		mWidth = width;
		mHeight = height;
	}

	public Point getPosition() {
		return mPosition;
	}

	public void setPosition(final Point position) {
		mPosition = position;
	}

	public Point getLookAt() {
		return mLookAt;
	}

	public void setLookAt(final Point lookAt) {
		mLookAt = lookAt;
	}

	public float getWidth() {
		return mWidth;
	}

	public float getHeight() {
		return mHeight;
	}
}
