package com.project.adapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.control.Utils;
import com.project.main.GalleryActivity;
import com.project.model.oComic;
import com.project.view.R;


public class PageAdapter extends BaseAdapter {

	private Context mContext;

	SharedPreferences sharedPreferences;

	Bitmap[] bm;
	File makkoImages = Environment.getExternalStorageDirectory();  
	File makkoFolder = new File(makkoImages + "/.Makko" + "/" + oComic.idComic + "/" + oComic.idEpisode + "/");
	File[] imagelist = makkoFolder.listFiles(new FilenameFilter(){ 
		public boolean accept(File dir, String name) {  
			return ((name.endsWith(".jpg"))||(name.endsWith(".png")));  
		}
	});

	public PageAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;

		Arrays.sort(imagelist, new Comparator<File>(){
			public int compare(File f1, File f2)
			{
				return f1.getName().compareTo(f2.getName());
			} 
		});

		BitmapFactory.Options bfOptions = new BitmapFactory.Options();  
		bfOptions.inSampleSize = 5;
		bfOptions.inDither=false;                     
		bfOptions.inPurgeable=true;                   
		bfOptions.inInputShareable=true;              
		bfOptions.inTempStorage=new byte[32 * 1024];

		bm = new Bitmap[imagelist.length];

		for (int i=0; i<imagelist.length;i++)
		{
			bm[i] = BitmapFactory.decodeFile(imagelist[i].getAbsolutePath(), bfOptions);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imagelist.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imagelist[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View grid;

		if(convertView==null){
			grid = new View(mContext);
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			grid=inflater.inflate(R.layout.grid_page, parent, false);
		}else{
			grid = (View)convertView;
		}

		Typeface tf_bold = Typeface.createFromAsset(mContext.getAssets(), "fonts/DINNextLTPro-Bold.otf");
		TextView textView = (TextView)grid.findViewById(R.id.prevpage);
		textView.setTypeface(tf_bold);
		textView.setText(String.valueOf(position));

		ImageView imageView = (ImageView)grid.findViewById(R.id.previmage);
		imageView.setImageBitmap(bm[position]);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, GalleryActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Utils.SavePreferences(oComic.idEpisode, position, (Activity)mContext );
				mContext.startActivity(intent);
			}
		});

		return grid;		
	}
}