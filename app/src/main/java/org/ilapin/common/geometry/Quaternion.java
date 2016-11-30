package org.ilapin.common.geometry;

public class Quaternion {

	public static Point4 createFrom(final float x, final float y, final float z, final float a) {
		final float angleInRadians = (float) Math.toRadians(a);
		return new Point4(
				(float) (x * Math.sin(angleInRadians / 2)),
				(float) (y * Math.sin(angleInRadians / 2)),
				(float) (z * Math.sin(angleInRadians / 2)),
				(float) (Math.cos(angleInRadians / 2))
		);
	}

	public static Point4 normalize(final Point4 quaternion) {
		final float n = (float) (1.0 / Math.sqrt(quaternion.getX() * quaternion.getX() +
				quaternion.getY() * quaternion.getY() + quaternion.getZ() * quaternion.getZ() +
				quaternion.getW() * quaternion.getW()));
		return new Point4(
				quaternion.getX() * n,
				quaternion.getY() * n,
				quaternion.getZ() * n,
				quaternion.getW() * n
		);
	}

	public static void calculateRotationMatrix(final float[] matrix, final int offset, final Point4 quaternion) {
		final Point4 normalizedRotationQuaternion = Quaternion.normalize(quaternion);

		final float xx = quaternion.getX() * quaternion.getX();
		final float xy = quaternion.getX() * quaternion.getY();
		final float xz = quaternion.getX() * quaternion.getZ();
		final float xw = quaternion.getX() * quaternion.getW();

		final float yy = quaternion.getY() * quaternion.getY();
		final float yz = quaternion.getY() * quaternion.getZ();
		final float yw = quaternion.getY() * quaternion.getW();

		final float zz = quaternion.getZ() * quaternion.getZ();
		final float zw = quaternion.getZ() * quaternion.getW();

		matrix[offset] = 1 - 2 * yy - 2 * zz;
		matrix[offset + 1] = 2 * xy + 2 * zw;
		matrix[offset + 2] = 2 * xz - 2 * yw;
		matrix[offset + 3] = 0;

		matrix[offset + 4] = 2 * xy - 2 * zw;
		matrix[offset + 5] = 1 - 2 * xx - 2 * zz;
		matrix[offset + 6] = 2 * yz + 2 * xw;
		matrix[offset + 7] = 0;

		matrix[offset + 8] = 2 * xz + 2 * yw;
		matrix[offset + 9] = 2 * yz - 2 * xw;
		matrix[offset + 10] = 1 - 2 * xx - 2 * yy;
		matrix[offset + 11] = 0;

		matrix[offset + 12] = 0;
		matrix[offset + 13] = 0;
		matrix[offset + 14] = 0;
		matrix[offset + 15] = 1;
	}
}
