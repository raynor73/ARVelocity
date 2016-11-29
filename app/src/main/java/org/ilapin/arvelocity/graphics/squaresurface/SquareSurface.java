package org.ilapin.arvelocity.graphics.squaresurface;

import android.content.Context;
import org.ilapin.arvelocity.graphics.Renderable;
import org.ilapin.arvelocity.graphics.Scene;
import org.ilapin.common.android.opengl.VertexArray;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.glDrawArrays;

public class SquareSurface implements Renderable {

	private static final int NUMBER_OF_POSITION_COMPONENTS = 2;
	private static final int NUMBER_OF_LINE_COMPONENTS = NUMBER_OF_POSITION_COMPONENTS * 2;
	private final float[] mLinesData;

	private final Context mContext;
	private final Scene mScene;

	private SquareSurfaceShaderProgram mShaderProgram;
	private VertexArray mVertexArray;

	public SquareSurface(final Context context, final Scene scene, final float cellSize, final int cellsOnEdge) {
		mContext = context;
		mScene = scene;

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
		mShaderProgram = new SquareSurfaceShaderProgram(mContext);
		mVertexArray = new VertexArray(mLinesData);
	}

	@Override
	public void render() {
		mShaderProgram.useProgram();

		mShaderProgram.setUniforms(mScene.getViewProjectionMatrix(), 0, 0.5f, 0, 1);

		mVertexArray.setVertexAttribPointer(
				0,
				mShaderProgram.getPositionAttributeLocation(),
				NUMBER_OF_POSITION_COMPONENTS,
				0
		);

		glDrawArrays(GL_LINES, 0, mLinesData.length);
	}
}
