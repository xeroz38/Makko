package com.project.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.component.CustomDialog;
import com.project.control.ImageLoader;
import com.project.main.MakkoDetailActivity;
import com.project.model.Comic;
import com.project.model.oComic;
import com.project.view.R;

public class ComicListAdapter extends BaseAdapter{
	/*int[] ID;
	String[] TITLES;
	String[] GENRE;
	String[] WRITER;
	String[] ARTIST;
	String[] PDATE;
	Integer[] THUMBNAILS;
	String[] EPISODE;*/
	Comic[] comics;
	ImageLoader imageLoader;
	private Activity activity;
	//	ImageLoader imageLoader;
	int resId;
	private static LayoutInflater inflater=null;
	public static CustomDialog pdialog;

	//public ComicListAdapter(Context context, int textViewResourceId, int[] ID, String[] TITLES, String[] GENRE, String[] WRITER, String[] ARTIST, String[] PDATE, Integer[] THUMBNAILS, String[] EPISODE) {
	public ComicListAdapter(Context context, int textViewResourceId, Comic[] comics) {
		super();
		/*this.ID=ID;
		this.TITLES=TITLES;
		this.GENRE=GENRE;
		this.WRITER=WRITER;
		this.ARTIST=ARTIST;
		this.PDATE=PDATE;
		this.THUMBNAILS=THUMBNAILS;
		this.EPISODE=EPISODE;*/
		this.comics=comics;

		this.activity=(Activity) context;
		this.resId=textViewResourceId;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//	    imageLoader=new ImageLoader(activity.getApplicationContext());
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
			vi = inflater.inflate(R.layout.list_comic, null);
			holder=new ViewHolder();

			holder.title = (TextView) vi.findViewById(R.id.title);
			holder.genre = (TextView) vi.findViewById(R.id.genre);
			holder.writer = (TextView) vi.findViewById(R.id.writer);
			holder.artist = (TextView) vi.findViewById(R.id.artist);
			holder.pdate = (TextView) vi.findViewById(R.id.pdate);
			holder.img = (ImageView) vi.findViewById(R.id.img);
			holder.episode = (TextView) vi.findViewById(R.id.episode);
			holder.episodetext = (TextView) vi.findViewById(R.id.episodetext);
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

		/*holder.title.setText(TITLES[position]);
		holder.title.setTypeface(tf_bold);
		holder.genre.setText("GENRE : " + GENRE[position]);
		holder.genre.setTypeface(tf);
		holder.writer.setText("WRITER : " + WRITER[position]);	
		holder.writer.setTypeface(tf);
		holder.artist.setText("ARTIST : " + ARTIST[position]);
		holder.artist.setTypeface(tf);
		holder.pdate.setText("PUBLISHED DATE : " + PDATE[position]);
		holder.pdate.setTypeface(tf);
		holder.episode.setText(EPISODE[position]);
		holder.episode.setTypeface(tf);
		holder.episodetext.setTypeface(tf);
		holder.img.setImageResource(THUMBNAILS[position]);*/

		holder.title.setText(comics[position].getTitle());
		holder.title.setTypeface(tf_bold);
		holder.genre.setText("GENRE : " + comics[position].getGenre());
		holder.genre.setTypeface(tf);
		//Log.e("writter null :", comics[position].getWritter());
		holder.writer.setText("WRITER : " + comics[position].getWritter());
		holder.writer.setTypeface(tf);
		holder.artist.setText("ARTIST : " + comics[position].getArtist());
		holder.artist.setTypeface(tf);
		holder.pdate.setText("PUBLISHED DATE : " + comics[position].getDate());
		holder.pdate.setTypeface(tf);
		holder.episode.setText(comics[position].getCount());
		//holder.img.setImageResource(THUMBNAILS[position]);

		holder.img.setTag(comics[position].getImage());
		imageLoader.DisplayImage(comics[position].getImage(), activity, holder.img);
		
		pdialog = new CustomDialog(activity);
		pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		final String id = comics[position].getId();

		//        final CinemaLocation cinemaLocation=arrList.get(position);
		//        holder.txtName.setText(cinemaLocation.getName());
		//        holder.txtAddr.setText(cinemaLocation.getAddress());


		/* final int idx_comic;
        idx_comic = position;*/

		vi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(activity,MakkoDetailActivity.class);
				intent.putExtra("id", id);
//				intent.setFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				pdialog.show();
				oComic.idComic = id;
				activity.startActivity(intent);
			}
		});

		notifyDataSetChanged();

		vi.setClickable(true);
		vi.setFocusable(true);
		//vi.setBackgroundResource(android.R.drawable.menuitem_background);


		//        holder.img.setTag(cinemaLocation.getThumb());
		//        imageLoader.DisplayImage(cinemaLocation.getThumb(), activity, holder.img);

		return vi;
	}
	static class ViewHolder{
		TextView title;
		TextView genre;
		TextView writer;
		TextView artist;
		TextView pdate;
		TextView episode;
		TextView episodetext;
		//		LinearLayout ll;
		ImageView img;
		//		ImageButton imgArr;
	}
}
