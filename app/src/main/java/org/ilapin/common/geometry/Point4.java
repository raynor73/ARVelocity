package org.ilapin.common.geometry;

public class Point4 {

	protected final float mX;
	protected final float mY;
	protected final float mZ;
	protected final float mW;

	public Point4() {
		this(0, 0, 0, 0);
	}

	public Point4(final float x, final float y, final float z, final float w) {
		mX = x;
		mY = y;
		mZ = z;
		mW = w;
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

	public float getW() {
		return mW;
	}
}
