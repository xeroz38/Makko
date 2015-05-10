package com.project.main;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.project.control.Utils;
import com.project.model.Ads;
import com.project.model.Comic;
import com.project.model.ComicLite;
import com.project.view.ComicCollectionTab;
import com.project.view.ComicEpisodeTab;
import com.project.view.ComicFavoriteTab;
import com.project.view.MakkoComicsTab;
import com.project.view.R;

public class MakkoActivity extends TabActivity {

	public static TabHost mTabHost;
	public static NotificationManager notificationManager;

	private WebView ads;
	private Intent intent;
	private ImageView iv;
	private Button logout;

	private int favImage;
	private Double val;

	public Typeface tf_bold;

//	boolean hasResume = false;
//
//	@Override
//	public void onResume(){
//		super.onResume();
//
//		if (!hasResume){
//			hasResume = true;
//		} else {
//			mTabHost.setCurrentTab(0);
//			mTabHost.clearAllTabs();
//			getDataTab();
//		}
//	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		
		if(LoginActivity.pdialog != null){
			LoginActivity.pdialog.dismiss();
		}
		
		//get banner link
		final Ads[] db_ads = Ads.getAds(Ads.url_foot_ads);
//		Log.e("banner", ""+db_ads[0].getBanner());
		ads = (WebView) findViewById(R.id.iklan_web);
		//ads.loadUrl("http://android.makko.co/banner.php?id=2");
		if(db_ads.length>0){
			ads.loadUrl(db_ads[0].getBanner());
		} else {
			ads.loadUrl("http://android.makko.co/");
		}
		
		ads.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return (event.getAction() == MotionEvent.ACTION_MOVE);
			}
		});
		ads.setHorizontalScrollBarEnabled(false);
		ads.setVerticalScrollBarEnabled(false);
		ads.setInitialScale(getScale(this));
		ads.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				if(url.indexOf("http://android.makko.co/")<0){
					Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
					startActivity(intent);
					if(db_ads.length>0){
						ads.loadUrl(db_ads[0].getBanner());
					}
				}
			}
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				//Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
				super.onReceivedError(view, errorCode, description, failingUrl);
				ads.setVisibility(View.GONE);
			}
		});
