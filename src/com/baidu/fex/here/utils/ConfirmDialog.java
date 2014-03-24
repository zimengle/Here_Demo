package com.baidu.fex.here.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmDialog {

	public static interface OnClickListener{
		public void onPositiveClick();
		public void onNegativeClick();
	}

	public static void open(final Context context,String title,String message,final OnClickListener clickListener){
		new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if(clickListener != null){
		        	clickListener.onNegativeClick();
		        }
			}
		    })
		 .setNegativeButton("确定", new DialogInterface.OnClickListener() {

			 public void onClick(DialogInterface dialog, int whichButton) {
			        if(clickListener != null){
			        	clickListener.onPositiveClick();
			        }
			 }


		}).show();
	}

}
