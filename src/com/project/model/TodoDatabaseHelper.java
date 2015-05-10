package com.project.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoDatabaseHelper extends SQLiteOpenHelper {
	//private static final String DATABASE_NAME = FinalVar.MY_DATABASE_NAME;
	private static final String DATABASE_NAME = "/data/data/com.project.view/databases/makko";

	private static final int DATABASE_VERSION = 3;

	// Database creation sql statement
	/*	private static final String DATABASE_CREATE1 = "CREATE TABLE IF NOT EXISTS "
                    + FinalVar.MY_DATABASE_TABLE1
                    +"(id INTEGER PRIMARY KEY AUTOINCREMENT,qrcodebinary TEXT,bookingid TEXT,totalticket TEXT,cinemaname TEXT," +
                    		"hallname TEXT,movietitle TEXT,datentime TEXT,seatselected TEXT,bookingfee TEXT,totalprice TEXT,transactiondate TEXT,moviethumb TEXT,rating TEXT);";
	private static final String DATABASE_CREATE2 ="CREATE TABLE IF NOT EXISTS "
                    + FinalVar.MY_DATABASE_TABLE2
                    + " (id INTEGER PRIMARY KEY AUTOINCREMENT, idheader INTEGER,"
                    + " type TEXT,numberofseat TEXT,price TEXT,surcharge TEXT);";

	 */

	private static final String DATABASE_CREATE1 = "CREATE TABLE IF NOT EXISTS comic (idComic INTEGER PRIMARY KEY  NOT NULL , judulComic TEXT, genre TEXT, writter TEXT, artist TEXT, publishedDate DATETIME, imageComic TEXT, iconComic TEXT);";
	private static final String DATABASE_CREATE2 = "CREATE TABLE if not exists episode (idEpisode INTEGER PRIMARY KEY  NOT NULL , judulEpisode TEXT, release DATETIME, idComic INTEGER);";
	//private static final String DATABASE_CREATE3 = "CREATE TABLE if not exists favorite (idFavorite INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , idComic INTEGER NOT NULL );";
	private static final String DATABASE_CREATE3 = "CREATE TABLE if not exists user (username VARCHAR PRIMARY KEY  NOT NULL );";
	private static final String DATABASE_CREATE4 = "CREATE TABLE if not exists user_eps (username VARCHAR NOT NULL , idEpisode INTEGER NOT NULL , PRIMARY KEY (username, idEpisode));";
	private static final String DATABASE_CREATE5 = "CREATE TABLE if not exists user_fav (username VARCHAR NOT NULL , idComic INTEGER NOT NULL , PRIMARY KEY (username, idComic));";



	public TodoDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE1);
		database.execSQL(DATABASE_CREATE2);
		database.execSQL(DATABASE_CREATE3);
		database.execSQL(DATABASE_CREATE4);
		database.execSQL(DATABASE_CREATE5);
	}

	// Method is called during an upgrade of the database, e.g. if you increase
	// the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(TodoDatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS comic");
		database.execSQL("DROP TABLE IF EXISTS episode");
		database.execSQL("DROP TABLE IF EXISTS user");
		database.execSQL("DROP TABLE IF EXISTS user_eps");
		database.execSQL("DROP TABLE IF EXISTS user_fav");
		//database.execSQL("DROP TABLE IF EXISTS favorite");

		onCreate(database);
	}
}