//		ads2 = (LinearLayout) findViewById(R.id.iklan);
//		ads2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setAction(Intent.ACTION_VIEW);
//				intent.addCategory(Intent.CATEGORY_BROWSABLE);
//				intent.setData(Uri.parse("http://makko.co"));
//				startActivity(intent);
//			}
//		});
		
		logout = (Button) findViewById(R.id.logout);
		tf_bold = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Bold.otf");
		logout.setTypeface(tf_bold);
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.SavePreferences("login", 0, MakkoActivity.this);
				Intent intent = new Intent(MakkoActivity.this, LoginActivity.class);
				intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
		});

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		getDataTab();
	}
	
	private void getDataTab(){
		checkFav();
		setupTab(new TextView(this), "MY COMIC\nCOLLECTION", ComicCollectionTab.class, 8);
		setupTab(new TextView(this), "MAKKO\nCOMICS", MakkoComicsTab.class, 8);
//		setupTab(new TextView(this), "MY COMIC\nCOLLECTION", ComicCollectionTab.class, 8);
		setupTab(new TextView(this), "FRESH\nEPISODE", ComicEpisodeTab.class, 8);
//		setupTab(new TextView(this), "MY COMIC\nCOLLECTION", ComicCollectionTab.class, 8);
		setupTab(new TextView(this), "MY FAVORITE\nCOMICS", ComicFavoriteTab.class, favImage);
	}

	private void setupTab(final View view, final String tag, Class<?> cls, int vis) {
		View tabview = createTabView(mTabHost.getContext(), tag);

		iv.setVisibility(vis);

		intent = new Intent().setClass(this, cls);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		mTabHost.addTab(setContent);
	}

	private View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);

		iv = (ImageView) view.findViewById(R.id.notif);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tf_bold = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Bold.otf");
		tv.setTypeface(tf_bold);
		tv.setText(text);
		
		return view;
	}

	public static void setNotif(Activity act, String title, String desc){
		String ns = Context.NOTIFICATION_SERVICE;

		NotificationManager mNotificationManager = (NotificationManager) act.getSystemService(ns);

		int icon = R.drawable.statusnotif;
		CharSequence tickerText = "New Comic Available";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
		CharSequence contentTitle = title + " Episode";
		CharSequence contentText = "Successfully Download";

		Intent notificationIntent = new Intent(act, SplashScreen.class);
		PendingIntent contentIntent = PendingIntent.getActivity(act, 0, notificationIntent, 0);
		notification.setLatestEventInfo(act, contentTitle, contentText, contentIntent);
		mNotificationManager.notify(1, notification);
	}
	
	public static void setProgressNotif(Activity act, int progress, int queue){
//        Intent intent = new Intent(act, MakkoDetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(act, 0, null, 0);
	    Notification notification = new Notification(R.drawable.statusnotif, "Downloading Comic...", System.currentTimeMillis());
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notification.contentIntent = pendingIntent;
        notification.contentView = new RemoteViews(act.getPackageName(), R.layout.progress_notif);
        notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.statusnotif);
        notification.contentView.setTextViewText(R.id.status_text, "Downloading Comic...");
        notification.contentView.setProgressBar(R.id.status_progress, 100, progress, false);

        notificationManager = (NotificationManager) act.getSystemService(act.NOTIFICATION_SERVICE);
        notificationManager.notify(queue, notification);
	}
	
	private void checkFav(){
		favImage = 8;
		ComicLite[] dbComic = ComicLite.getComic(ComicLite.query_favorit_comic, getApplicationContext());
		
		if (dbComic.length > 0){
			String query = Comic.url_count_comic;
			
			for (int a = 0; a<dbComic.length; a++){
				query = query + "&idComic[]=" + dbComic[a].getId();
			}
				
			Comic[] comics = Comic.getAllComic(query);
			
			for(int a = 0; a<comics.length; a++){
				int dbCount = ComicLite.countEps(getApplicationContext(), comics[a].getId());
				int serverCount = Integer.parseInt(comics[a].getCount());
				
				if(dbCount < serverCount){
					favImage = 0;
				}
			}
		}
	}
	
	public int getScale(Context c){
	    Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	    int width = display.getWidth(); 
	    int height = display.getHeight();
	    int rotation = display.getRotation();
	    
		if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180){
			val = new Double(width)/new Double(800);
		} else {
			val = new Double(height)/new Double(780); //780 because there is white spot
		}
		val = val * 100d;

	    return val.intValue();
	}

