package com.baidu.fex.here;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.selector).setOnClickListener(this);
        findViewById(R.id.capture).setOnClickListener(this);

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }



	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selector:
			GalleryActivity.open(this);
			break;
		case R.id.capture:
			CameraActivity.open(this);
			break;
		}
		
	}
	
	
	
    
}
