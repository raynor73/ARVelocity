package org.ilapin.arvelocity.graphics.squaresurface;

import org.ilapin.arvelocity.graphics.Renderable;

public class SquareSurface implements Renderable {

	private static final int NUMBER_OF_POSITION_COMPONENTS = 2;
	private static final int NUMBER_OF_LINE_COMPONENTS = NUMBER_OF_POSITION_COMPONENTS * 2;
	private final float[] mLinesData;

	public SquareSurface(final float cellSize, final int cellsOnEdge) {
		mLinesData = new float[2 * (cellsOnEdge + 1) * NUMBER_OF_LINE_COMPONENTS];

		final float size = cellSize * cellsOnEdge;
		final float halfSize = size / 2;

		int indexOfLine = 0;

		for (int i = 0; i < cellsOnEdge; i++) {
			indexOfLine = addLine(
					mLinesData,
					indexOfLine,
					-halfSize + i * cellSize,
					halfSize,
					-halfSize + i * cellSize,
					-halfSize
			);
		}
		indexOfLine = addLine(
				mLinesData,
				indexOfLine,
				halfSize,
				halfSize,
				halfSize,
				-halfSize
		);

		for (int i = 0; i < cellsOnEdge; i++) {
			indexOfLine = addLine(
					mLinesData,
					indexOfLine,
					-halfSize,
					-halfSize + i * cellSize,
					halfSize,
					-halfSize + i * cellSize
			);
		}
		addLine(
				mLinesData,
				indexOfLine,
				-halfSize,
				halfSize,
				halfSize,
				halfSize
		);
	}

	private int addLine(final float[] verticesData, final int indexOfLine, final float x1, final float y1,
						final float x2, final float y2) {
		verticesData[indexOfLine * NUMBER_OF_LINE_COMPONENTS] = x1;
		verticesData[indexOfLine * NUMBER_OF_LINE_COMPONENTS + 1] = y1;
		verticesData[indexOfLine * NUMBER_OF_LINE_COMPONENTS + 2] = x2;
		verticesData[indexOfLine * NUMBER_OF_LINE_COMPONENTS + 3] = y2;
		return indexOfLine + 1;
	}

	@Override
	public void onOpenGlReady() {

	}

	@Override
	public void render() {

	}
}
