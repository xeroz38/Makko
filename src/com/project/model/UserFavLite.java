package com.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserFavLite {

	private String username;
	static SQLiteDatabase db;
	static Cursor favCursor;
	public static final String query_user_fav ="SELECT * FROM user_fav";

	// insert user_eps
	public static void insertUserFav(Context context,  String username, String id) {
		// insert
		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();

		ContentValues cv = new ContentValues();

		cv.put(dbAdapter.username, username);
		cv.put(dbAdapter.idComic, id);
		dbAdapter.insert("user_fav", cv);

		dbAdapter.close();
	}
	//delete episode
		public static void deleteUserFav(Context context, String id){
			//delete comic
			TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
			dbAdapter.open();
			dbAdapter.delete("user_fav", "idComic=?", new String[]{id});
			dbAdapter.close();
		}

	//check favorite from sql
	public static boolean checkFav(String query, Context context){
		boolean favCheck = false;
		//EpisodeLite[] epsCollection= new EpisodeLite[0];// = new Comic[];
		TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(context);
		db= dbHelper.getReadableDatabase();
		//exeQuery
		favCursor = db.rawQuery(query, null);
		favCursor.moveToFirst();

		if((favCursor.getCount())>0){
			favCheck=true;
			Log.e("saa",""+favCursor.getCount());
		}
		favCursor.close();
		db.close();
		dbHelper.close();
		return favCheck;
	}
}
