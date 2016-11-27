package org.ilapin.arvelocity.free;

import android.Manifest;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.ilapin.arvelocity.graphics.CameraPreview;
import org.ilapin.arvelocity.graphics.MainScene;
import org.ilapin.arvelocity.graphics.SceneRenderer;
import org.ilapin.arvelocity.ui.MessageDialog;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MessageDialog.Listener {

	public static final int CAMERA_PERMISSION_REQUEST_CODE = 12345;

	private GLSurfaceView mGlSurfaceView;
	private boolean mIsRendererSet;

	@Inject
	MainScene mMainScene;

	@Inject
	CameraPreview mCameraPreview;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		App.getApplicationComponent().inject(this);

		mCameraPreview.setActivity(this);

		mGlSurfaceView = new GLSurfaceView(this);
		final SceneRenderer renderer = new SceneRenderer(mMainScene, mCameraPreview);

		final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		final boolean isSupportsEs2 = activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000
				|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && (Build.FINGERPRINT.startsWith("generic")
				|| Build.FINGERPRINT.startsWith("unknown")
				|| Build.MODEL.contains("google_sdk")
				|| Build.MODEL.contains("Emulator")
				|| Build.MODEL.contains("Android SDK built for x86")));

		if (isSupportsEs2) {
			mGlSurfaceView.setEGLContextClientVersion(2);
			mGlSurfaceView.setRenderer(renderer);
			mIsRendererSet = true;
		} else {
			MessageDialog.newInstance(
					"Error",
					"This device does not support OpenGL ES 2.0",
					getString(android.R.string.ok),
					getString(android.R.string.cancel)
			).show(getSupportFragmentManager(), "MessageDialog");

			return;
		}

		setContentView(mGlSurfaceView);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mIsRendererSet) {
			mGlSurfaceView.onResume();
		}

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
				ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA },
						CAMERA_PERMISSION_REQUEST_CODE);
			}
		} else {
			Log.d("!@#", "Access to camera granted");
		}

		mCameraPreview.start();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mIsRendererSet) {
			mGlSurfaceView.onPause();
		}

		mCameraPreview.stop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mCameraPreview.setActivity(null);
	}

	@Override
	public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
			@NonNull final int[] grantResults) {
		if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Log.d("!@#", "Access to camera granted");
			}
		}
	}

	@Override
	public void onPositiveButtonClick(final MessageDialog dialog) {
		finish();
	}

	@Override
	public void onNegativeButtonClick(final MessageDialog dialog) {
		finish();
	}
}
