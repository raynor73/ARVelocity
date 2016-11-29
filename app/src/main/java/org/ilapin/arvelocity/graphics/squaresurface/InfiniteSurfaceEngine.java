package org.ilapin.arvelocity.graphics.squaresurface;

import org.ilapin.common.geometry.Vector;

public class InfiniteSurfaceEngine {

	private final float mSize;

	private Vector mVelocity = new Vector();
	private Vector mOffset = new Vector();
	private long mLastTimestamp = -1;

	private InfiniteSurfaceEngine(final float size) {
		mSize = size;
	}

	public void setVelocity(final Vector velocity) {
		mVelocity = velocity;
	}

	public Vector getOffset() {
		return mOffset;
	}

	public void tick(final long timestamp) {
		if (mLastTimestamp < 0) {
			mLastTimestamp = timestamp;
			return;
		}

		move(timestamp - mLastTimestamp);
		mLastTimestamp = timestamp;
	}

	private void move(final long dt) {
		final Vector newOffset = mOffset.add(mVelocity.multiply(dt));
		final float clampedX  = newOffset.getX() > mSize ? newOffset.getX() % mSize : newOffset.getX();
		final float clampedZ  = newOffset.getZ() > mSize ? newOffset.getZ() % mSize : newOffset.getZ();
		mOffset = new Vector(clampedX, 0, clampedZ);
	}
}
