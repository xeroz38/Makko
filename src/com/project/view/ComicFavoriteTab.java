package com.project.view;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.project.adapter.FavoriteListAdapter;
import com.project.control.Utils;
import com.project.model.ComicLite;

public class ComicFavoriteTab extends ListActivity {

	ComicLite[] dbComic;
	
	FavoriteListAdapter fla;
	ListView lv;
	boolean hasResume = false;

	
	@Override
	public void onResume(){
		super.onResume();

		if (!hasResume){
			hasResume = true;
		} else {
			getDataList();
			fla = new FavoriteListAdapter(this, R.layout.list_favorite, dbComic);
			lv.setAdapter(fla);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);
		
		lv = getListView();
		lv.setCacheColorHint(0x00000000);
		lv.setBackgroundResource(R.layout.backrepeat);

		getDataList();
		
		fla = new FavoriteListAdapter(this, R.layout.list_favorite, dbComic);
		lv.setAdapter(fla);
	}

	public void getDataList(){
		dbComic = ComicLite.getComic(ComicLite.query_favorit_comic, getApplicationContext());
	}
}
