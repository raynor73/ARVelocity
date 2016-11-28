package org.ilapin.arvelocity.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;
import org.ilapin.arvelocity.free.R;
import org.ilapin.arvelocity.sensor.Sensor;
import org.ilapin.common.android.CameraUtils;
import org.ilapin.common.android.opengl.IndexBuffer;
import org.ilapin.common.android.opengl.VertexBuffer;

import java.io.IOException;
import java.nio.IntBuffer;

import static android.opengl.GLES10.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
import static android.opengl.GLES20.*;
import static org.ilapin.common.Constants.BYTES_PER_FLOAT;

@SuppressWarnings("deprecation")
public class CameraPreview implements Renderable, Sensor {

	private static final String TAG = "CameraPreview";

	private static final int NUMBER_OF_POSITION_COMPONENTS = 2;
	private static final int NUMBER_OF_TEXTURE_COMPONENTS = 2;
	private static final int STRIDE = (NUMBER_OF_POSITION_COMPONENTS + NUMBER_OF_TEXTURE_COMPONENTS) * BYTES_PER_FLOAT;

	private final float[] mSurfaceTextureTransformMatrix = new float[16];

	private final float[] mVertices = new float[] {
			-1, 1, 0, 0,
			-1, -1, 0, 0,
			1, -1, 0, 0,
			1, 1, 0, 0
	};
	private final short[] mIndices = new short[] {
			0, 1, 2,
			2, 3, 0
	};

	private final Context mContext;

	private volatile Activity mActivity;
	private volatile boolean mIsTextureCoordinatesCalculated;
	private volatile boolean mHasPendingTextureCoordinatesCalculation;
	private volatile boolean mHasPendingStartPreview;

	private volatile boolean mIsOpenGlReady;

	private VertexBuffer mVertexBuffer;
	private IndexBuffer mIndexBuffer;
	private CameraPreviewShaderProgram mShaderProgram;
	private Camera mCamera;
	private int mTextureUnitLocation;
	private SurfaceTexture mSurfaceTexture;

	public CameraPreview(final Context context) {
		mContext = context;
	}

	@Override
	public void onOpenGlReady() {
		Log.d(TAG, "OpenGL ready...");
		if (mIsOpenGlReady) {
			Log.d(TAG, "OpenGL ready callback already fired, ignoring it...");
			return;
		}

		mIndexBuffer = new IndexBuffer(mIndices);
		mShaderProgram = new CameraPreviewShaderProgram(mContext);
		mTextureUnitLocation = initTextureUnit();

		if (mActivity != null) {
			Log.d(TAG, "Activity found, calculating texture coordinates...");
			calculateTextureCoordinates();
			mVertexBuffer = new VertexBuffer(mVertices);
			mIsTextureCoordinatesCalculated = true;
			mHasPendingTextureCoordinatesCalculation = false;
			if (mHasPendingStartPreview) {
				mHasPendingStartPreview = false;
				Log.d(TAG, "Pending camera preview found, starting preview...");
				startCameraPreview();
			}
		} else {
			Log.d(TAG, "Activity not found, postponing calculation of texture coordinates...");
			mHasPendingTextureCoordinatesCalculation = true;
		}

		mIsOpenGlReady = true;
	}

	@Override
	public void render() {
		if (mHasPendingStartPreview || mCamera == null) {
			return;
		}

		mSurfaceTexture.updateTexImage();

		mShaderProgram.useProgram();

		mVertexBuffer.setVertexAttribPointer(
				0,
				mShaderProgram.getPositionAttributeLocation(),
				NUMBER_OF_POSITION_COMPONENTS,
				STRIDE
		);
		mVertexBuffer.setVertexAttribPointer(
				NUMBER_OF_POSITION_COMPONENTS * BYTES_PER_FLOAT,
				mShaderProgram.getTextureCoordinateAttributeLocation(),
				NUMBER_OF_TEXTURE_COMPONENTS,
				STRIDE
		);

		// TODO Remove texture matrix and try to calculate texture coordinates considering view and camera preview aspect ratios.
		Matrix.setIdentityM(mSurfaceTextureTransformMatrix, 0);
		mShaderProgram.setUniforms(mTextureUnitLocation, mSurfaceTextureTransformMatrix);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBuffer.getBufferId());
		glDrawElements(GL_TRIANGLES, mIndices.length, GL_UNSIGNED_SHORT, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public void start() {
		Log.d(TAG, "Starting camera...");
		if (!mIsOpenGlReady) {
			Log.d(TAG, "OpenGL is not ready, postponing preview...");
			mHasPendingStartPreview = true;
		} else if (mIsTextureCoordinatesCalculated) {
			Log.d(TAG, "Texture coordinates calculated, starting preview...");
			mHasPendingStartPreview = false;
			startCameraPreview();
		} else {
			Log.d(TAG, "Texture coordinates not calculated, postponing preview...");
			mHasPendingStartPreview = true;
		}
	}

	@Override
	public void stop() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
		mIsOpenGlReady = false;
	}

