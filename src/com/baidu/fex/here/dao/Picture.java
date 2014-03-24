package com.baidu.fex.here.dao;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.baidu.fex.here.GalleryFragment.Model;
import com.baidu.location.BDLocation;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName="picture")
public class Picture implements Serializable{

	public static final String COLUMN_RID = "rid";
	
	public static final int ORIENTATION_LANDSCAPE = 1;
	
	@DatabaseField(id=true)
	protected String id;
	
	@DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = "pid")
	private Picture parent;
	
	@DatabaseField (foreign = true, foreignAutoRefresh = true, columnName = COLUMN_RID)
	private Picture root;
	
	@DatabaseField
	private float sensorX;
	
	@DatabaseField
	private float sensorY;
	
	@DatabaseField
	private float sensorZ;
	
	@DatabaseField
	private long datetime;
	
	@DatabaseField
	private double latitude;
	
	@DatabaseField
	private double lontitude;
	
	@DatabaseField
	private double radius;
	
	@DatabaseField
	private String url;
	
	@DatabaseField
	private int orientation;
	
	@DatabaseField
	private int width;
	
	@DatabaseField
	private int height;
	
	@DatabaseField
	private long size;
	
	
	private Picture(){
		
	}
	
	
	


	public String getId() {
		return id;
	}



	public Picture getParent() {
		return parent;
	}



	public long getDatetime() {
		return datetime;
	}



	public double getLatitude() {
		return latitude;
	}



	public double getLontitude() {
		return lontitude;
	}



	public double getRadius() {
		return radius;
	}



	public String getUrl() {
		return url;
	}



	public int getOrientation() {
		return orientation;
	}



	public int getWidth() {
		return width;
	}



	public int getHeight() {
		return height;
	}



	public long getSize() {
		return size;
	}
	
	public Picture getRoot() {
		return root;
	}
	
	public static List<Picture> findPictureByPid(Context context,String rid) throws SQLException{
		List<Picture> list = new ArrayList<Picture>();
		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
		Where<Picture, String> where = null;
		if(rid == null){
			where = dataBaseHelper.getPictureDao().queryBuilder().where().isNull(Picture.COLUMN_RID);
		}else{
			list.add(dataBaseHelper.getPictureDao().queryForId(rid));
			where = dataBaseHelper.getPictureDao().queryBuilder().where().eq(Picture.COLUMN_RID, rid);
		}
		list.addAll(where.query());
		return list;
	}
	
//	public static List<Picture> getRootList(Context context){
//		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
//		dataBaseHelper.getPictureDao().queryBuilder().groupBy(COLUMN_RID).join(dataBaseHelper.getPictureDao().queryBuilder())
//	}
	
	
	public static void remove(Context context,Picture picture) throws SQLException{
		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
		dataBaseHelper.getPictureDao().delete(picture);
		new File(picture.getUrl()).deleteOnExit();
	}



	public static Picture createPicture(Context context,BDLocation location,float[] sensor,File file,final String pid){
		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
		Picture picture = new Picture();
		picture.id = UUID.randomUUID().toString();
		if(pid != null){
			try {
				picture.parent = dataBaseHelper.getPictureDao().queryForId(pid);
				
				if(picture.parent.root == null){
					picture.root = picture.parent;
				}else{
					picture.root = picture.parent.root;
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(sensor != null ){
			picture.sensorX = sensor[0];
			picture.sensorY = sensor[1];
			picture.sensorZ = sensor[2];
		}
		
		picture.datetime = System.currentTimeMillis();
		picture.latitude = location.getLatitude();
		picture.lontitude = location.getLongitude();
		picture.radius = location.getRadius();
		picture.url = file.toString();
		picture.orientation = ORIENTATION_LANDSCAPE;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picture.url, options);
		picture.width = options.outWidth;
		picture.height = options.outHeight;
		picture.size = file.length();
		return picture;
		
	}
	
	public static String toUriString(String filepath){
		return Uri.fromFile(new File(filepath)).toString();
	}
	
	
	
}
