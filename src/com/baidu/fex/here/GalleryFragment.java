package com.baidu.fex.here;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


import com.baidu.fex.here.dao.DataBaseHelper;
import com.baidu.fex.here.dao.Picture;
import com.baidu.fex.here.utils.Utils;
import com.j256.ormlite.stmt.Where;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryFragment extends Fragment implements OnItemClickListener{

	public static GalleryFragment create(String rid){
		Bundle arguments = new Bundle();
		arguments.putString(PARAM_ROOT_ID, rid);
		GalleryFragment fragment = new GalleryFragment();
		fragment.setArguments(arguments);
		return fragment;
	}
	
	public static interface OnGalleryItemClickListener{
		public void onGalleryItemClick(Model model);
	}
	
	private List<Model> list = new ArrayList<Model>();

	private GalleryAdapter adapter;

	private DataBaseHelper dataBaseHelper;
	
	private OnGalleryItemClickListener onGalleryItemClickListener;
	
	public static final String PARAM_ROOT_ID = "PARAM_ROOT_ID";
	
	private String rootId;

	public void setOnGalleryItemClickListener(
			OnGalleryItemClickListener onGalleryItemClickListener) {
		this.onGalleryItemClickListener = onGalleryItemClickListener;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		dataBaseHelper = new DataBaseHelper(getActivity());
		rootId = getArguments().getString(PARAM_ROOT_ID);

	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.gallery_layout, null);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		list.clear();
		
		try {
			Where<Picture, String> where = null;
			if(rootId == null){
				where = dataBaseHelper.getPictureDao().queryBuilder().where().isNull(Picture.COLUMN_RID);
			}else{
				list.add(Model.toModel(dataBaseHelper.getPictureDao().queryForId(rootId)));
				where = dataBaseHelper.getPictureDao().queryBuilder().where().eq(Picture.COLUMN_RID, rootId);
			}
			List<Picture> results = where.query();
			for(Picture picture : results){
				
				list.add(Model.toModel(picture));
			}
			adapter.notifyDataSetChanged();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		GridView gridView = (GridView) view.findViewById(R.id.gridview);
		gridView.setOnItemClickListener(this);

		adapter = new GalleryAdapter(getActivity(), list);

		gridView.setAdapter(adapter);
	}

	public static class Model {

		private static SimpleDateFormat myFmt1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

		private String id;

		private String url;

		private String datetime;

		public Model(String id, String url, long datetime) {
			super();
			url = Picture.toUriString(url);
			this.id = id;
			this.url = url;
			this.datetime = myFmt1.format(new Date(datetime));
		}

		public String getId() {
			return id;
		}

		public String getUrl() {
			return url;
		}

		public String getDatetime() {
			return datetime;
		}
		
		public static Model toModel(Picture picture){
			return new Model(picture.getId(), picture.getUrl(), picture.getDatetime());
		}
		

	}

	private static class GalleryAdapter extends BaseAdapter {

		private Context context;

		private List<Model> list;

		private ImageLoader imageLoader;

		public GalleryAdapter(Context context, List<Model> list) {
			super();
			this.context = context;
			this.list = list;
			imageLoader = Utils.getImageLoader(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Model model = list.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.gallery_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			imageLoader.displayImage(model.url, holder.imageView);

			holder.textView.setText(model.datetime);

			return convertView;
		}

		private static class ViewHolder {

			private ImageView imageView;

			private TextView textView;

			public ViewHolder(View view) {
				imageView = (ImageView) view.findViewById(R.id.imageview);
				textView = (TextView) view.findViewById(R.id.textview);
			}

		}

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(onGalleryItemClickListener != null){
			onGalleryItemClickListener.onGalleryItemClick(list.get(position));
		}

	}

}
