package com.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserEpsLite {
	private String idEpisode;
	public static final String query_user_eps ="SELECT * FROM user_eps";
	public static final String query_user_epsCom ="SELECT distinct(episode.idComic) FROM user_eps join episode on user_eps.idEpisode=episode.idEpisode";

	private String username;
	static SQLiteDatabase db;

	static Cursor epsCursor;

	//inser user eps
	public static void insertUserEps(Context context,  String username, String id){
		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();
		dbAdapter.execSQL("INSERT OR REPLACE INTO user_eps ('username','idEpisode') VALUES ("+username+","+id+")");
		/*ContentValues cv1=new ContentValues();
		cv1.put(dbAdapter.username, oComic.username);
		cv1.put(dbAdapter.idEpisode, id);
		dbAdapter.insert("user_eps", cv1);*/
		dbAdapter.close();
	}
	
	//delete comic
		public static void deleteUserComic(Context context, String id){
			//delete comic
			String query = "delete from user_eps where idEpisode=1)";
//			
//			TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
//			dbAdapter.open();
//			dbAdapter.execSQL(query);
//			/*ContentValues cv1=new ContentValues();
//			cv1.put(dbAdapter.username, oComic.username);
//			cv1.put(dbAdapter.idEpisode, id);
//			dbAdapter.insert("user_eps", cv1);*/
//			dbAdapter.close();
			
			
//			TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(context);
//			db= dbHelper.getReadableDatabase();
//			//exeQuery
//			epsCursor = db.rawQuery(query, null);
			
//			epsCursor.close();
//			db.close();
//			dbHelper.close();
			
			String idEps[] = UserEpsLite.getEpsUser("select idEpisode from episode where idComic = "+id, context); 
			Log.e("id eps :::", ""+idEps[0]+"lenght:"+idEps.length);
			TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
			int comicLength = idEps.length;
			Log.e("lenght:", ""+comicLength);
			dbAdapter.open();
			//dbAdapter.delete("user_eps", "idEpisode=?", new String[]{"1"});
			if(comicLength>0){
				for(int a = 0 ; a<comicLength ; a++){
					dbAdapter.delete("user_eps", "idEpisode=?", new String[]{idEps[a]});


				}
				
			}
			//dbAdapter.delete("user_eps", "idEpisode=?", idEps);
			dbAdapter.close();
		}
	//check episode from sql
	public static boolean checkEps(String query, Context context){
		boolean epsCheck = false;
		//EpisodeLite[] epsCollection= new EpisodeLite[0];// = new Comic[];
		TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(context);
		db= dbHelper.getReadableDatabase();
		//exeQuery
		epsCursor = db.rawQuery(query, null);
		epsCursor.moveToFirst();
		Log.e("asdasd :", ""+epsCursor.getCount());
		if((epsCursor.getCount())>0){
			epsCheck=true;
		}
		epsCursor.close();
		db.close();
		dbHelper.close();
		return epsCheck;
	}

	//check get episode user from sql
	public static String[] getEpsUser(String query, Context context){
		String[] eps = new String[0];
		//EpisodeLite[] epsCollection= new EpisodeLite[0];// = new Comic[];
		TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(context);
		db= dbHelper.getReadableDatabase();
		//exeQuery
		epsCursor = db.rawQuery(query, null);
		epsCursor.moveToFirst();
		eps = new String[epsCursor.getCount()];
		//Log.e("user size: ", ""+epsCursor.getCount());
		if((epsCursor.getCount())>0){
			for(int a=0 ; a<epsCursor.getCount() ; a++){
				eps[a] = epsCursor.getString(0);
				//Log.e("eps: ", ""+epsCursor.getString(1));
				if(a!=epsCursor.getCount()){
					epsCursor.moveToNext();
				}
			}
		}
		epsCursor.close();
		db.close();
		dbHelper.close();
		return eps;
	}

	public static void insertUserEps(Context context, String id){
		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();

		ContentValues cv1=new ContentValues();
		cv1.put(dbAdapter.username, oComic.username);
		cv1.put(dbAdapter.idEpisode, id);
		dbAdapter.insert("user_eps", cv1);
		dbAdapter.close();
	}
	//delete user episode by id episode
	public static void deleteUserEpisode(Context context, String id){
		//delete comic
		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();
		dbAdapter.delete("user_eps", "idEpisode=?", new String[]{id});
		dbAdapter.close();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	String getIdEpisode() {
		return idEpisode;
	}

	void setIdEpisode(String idEpisode) {
		this.idEpisode = idEpisode;
	}

}
