package com.project.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class ComicLite {

	public static final String query_favorit_comic = "SELECT * FROM comic WHERE idComic IN(SELECT DISTINCT idComic FROM user_fav WHERE username='" + oComic.username +"')";
	//public static final String query_title_comic ="SELECT judulComic FROM comic";
	//public static final String query_favorit_comic ="SELECT * FROM comic WHERE idComic=";
	//public static final String url_detail_comic ="http://192.168.2.114/makko/comic.php?request=comicDetails&id=";
	//public static final String url_head_comic ="http://192.168.2.114/makko/comic.php?request=headComic&id=";

	String idComic;
	String title;
	String genre;
	String writter;
	String artist;
	String date;
	//String info;
	String image;
	/*String count;
	String head;*/
	//String publishedDate;
	//String url;
	static SQLiteDatabase db;

	static Cursor comicCursor;
	public ComicLite(){

	}

	public static ComicLite[] getComic(String query, Context context){

		ComicLite[] comicCollection= new ComicLite[0];// = new Comic[];
		TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(context);
		db= dbHelper.getReadableDatabase();
		//exeQuery
		comicCursor = db.rawQuery(query, null);
		comicCursor.moveToFirst();

		//Log.e("favorite size : ", ""+comicCursor.getCount());
		comicCollection = new ComicLite[comicCursor.getCount()];
		if((comicCursor.getCount())>0){
			for(int a=1 ; a<=comicCursor.getCount() ; a++){
				ComicLite aComic = new ComicLite();

				aComic.setId(comicCursor.getString(0));
				aComic.setTitle(comicCursor.getString(1));
				aComic.setGenre(comicCursor.getString(2));
				aComic.setWritter(comicCursor.getString(3));
				aComic.setArtist(comicCursor.getString(4));
				aComic.setDate(comicCursor.getString(5));

				//Log.e("id : ", aComic.getId());
				comicCollection[a-1] = aComic;

				if(a!=comicCursor.getCount()){
					comicCursor.moveToNext();
				}
			}
		}
		//Log.e("id 0: ", com.getId());
		comicCursor.close();
		db.close();
		dbHelper.close();
		return comicCollection;
	}

	//insert comic into sqlite
	public static void insertComic(Context context, String id){
		Comic[] comic = Comic.getAllComic(Comic.url_detail_comic+id);

		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();

		ContentValues cv1=new ContentValues();
		cv1.put(dbAdapter.idComic, comic[0].getId());
		cv1.put(dbAdapter.judulComic, comic[0].getTitle());
		cv1.put(dbAdapter.genre, comic[0].getGenre());
		cv1.put(dbAdapter.writter, comic[0].getWritter());
		cv1.put(dbAdapter.artist, comic[0].getArtist());
		cv1.put(dbAdapter.publishedDate, comic[0].getDate());
		dbAdapter.insert("comic", cv1);
		dbAdapter.close();
	}
	
	//delete comic
	public static void deleteComic(Context context, String id){
		//delete comic
		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();
		dbAdapter.delete("comic", "idComic=?", new String[]{id});
		dbAdapter.close();
	}

	//avail comic
	public static boolean availComic(Context context, String idComic){
		boolean stat = false;
		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();
		Cursor db = dbAdapter.query("SELECT idComic FROM comic where idComic="+idComic);
		Log.e("cursor: "+idComic, ""+db.getCount());
		if(db.getCount()!=0){
			stat= true;
		}
		dbAdapter.close();
		return stat;
	}
	
	//count epsd
	public static int countEps(Context context, String idComic){
		int count = 0;
		TodoDbAdapter dbAdapter = new TodoDbAdapter(context);
		dbAdapter.open();
		Cursor db = dbAdapter.query("SELECT count(idComic) as count FROM episode where idComic=" + idComic);
		db.moveToFirst();
		if (db.getCount()!=0){
			count = Integer.parseInt(db.getString(0));
		}
		dbAdapter.close();
		return count;
	}

	public String getId(){

		return idComic;
	}

	public void setId(String id){
		this.idComic=id;

	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title=title;
	}

	public String getGenre(){
		return genre;
	}

	public void setGenre(String genre){
		this.genre=genre;
	}

	public String getWritter(){
		return writter;
	}

	public void setWritter(String writter){
		this.writter=writter;
	}

	public String getArtist(){
		return artist;
	}

	public void setArtist(String artist){
		this.artist=artist;
	}

	public String getDate(){
		return date;
	}

	public void setDate(String date){
		this.date=date;
	}
}
