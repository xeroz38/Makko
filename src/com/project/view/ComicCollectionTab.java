package com.project.view;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import com.project.adapter.CollectionListAdapter;
import com.project.control.Utils;
import com.project.model.TodoDatabaseHelper;
import com.project.model.UserEpsLite;
import com.project.model.oComic;

public class ComicCollectionTab extends ExpandableListActivity {

	DisplayMetrics metrics;
	int width;
	ExpandableListView expList;
	CollectionListAdapter cla;

	SQLiteDatabase db;
	Cursor judCursor=null;
	Cursor judEpCursor=null;
	String idComUser="";

	ArrayList list_judul = new ArrayList();
	ArrayList list_judulEp = new ArrayList();

	ArrayList arrChildelements;

	boolean hasResume = false;

	@Override
	public void onResume(){
		super.onResume();

		if (!hasResume){
			hasResume = true;
		} else {
			getDataExpList();
			cla.notifyDataSetChanged();
			expList.invalidate();
		}
	}
	
//	@Override
//    protected void onRestoreInstanceState(Bundle state) {
//		setContentView(R.layout.list_collection);
//        super.onRestoreInstanceState(state);
//    }


	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_collection);

		
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int rotation = display.getRotation();

//		if (rotation == 1 || rotation == 3){
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		} else {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}
		
		Drawable myIcon = (Drawable) getResources().getDrawable(R.layout.group_indicator);
		
		expList = getExpandableListView();
//		expList = (ExpandableListView) findViewById(R.id.list);
		expList.setPadding(15, 0, 15, 0);
		expList.setChildDivider(new PaintDrawable(Color.DKGRAY));		
		expList.setDividerHeight(1);
		expList.setCacheColorHint(0x00000000);
		expList.setGroupIndicator(myIcon);
		expList.setBackgroundResource(R.layout.backrepeat);

		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;

		getDataExpList();
		// this code for adjusting the group indicator into right side of the
		// view
		expList.setIndicatorBounds(width - GetDipsFromPixel(80), width - GetDipsFromPixel(50));
		cla = new CollectionListAdapter(this, list_judul, list_judulEp);

		expList.setAdapter(cla);

//		expList.setOnGroupExpandListener(new OnGroupExpandListener() {
//			@Override
//			public void onGroupExpand(int groupPosition) {
//				Log.e("onGroupExpand", "OK");
//			}
//		});
//
//		expList.setOnGroupCollapseListener(new OnGroupCollapseListener() {
//			@Override
//			public void onGroupCollapse(int groupPosition) {
//				Log.e("onGroupCollapse", "OK");
//			}
//		});
//
//		expList.setOnChildClickListener(new OnChildClickListener() {
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v,
//					int groupPosition, int childPosition, long id) {
//				Log.e("OnChildClickListener", "OK");
//				return false;
//			}
//		});
	}

	public int GetDipsFromPixel(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
	}

	public void getDataExpList(){

		final String epsUser[] = UserEpsLite.getEpsUser(UserEpsLite.query_user_epsCom+" WHERE username = '" + oComic.username + "'", getApplicationContext());

		idComUser="";

		for(int a=0 ; a<epsUser.length ; a++){
			if(a==(epsUser.length-1)){
				idComUser = idComUser+epsUser[a].toString();
			}else{
				idComUser = idComUser+epsUser[a].toString()+",";
			}

		}
		Log.e("id com : ", idComUser);
		TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(this);
		db= dbHelper.getReadableDatabase();
		//db = dbAdapter.re
		String queryComic = "SELECT idComic, judulComic FROM comic WHERE idComic IN("+idComUser+")";
		judCursor = db.rawQuery(queryComic, null);
		judCursor.moveToFirst();

		list_judul.clear();
		list_judulEp.clear();

		if((judCursor.getCount())>0){
			for(int a=1 ; a<=judCursor.getCount() ; a++){
				list_judul.add(new ArrayList());
				list_judulEp.add(new ArrayList());
				//array comic
				((ArrayList) list_judul.get(a-1)).add(judCursor.getString(0));
				((ArrayList) list_judul.get(a-1)).add(judCursor.getString(1));
				//list_judul[0].add(judCursor.getString(0));
				//list_judul.add(judCursor.getString(0));
				//Log.e("judul", judCursor.getString(0));

				//
				//episode db
				String queryEpisode = "SELECT idEpisode, judulEpisode FROM episode WHERE idComic="+judCursor.getString(0);
				judEpCursor = db.rawQuery(queryEpisode, null);
				judEpCursor.moveToFirst();

				if((judEpCursor.getCount())>0){
					for(int b=1 ; b<=judEpCursor.getCount() ; b++){

						((ArrayList) list_judulEp.get(a-1)).add(new ArrayList());
						/*test[a-1][b-1][0]=judEpCursor.getString(0);
						test[a-1][b-1][1]=judEpCursor.getString(1);*/
						//array episode
						((ArrayList) ((ArrayList) list_judulEp.get(a-1)).get(b-1)).add(judEpCursor.getString(0));
						((ArrayList) ((ArrayList) list_judulEp.get(a-1)).get(b-1)).add(judEpCursor.getString(1));
						//list.get(a-1).get(b-1).add(judEpCursor.getString(1));
						//list.get(0).get(0).add(judEpCursor.getString(0));
						//list.get(a-1).get(b-1).add(judEpCursor.getString(1));
						//Log.e("judul", list_judulEp.get(b-1).toString());
						//listEpisode[a-1][b-1][0]=judEpCursor.getString(0);
						if(b!=judEpCursor.getCount()){
							judEpCursor.moveToNext();
						}
					}
				}
				if(a!=judCursor.getCount()){
					judCursor.moveToNext();
				}
				
				judEpCursor.close();
				
			}
		}
		
		judCursor.close();
		db.close();
	}
}