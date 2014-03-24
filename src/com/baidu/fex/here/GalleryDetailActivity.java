package com.baidu.fex.here;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.baidu.fex.here.dao.Picture;
import com.baidu.fex.here.utils.ConfirmDialog;
import com.baidu.fex.here.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class GalleryDetailActivity extends Activity implements OnPageChangeListener,android.view.View.OnClickListener{

	public static void open(Context context,String pid,int selected){
		Intent intent = new Intent(context, GalleryDetailActivity.class);
		intent.putExtra(PARAM_RID, pid);
		intent.putExtra(PARAM_SELECTED, selected);
		context.startActivity(intent);
	}
	
	public static final String PARAM_RID = "Rid";
	
	public static final String PARAM_SELECTED = "PARAM_SELECTED";
	
	private ViewPager viewPager;
	
	private TextView titleText;
	
	private String rid;
	
	private int selected;
	
	private List<Picture> pictures = new ArrayList<Picture>();
	
	private SamplePagerAdapter adapter;
	
	private View delBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rid = getIntent().getStringExtra(PARAM_RID);
		selected = getIntent().getIntExtra(PARAM_SELECTED, 0);
		setContentView(R.layout.gallery_detail_layout);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setOnPageChangeListener(this);
		adapter = new SamplePagerAdapter(this, pictures);
		viewPager.setAdapter(adapter);
		titleText = (TextView) findViewById(R.id.page_no);
		delBtn = findViewById(R.id.del);
		delBtn.setOnClickListener(this);
		findViewById(R.id.capture).setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		pictures.clear();
		try {
			pictures.addAll(Picture.findPictureByPid(this, rid));
			adapter.notifyDataSetChanged();
			viewPager.setCurrentItem(selected);
			onPageSelected(selected);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static class SamplePagerAdapter extends PagerAdapter {

		
		private Context context;
		
		private List<Picture> list;
		
		private ImageLoader imageLoader;

		public SamplePagerAdapter(Context context, List<Picture> list) {
			this.context = context;
			this.list = list;
			imageLoader = Utils.getImageLoader(context);
		}

		@Override
		public int getCount() {
			return list.size();
		}
		
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			Picture picture = list.get(position);
			PhotoView photoView = new PhotoView(container.getContext());
			imageLoader.displayImage(Picture.toUriString(picture.getUrl()), photoView);

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	public void onPageSelected(int position) {
		if(position == 0){
			delBtn.setVisibility(View.GONE);
		}else{
			delBtn.setVisibility(View.VISIBLE);
		}
		selected = position;
		titleText.setText((selected+1) + "/"+pictures.size());
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.del:
			ConfirmDialog.open(this, "删除", "确认要删除该图片吗？", new ConfirmDialog.OnClickListener() {
				public void onPositiveClick() {
					ondel();
				}
				public void onNegativeClick() {
				}
			});
			break;
		case R.id.capture:
			oncapture();
			break;
		}
		
	}

	private void oncapture() {
		Picture picture = pictures.get(selected);
		CameraActivity.open(this, picture.getId());
	}

	private void ondel() {
		Picture picture = pictures.get(selected);
		try {
			Picture.remove(this, picture);
			pictures.remove(picture);
			adapter.notifyDataSetChanged();
			onPageSelected(selected);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
