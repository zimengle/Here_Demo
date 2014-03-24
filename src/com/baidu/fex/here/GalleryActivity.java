package com.baidu.fex.here;


import java.util.List;

import com.baidu.fex.here.GalleryFragment.Model;
import com.baidu.fex.here.GalleryFragment.OnGalleryItemClickListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


public class GalleryActivity extends FragmentActivity {


	
	public static void open(Context context) {
		context.startActivity(new Intent(context, GalleryActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);
		GalleryFragment fragment = GalleryFragment.create(null);
		fragment.setOnGalleryItemClickListener(new OnGalleryItemClickListener() {
			
			public void onGalleryItemClick(List<Model> list, int selected) {
				final String id = list.get(selected).getId();
				GalleryFragment fragment = GalleryFragment.create(id);
				
				fragment.setOnGalleryItemClickListener(new OnGalleryItemClickListener() {
					

					public void onGalleryItemClick(List<Model> list,
							int selected) {
						GalleryDetailActivity.open(GalleryActivity.this,list.get(selected).getRid(),selected );
						
					}
				});
				switeFragment(fragment, "detail");
				
			}
		});
		switeFragment(fragment, null);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void switeFragment(Fragment fragment,String tag) {

			
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.root, fragment); // f2_container is your FrameLayout
											// container
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		if(tag != null){
			ft.addToBackStack(tag);
		}
		
		ft.commit();
	}

}
