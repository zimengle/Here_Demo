package com.baidu.fex.here;

import java.io.File;
import java.sql.SQLException;

import uk.co.senab.photoview.PhotoViewAttacher;

import com.baidu.fex.here.dao.DataBaseHelper;
import com.baidu.fex.here.dao.Picture;
import com.baidu.fex.here.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraConfirmActivity extends Activity implements OnClickListener{
	
	
	
	public static final String PARAM_PICTURE = "PARAM_PICTURE";

	private DataBaseHelper dataBaseHelper;
	
	private Picture picture;
	
	private ImageLoader imageLoader;
	
	private PhotoViewAttacher photoViewAttacher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_layout);
		final ImageView imageView = (ImageView) findViewById(R.id.imageview);
		
		dataBaseHelper = new DataBaseHelper(this);
		picture = (Picture) getIntent().getSerializableExtra(PARAM_PICTURE);
		imageLoader = Utils.getImageLoader(this);
		imageLoader.displayImage(Picture.toUriString(picture.getUrl()), imageView,new ImageLoadingListener() {
			
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				photoViewAttacher = new PhotoViewAttacher(imageView);
				
			}
			
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
		findViewById(R.id.ok).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
	}
	
	private void save(){
		try {
			dataBaseHelper.getPictureDao().create(picture);
			setResult(RESULT_OK);
			Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT);
			finish();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void finish() {
		
		new File(picture.getUrl()).deleteOnExit();
		
		super.finish();
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok:
			save();
			break;
		case R.id.cancel:
			finish();
			break;
		}
	}
	
	
	
	
	
}
