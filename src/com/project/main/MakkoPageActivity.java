package com.project.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.project.adapter.PageAdapter;
import com.project.control.Utils;
import com.project.view.R;

public class MakkoPageActivity extends Activity {

	private Button collection_button;
	private Button favorite_button;
	private TextView pgtext;
	private GridView gridview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.page);
		
		
		if(GalleryActivity.pdialog != null){
			GalleryActivity.pdialog.dismiss();
		}

		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new PageAdapter(this));

		Typeface tf_bold = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Bold.otf");

		pgtext = (TextView) findViewById(R.id.pagetext);
		pgtext.setTypeface(tf_bold);

		collection_button = (Button) findViewById(R.id.collection_button);
		collection_button.setTypeface(tf_bold);
		collection_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MakkoPageActivity.this, GalleryDetailActivity.class);
				GalleryDetailActivity.setTab = 1;
				startActivity(intent);
			}
		});
		favorite_button = (Button) findViewById(R.id.favorite_button);
		favorite_button.setTypeface(tf_bold);
		favorite_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MakkoPageActivity.this, GalleryDetailActivity.class);
				GalleryDetailActivity.setTab = 2;
				startActivity(intent);
			}
		});
	}

}