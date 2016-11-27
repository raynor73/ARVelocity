package org.ilapin.common.android.opengl;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextResourceReader {

	/**
	 * Reads in text from a resource file and returns a String containing the
	 * text.
	 */
	public static String readTextFileFromResource(final Context context, final int resourceId) {
		final StringBuilder body = new StringBuilder();

		try {
			final InputStream inputStream =
					context.getResources().openRawResource(resourceId);
			final InputStreamReader inputStreamReader =
					new InputStreamReader(inputStream);
			final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String nextLine;

			while ((nextLine = bufferedReader.readLine()) != null) {
				body.append(nextLine);
				body.append('\n');
			}
		} catch (final IOException e) {
			throw new RuntimeException(
					"Could not open resource: " + resourceId, e);
		} catch (final Resources.NotFoundException nfe) {
			throw new RuntimeException("Resource not found: " + resourceId, nfe);
		}

		return body.toString();
	}
}