//	private void insertFavorite() {
//		// insert
//		TodoDbAdapter dbAdapter = new TodoDbAdapter(this);
//		dbAdapter.open();
//		ContentValues cv4 = new ContentValues();
//		ContentValues cv3 = new ContentValues();
//		cv3.put(dbAdapter.idComic, "3");
//		dbAdapter.insert("favorite", cv3);
//		cv4.put(dbAdapter.idComic, "4");
//		dbAdapter.insert("favorite", cv4);
//		dbAdapter.close();
//	}
//
//	// insert comic
//	private void insertComic() {
//		// insert
//		TodoDbAdapter dbAdapter = new TodoDbAdapter(this);
//		dbAdapter.open();
//		ContentValues cv3 = new ContentValues();
//		ContentValues cv4 = new ContentValues();
//		cv3.put(dbAdapter.idComic, "3");
//		cv3.put(dbAdapter.judulComic, "5 Menit Sebelum Tayang");
//		cv3.put(dbAdapter.genre, "Remaja");
//		cv3.put(dbAdapter.writter, "Go King");
//		cv3.put(dbAdapter.artist, "Matto");
//		cv3.put(dbAdapter.publishedDate, "2011-05-05");
//		cv3.put(dbAdapter.imageComic, "");
//		cv3.put(dbAdapter.iconComic, "");
//		dbAdapter.insert("comic", cv3);
//
//		cv4.put(dbAdapter.idComic, "4");
//		cv4.put(dbAdapter.judulComic, "Wanara");
//		cv4.put(dbAdapter.genre, "Remaja");
//		cv4.put(dbAdapter.writter, "Sweta Kartika");
//		cv4.put(dbAdapter.artist, "Sweta Kartika");
//		cv4.put(dbAdapter.publishedDate, "2011-03-16");
//		cv4.put(dbAdapter.imageComic, "");
//		cv4.put(dbAdapter.iconComic, "");
//		dbAdapter.insert("comic", cv4);
//		dbAdapter.close();
//	}
//
//	// insert episode
//	private void insertEpisode() {
//		// insert
//		TodoDbAdapter dbAdapter = new TodoDbAdapter(this);
//		dbAdapter.open();
//
//		ContentValues cv1 = new ContentValues();
//		ContentValues cv2 = new ContentValues();
//		ContentValues cv3 = new ContentValues();
//		ContentValues cv4 = new ContentValues();
//
//		cv1.put(dbAdapter.idComic, "3");
//		cv1.put(dbAdapter.idEpisode, "5");
//		cv1.put(dbAdapter.judulEpisode, "1");
//		cv1.put(dbAdapter.release, "2011-11-14");
//		dbAdapter.insert("episode", cv1);
//
//		cv2.put(dbAdapter.idComic, "4");
//		cv2.put(dbAdapter.idEpisode, "6");
//		cv2.put(dbAdapter.judulEpisode, "1");
//		cv2.put(dbAdapter.release, "2011-11-14");
//		dbAdapter.insert("episode", cv2);
//
//		cv3.put(dbAdapter.idComic, "3");
//		cv3.put(dbAdapter.idEpisode, "7");
//		cv3.put(dbAdapter.judulEpisode, "2");
//		cv3.put(dbAdapter.release, "2011-11-16");
//		dbAdapter.insert("episode", cv3);
//
//		cv4.put(dbAdapter.idComic, "4");
//		cv4.put(dbAdapter.idEpisode, "8");
//		cv4.put(dbAdapter.judulEpisode, "2");
//		cv4.put(dbAdapter.release, "2011-11-16");
//		dbAdapter.insert("episode", cv4);
//		dbAdapter.close();
//	}
//
//	// insert user
//	private void insertUser() {
//		// insert
//		TodoDbAdapter dbAdapter = new TodoDbAdapter(this);
//		dbAdapter.open();
//
//		ContentValues cv1 = new ContentValues();
//
//		cv1.put(dbAdapter.username, oComic.username);
//		dbAdapter.insert("user", cv1);
//		dbAdapter.close();
//	}
//
//	// insert user_eps
//	private void insertUserEpisode() {
//		// insert
//		TodoDbAdapter dbAdapter = new TodoDbAdapter(this);
//		dbAdapter.open();
//
//		ContentValues cv1 = new ContentValues();
//		ContentValues cv2 = new ContentValues();
//		ContentValues cv3 = new ContentValues();
//		ContentValues cv4 = new ContentValues();
//		// ContentValues cv5=new ContentValues();
//
//		cv1.put(dbAdapter.username, oComic.username);
//		cv1.put(dbAdapter.idEpisode, "5");
//		dbAdapter.insert("user_eps", cv1);
//
//		cv2.put(dbAdapter.username, oComic.username);
//		cv2.put(dbAdapter.idEpisode, "6");
//		dbAdapter.insert("user_eps", cv2);
//
//		cv3.put(dbAdapter.username, oComic.username);
//		cv3.put(dbAdapter.idEpisode, "7");
//		dbAdapter.insert("user_eps", cv3);
//
//		cv4.put(dbAdapter.username, oComic.username);
//		cv4.put(dbAdapter.idEpisode, "8");
//		dbAdapter.insert("user_eps", cv4);
//
//		dbAdapter.close();
//	}
}