	public void setActivity(final Activity activity) {
		Log.d(TAG, "Setting activity...");
		mActivity = activity;

		if (mActivity != null && mHasPendingTextureCoordinatesCalculation) {
			Log.d(TAG, "Pending texture coordinates calculation found, calculating texture coordinates...");
			mHasPendingTextureCoordinatesCalculation = false;
			calculateTextureCoordinates();
			mVertexBuffer = new VertexBuffer(mVertices);
			if (mHasPendingStartPreview) {
				Log.d(TAG, "Pending camera preview found, starting camera preview...");
				mHasPendingStartPreview = false;
				startCameraPreview();
			}
		}
	}

	private void startCameraPreview() {
		mCamera = Camera.open();

		if (mCamera != null) {
			final Camera.Parameters cameraParameters = mCamera.getParameters();
			final Camera.Size size = CameraUtils.calculateLargestPreviewSize(mCamera);
			cameraParameters.setPreviewSize(size.width, size.height);
			Log.d("!@#", "Camera preview width: " + size.width + "; height: " + size.height);
			mCamera.setParameters(cameraParameters);

			mSurfaceTexture = new SurfaceTexture(mTextureUnitLocation);

			try {
				mCamera.setPreviewTexture(mSurfaceTexture);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}

			mCamera.startPreview();
		} else {
			Toast.makeText(mActivity, R.string.error_can_not_open_camera, Toast.LENGTH_SHORT).show();
		}
	}

	private int initTextureUnit() {
		final IntBuffer intBuffer = IntBuffer.allocate(1);
		glGenTextures(1, intBuffer);
		glBindTexture(GL_TEXTURE_EXTERNAL_OES, intBuffer.get(0));
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		return intBuffer.get(0);
	}

	private void calculateTextureCoordinates() {
		switch (mActivity.getWindowManager().getDefaultDisplay().getRotation()) {
			case Surface.ROTATION_0:
				Log.d(TAG, "ROTATION_0 detected");
				// top left
				mVertices[2] = 0.0f;
				mVertices[3] = 0.99825f;
				// bottom left
				mVertices[6] = 1.0f;
				mVertices[7] = 0.99825f;
				// bottom right
				mVertices[10] = 1.0f;
				mVertices[11] = 0.0f;
				// top right
				mVertices[14] = 0.0f;
				mVertices[15] = 0.0f;
				/*
				// top left
				mVertices[2] = 0.0f;
				mVertices[3] = 1.0f;
				// bottom left
				mVertices[6] = 1.0f;
				mVertices[7] = 1.0f;
				// bottom right
				mVertices[10] = 1.0f;
				mVertices[11] = 0.0f;
				// top right
				mVertices[14] = 0.0f;
				mVertices[15] = 0.0f;
				*/
				break;

			case Surface.ROTATION_90:
				Log.d(TAG, "ROTATION_90 detected");
				// top left
				mVertices[2] = 0.0f;
				mVertices[3] = 0.0f;
				// bottom left
				mVertices[6] = 0.0f;
				mVertices[7] = 0.6941f;
				// bottom right
				mVertices[10] = 1.0f;
				mVertices[11] = 0.6941f;
				// top right
				mVertices[14] = 1.0f;
				mVertices[15] = 0.0f;
				/*
				// top left
				mVertices[2] = 0.0f;
				mVertices[3] = 0.0f;
				// bottom left
				mVertices[6] = 0.0f;
				mVertices[7] = 1.0f;
				// bottom right
				mVertices[10] = 1.0f;
				mVertices[11] = 1.0f;
				// top right
				mVertices[14] = 1.0f;
				mVertices[15] = 0.0f;
				*/
				break;

			case Surface.ROTATION_180:
				Log.d(TAG, "ROTATION_180 detected");
				// top left
				mVertices[2] = 1.0f;
				mVertices[3] = 0.0f;
				// bottom left
				mVertices[6] = 0.0f;
				mVertices[7] = 0.0f;
				// bottom right
				mVertices[10] = 0.0f;
				mVertices[11] = 1.0f;
				// top right
				mVertices[14] = 1.0f;
				mVertices[15] = 1.0f;
				break;

			case Surface.ROTATION_270:
				Log.d(TAG, "ROTATION_270 detected");
				// top left
				mVertices[2] = 1.0f;
				mVertices[3] = 1.0f;
				// bottom left
				mVertices[6] = 1.0f;
				mVertices[7] = 0.0f;
				// bottom right
				mVertices[10] = 0.0f;
				mVertices[11] = 0.0f;
				// top right
				mVertices[14] = 0.0f;
				mVertices[15] = 1.0f;
				break;

			default:
				Log.d(TAG, "Unknown orientation");
				Toast.makeText(mActivity, "Unknown orientation", Toast.LENGTH_SHORT).show();
				// top left
				mVertices[2] = 0.0f;
				mVertices[3] = 1.0f;
				// bottom left
				mVertices[6] = 1.0f;
				mVertices[7] = 1.0f;
				// bottom right
				mVertices[10] = 1.0f;
				mVertices[11] = 0.0f;
				// top right
				mVertices[14] = 0.0f;
				mVertices[15] = 0.0f;
		}
	}
}
