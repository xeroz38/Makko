package com.project.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.project.adapter.DetailEpisodeListAdapter;
import com.project.control.Utils;
import com.project.model.Episode;
import com.project.model.oComic;
import com.project.view.R;

public class MakkoEpisodeActivity extends Activity {

	private Button collection_button;
	private Button favorite_button;
	private TextView eptext;

	private ListView listview;
	private String id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.episode);

		
		if(GalleryActivity.pdialog != null){
			GalleryActivity.pdialog.dismiss();
		}
		
		id = oComic.idComic;
		Episode[] episodes = Episode.getEpisode(Episode.url_list_episode+id);
		DetailEpisodeListAdapter ela = new DetailEpisodeListAdapter(this, R.layout.list_episode, episodes);

		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(ela);

		Typeface tf_bold = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Bold.otf");

		eptext = (TextView) findViewById(R.id.episodetext);
		eptext.setTypeface(tf_bold);

		collection_button = (Button) findViewById(R.id.collection_button);
		collection_button.setTypeface(tf_bold);
		collection_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MakkoEpisodeActivity.this, GalleryDetailActivity.class);
				GalleryDetailActivity.setTab = 1;
				startActivity(intent);
			}
		});
		favorite_button = (Button) findViewById(R.id.favorite_button);
		favorite_button.setTypeface(tf_bold);
		favorite_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MakkoEpisodeActivity.this, GalleryDetailActivity.class);
				GalleryDetailActivity.setTab = 2;
				startActivity(intent);
			}
		});
	}
}