package com.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TodoDbAdapter {

	// Database fields
	//comic fields
	public static final String idComic = "idComic";
	public static final String judulComic = "judulComic";
	public static final String genre = "genre";
	public static final String writter = "writter";
	public static final String artist = "artist";
	public static final String publishedDate = "publishedDate";
	public static final String imageComic = "imageComic";
	public static final String iconComic = "iconComic";
	//episode fields
	public static final String idEpisode = "idEpisode";
	public static final String judulEpisode = "judulEpisode";
	public static final String release = "release";
	//favorite
	//public static final String idFavorite = "idFavorite";
	//user
	public static final String username = "username";

	//private static final String DATABASE_TABLE = "todo";


	private Context context;
	private SQLiteDatabase database;
	private TodoDatabaseHelper dbHelper;

	public TodoDbAdapter(Context context) {
		this.context = context;
	}

	public TodoDbAdapter open() throws SQLException {
		dbHelper = new TodoDatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}
	public TodoDbAdapter open2() throws SQLException {
		dbHelper = new TodoDatabaseHelper(context);
		database = dbHelper.getReadableDatabase();
	
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public Cursor query(String query){
		return database.rawQuery(query, null);
	}
	public void execSQL(String query){
		database.execSQL(query);
	}

	public void delete(String table,String where,String[] args){
		database.delete(table, where, args);
	}

	public void insert(String table,ContentValues values){
		database.insert(table, null, values);
	}
}

