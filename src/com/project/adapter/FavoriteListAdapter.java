package com.project.adapter;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.control.ImageLoader;
import com.project.main.MakkoDetailActivity;
import com.project.model.ComicLite;
import com.project.model.oComic;
import com.project.view.R;

public class FavoriteListAdapter extends BaseAdapter{

	ComicLite[] comics;
	private Activity activity;
	ImageLoader imageLoader;
	int resId;
	private static LayoutInflater inflater=null;

	public FavoriteListAdapter(Context context, int textViewResourceId, ComicLite[] comics) {
		super();
		this.comics=comics;
		this.activity=(Activity) context;
		this.resId=textViewResourceId;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity.getApplicationContext());

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comics.length;
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comics[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.list_favorite, null);
			holder=new ViewHolder();

			holder.title = (TextView) vi.findViewById(R.id.title);
			holder.genre = (TextView) vi.findViewById(R.id.genre);
			holder.writer = (TextView) vi.findViewById(R.id.writer);
			holder.artist = (TextView) vi.findViewById(R.id.artist);
			holder.pdate = (TextView) vi.findViewById(R.id.pdate);
			holder.img = (ImageView) vi.findViewById(R.id.img);
			//	          holder.ll=(LinearLayout)vi.findViewById(R.id.linearLayout2);
			//	          holder.txtName	=(TextView) holder.ll.findViewById(R.id.name);
			//	          holder.txtAddr 	=(TextView) holder.ll.findViewById(R.id.addr);
			//	          
			//	          holder.img=(ImageView)vi.findViewById(R.id.image);
			//	          holder.img.setBackgroundColor(Color.TRANSPARENT);
			//holder.img.setBackgroundResource(NO_SELECTION);
			//holder.imgBtn=(ImageButton)vi.findViewById(R.id.btnCinemaDetail);
			//	          holder.imgArr=(ImageButton)vi.findViewById(R.id.imageArrow);


			vi.setTag(holder);      
		}
		else{
			holder = (ViewHolder) vi.getTag();
		}

		Typeface tf_bold = Typeface.createFromAsset(activity.getAssets(), "fonts/DINNextLTPro-Bold.otf");
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/DINNextLTPro-BoldCondensed.otf");

		holder.title.setText(comics[position].getTitle());
		holder.title.setTypeface(tf_bold);
		holder.genre.setText("GENRE : " + comics[position].getGenre());
		holder.genre.setTypeface(tf);
		holder.writer.setText("WRITER : " + comics[position].getWritter());	
		holder.writer.setTypeface(tf);
		holder.artist.setText("ARTIST : " + comics[position].getArtist());
		holder.artist.setTypeface(tf);
		holder.pdate.setText("PUBLISHED DATE : " + comics[position].getDate());
		holder.pdate.setTypeface(tf);

		File imgFile = new  File("/sdcard/.Makko/"+comics[position].getId()+"/image.png");
		if(imgFile.exists()){

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			holder.img.setImageBitmap(myBitmap);

		}

		final String id = comics[position].getId();

		vi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(activity,MakkoDetailActivity.class);
				intent.putExtra("id", id);
				oComic.idComic = id;
				activity.startActivity(intent);
			}
		});

//		notifyDataSetChanged();

		vi.setClickable(true);
		vi.setFocusable(true);

		return vi;
	}
	static class ViewHolder{
		TextView title;
		TextView genre;
		TextView writer;
		TextView artist;
		TextView pdate;
		//		LinearLayout ll;
		ImageView img;
		//		ImageButton imgArr;
	}
}
