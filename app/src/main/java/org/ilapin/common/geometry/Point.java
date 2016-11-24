package org.ilapin.common.geometry;

public class Point {

	protected final float mX;
	protected final float mY;
	protected final float mZ;

	public Point() {
		this(0, 0, 0);
	}

	public Point(final float x, final float y, final float z) {
		mX = x;
		mY = y;
		mZ = z;
	}

	public float getX() {
		return mX;
	}

	public float getY() {
		return mY;
	}

	public float getZ() {
		return mZ;
	}
}
