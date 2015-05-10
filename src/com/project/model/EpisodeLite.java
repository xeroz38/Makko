package com.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class EpisodeLite {


	//public static final String query_title_comic ="SELECT judulComic FROM comic";

	public static final String query_check_episode ="SELECT idEpisode FROM episode WHERE idEpisode=";

	String id;
	String judul;
	String release;
	String idComic;

	static SQLiteDatabase db;

	static Cursor epsCursor;

	//check episode from sql
	/*	public static boolean checkEps(String query, Context context){
		Log.e("query sql user eps : ", query);
		boolean epsCheck = false;
		//EpisodeLite[] epsCollection= new EpisodeLite[0];// = new Comic[];
		TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(context);
		db= dbHelper.getReadableDatabase();
		//exeQuery
		epsCursor = db.rawQuery(query, null);
		epsCursor.moveToFirst();

		if((epsCursor.getCount())>0){
			epsCheck=true;
		}
		db.close();
		dbHelper.close();
		return epsCheck;
	}*/

	//insert comic into sqlite
	public static void insertEpisode(Context context, String id){
		//Comic[] comic = Comic.getAllComic(Comic.url_detail_comic+id);
		Episode[] eps = Episode.getEpisode(Episode.url_detail_episode+id);

		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();

		ContentValues cv1=new ContentValues();
		cv1.put(dbAdapter.idEpisode, eps[0].getId());
		Log.e("id:", eps[0].getId());
		cv1.put(dbAdapter.judulEpisode, eps[0].getJudul());
		Log.e("judul:", eps[0].getJudul());
		cv1.put(dbAdapter.release, eps[0].getRelease());
		Log.e("release:", eps[0].getRelease());
		cv1.put(dbAdapter.idComic, eps[0].getIdComic());
		Log.e("idcom:", eps[0].getIdComic());
		dbAdapter.insert("episode", cv1);
		dbAdapter.close();
	}
	
	//delete episode
	public static void deleteEpisodeComic(Context context, String id){
		//delete comic
		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();
		dbAdapter.delete("episode", "idComic=?", new String[]{id});
		dbAdapter.close();
	}

	//delete episode by id comic
	public static void deleteEpisode(Context context, String id){
		//delete comic
		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();
		dbAdapter.delete("episode", "idEpisode=?", new String[]{id});
		dbAdapter.close();
	}
	//avail episode
	public static boolean availEps(Context context, String idEps){
		boolean stat = false;
		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();
		Cursor db = dbAdapter.query("SELECT idEpisode FROM episode where idEpisode="+idEps);
		Log.e("cursor: "+idEps, ""+db.getCount());
		if(db.getCount()!=0){
			stat= true;
		}
		dbAdapter.close();
		return stat;
	}
	
	//avail episode
		public static boolean availEpsComic(Context context, String idComic){
			boolean stat = false;
			TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
			dbAdapter.open();
			Cursor db = dbAdapter.query("SELECT idEpisode FROM episode where idComic="+idComic);
			if(db.getCount()!=0){
				stat= true;
			}
			dbAdapter.close();
			return stat;
		}

	public String getId(){

		return id;
	}

	public void setId(String id){
		this.id=id;
	}

	public String getIdComic(){

		return idComic;
	}

	public void setIdComic(String idComic){
		this.idComic=idComic;
	}

	public String getJudul(){

		return judul;
	}

	public void setJudul(String judul){
		this.judul=judul;
	}


	public String getRelease(){
		return release;
	}

	public void setRelease(String release){
		this.release = release;
	}

}
