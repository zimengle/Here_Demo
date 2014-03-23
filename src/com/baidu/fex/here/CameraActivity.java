package com.baidu.fex.here;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;

import com.baidu.fex.here.camera.CameraPreview;
import com.baidu.fex.here.camera.ResizableCameraPreview;
import com.baidu.fex.here.dao.DataBaseHelper;
import com.baidu.fex.here.dao.Picture;
import com.baidu.fex.here.utils.Dirctionary;
import com.baidu.fex.here.utils.Utils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class CameraActivity extends Activity implements OnClickListener,
		PreviewCallback, BDLocationListener {

	public static final String PARAM_PID = "PARAM_PID";

	private ResizableCameraPreview mPreview;
	private RelativeLayout mLayout;
	private int mCameraId = 0;
	private LocationClient locationClient;
	private BDLocation bdLocation;
	private DataBaseHelper dataBaseHelper;
	private String pid;

	public static void open(Context context, String pid) {
		Intent intent = new Intent(context, CameraActivity.class);
		intent.putExtra(PARAM_PID, pid);
		context.startActivity(intent);
	}

	public static void open(Context context) {
		open(context, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pid = getIntent().getStringExtra(PARAM_PID);
		dataBaseHelper = new DataBaseHelper(this);
		locationClient = Utils.getLocation(this);
		locationClient.registerLocationListener(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		mLayout = (RelativeLayout) findViewById(R.id.layout);

		if (pid != null) {
			try {
				Picture picture = dataBaseHelper.getPictureDao()
						.queryForId(pid);
				ImageView imageView = (ImageView) findViewById(R.id.mask);
				imageView.setVisibility(View.VISIBLE);
				Utils.getImageLoader(this).displayImage(
						Picture.toUriString(picture.getUrl()), imageView);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		findViewById(R.id.capture).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationClient.start();
		createCameraPreview();
	}

	@Override
	protected void onPause() {
		super.onPause();
		camera.setPreviewCallback(null);
		mPreview.stop();
		mLayout.removeView(mPreview);
		mPreview = null;
	}

	private void createCameraPreview() {
		mPreview = new ResizableCameraPreview(this, mCameraId,
				CameraPreview.LayoutMode.FitToParent, false);
		mPreview.setPreviewCallback(this);
		LayoutParams previewLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mLayout.addView(mPreview, 0, previewLayoutParams);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.capture:
			capture();
			break;
		}
	}

	private void capture() {
		locationClient.requestLocation();
		File dir = Dirctionary.getDir();
		dir.mkdirs();
		Long tsLong = System.currentTimeMillis() / 1000;
		File file = new File(dir, tsLong + ".jpg");
		try {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();
			YuvImage image = new YuvImage(data, parameters.getPreviewFormat(),
					size.width, size.height, null);

			FileOutputStream filecon = new FileOutputStream(file);
			image.compressToJpeg(
					new Rect(0, 0, image.getWidth(), image.getHeight()), 90,
					filecon);
		} catch (FileNotFoundException e) {
			Toast toast = Toast
					.makeText(getBaseContext(), e.getMessage(), 1000);
			toast.show();
		}
		
		
		Intent intent = new Intent(this, CameraConfirmActivity.class);
		intent.putExtra(CameraConfirmActivity.PARAM_PICTURE, Picture.createPicture(this,bdLocation, file, pid));
		startActivityForResult(intent,REQ_CONFIRM);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQ_CONFIRM && resultCode == RESULT_OK){
			finish();
		}
	}
	
	private static final int REQ_CONFIRM = 10;

	private byte[] data;

	private Camera camera;

	public void onPreviewFrame(byte[] data, Camera camera) {
		this.data = data;
		this.camera = camera;

	}

	public void onReceiveLocation(BDLocation bdLocation) {
		this.bdLocation = bdLocation;
		Log.d("zzm", bdLocation.toString());
	}

	public void onReceivePoi(BDLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
		case KeyEvent.KEYCODE_POWER:
			capture();
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

}
