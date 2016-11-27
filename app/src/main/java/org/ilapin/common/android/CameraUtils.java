package org.ilapin.common.android;

import android.hardware.Camera;

import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class CameraUtils {

	public static Camera.Size calculateLargestPreviewSize(final Camera camera) {
		final Stream<Camera.Size> stream = StreamSupport.stream(camera.getParameters().getSupportedPreviewSizes());
		return stream.reduce(null, (a, b) -> {
			if (a == null || (b.width > a.width && b.height > a.height)) {
				return b;
			} else {
				return a;
			}
		});
	}
}
