package com.baidu.fex.here.utils;

import java.io.File;

import android.net.Uri;

public class Dirctionary {

	private static File dir = new File("/sdcard/here/");
	
	public static File getDir() {
		return dir;
	}
	
	public static Uri getLastFile(){
		File[] files = dir.listFiles();
		if (files.length == 0) {
		    return null;
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
		   if (lastModifiedFile.lastModified() < files[i].lastModified()) {
		       lastModifiedFile = files[i];
		   }
		}
		return Uri.fromFile(lastModifiedFile);
	}
	
}
