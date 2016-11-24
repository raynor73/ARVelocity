package org.ilapin.common.geometry;

public class Vector extends Point {

	public Vector() {}

	public Vector(final float x, final float y, final float z) {
		super(x, y, z);
	}

	public Vector multiply(final float c) {
		return new Vector(
				c * mX,
				c * mY,
				c * mZ
		);
	}

	public Vector add(final Vector a) {
		return new Vector(
				mX + a.mX,
				mY + a.mY,
				mZ + a.mZ
		);
	}
}
