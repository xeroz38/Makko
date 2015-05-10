package com.project.main;

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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.project.adapter.ComicListAdapter;
import com.project.control.Download;
import com.project.control.ImageLoader;
import com.project.control.Utils;
import com.project.model.Ads;
import com.project.model.Comic;
import com.project.model.ComicLite;
import com.project.model.UserFavLite;
import com.project.model.oComic;
import com.project.view.DetailEpisodeTab;
import com.project.view.DetailInfoTab;
import com.project.view.R;

public class MakkoDetailActivity extends TabActivity {

	private TabHost mTabHost;
	private Button collection_button;
	private Button favorite_button;
	private Button add_favorite;
	private ImageView bg_image;
	private ImageLoader imageLoader;
	private WebView ads;
	
	boolean checkFav;
	boolean checkCom;
	private String id;
	private Double val;


	private void setupTabHost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		
		
		if(ComicListAdapter.pdialog != null){
			ComicListAdapter.pdialog.dismiss();
		}

		checkFav = UserFavLite.checkFav(UserFavLite.query_user_fav+" WHERE username = '" + oComic.username + "' AND idComic='"+oComic.idComic+"'", getApplicationContext());
		checkCom = ComicLite.availComic(getApplicationContext(), oComic.idComic);

		Bundle bundle = getIntent().getExtras();
		if(bundle!= null){
			id = bundle.getString("id".toString());

		}
		imageLoader = new ImageLoader(getApplicationContext());

		bg_image = (ImageView)findViewById(R.id.bg);


		Comic[] comics = Comic.getAllComic(Comic.url_head_comic+id);
		
		if(comics.length > 0)
		{
			if(!comics[0].getHead().equals("N/A"))
			{
				bg_image.setTag(comics[0].getHead());
				imageLoader.DisplayImage(comics[0].getHead(), this, bg_image);
			} 
		} 
		

		Typeface tf_bold = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Bold.otf");

		collection_button = (Button) findViewById(R.id.collection_button);
		collection_button.setTypeface(tf_bold);
		collection_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MakkoActivity.mTabHost.setCurrentTab(2);
				finish();
			}
		});
		favorite_button = (Button) findViewById(R.id.favorite_button);
		favorite_button.setTypeface(tf_bold);
		favorite_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MakkoActivity.mTabHost.setCurrentTab(3);
				finish();
			}
		});
		add_favorite = (Button) findViewById(R.id.add_fav);
		add_favorite.setTypeface(tf_bold);
		add_favorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!checkFav)
				{
					UserFavLite.insertUserFav(getApplicationContext(), oComic.username, oComic.idComic);
					ComicLite.insertComic(getApplicationContext(), oComic.idComic);
					downloadImage(id, getApplicationContext());

					Toast.makeText(getApplicationContext(), "Added To Favorite", Toast.LENGTH_SHORT).show();
				} else{
					Toast.makeText(getApplicationContext(), "My Favorite Comic", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		final Ads[] db_ads = Ads.getAds(Ads.url_foot_ads);
		ads = (WebView) findViewById(R.id.iklan_web);
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


		setupTabHost();
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		setupTab(new TextView(this), "COMIC\r\nEPISODE", DetailEpisodeTab.class, id);
		setupTab(new TextView(this), "COMIC\r\nINFO", DetailInfoTab.class, id);
		//		setupTab(new TextView(this), "ADD TO MY FAVORITE\r\nCOMIC", DetailInfoTab.class, id);
	}

	private void setupTab(final View view, final String tag, Class<?> cls, String id) {
		View tabview = createTabView(mTabHost.getContext(), tag);

		Intent intent = null;

		intent = new Intent().setClass(this, cls);
		intent.putExtra("id", id);

		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		mTabHost.addTab(setContent);
	}

	private View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);

		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		Typeface tf_bold = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Bold.otf");
		tv.setTypeface(tf_bold);
		tv.setText(text);
		return view;
	}

	public void downloadImage(String id, Context context){
		Comic[] comicImage = Comic.getAllComic(Comic.url_favorite_comic + "&idComic[]=" + oComic.idComic);
		Log.e("link image", "" + comicImage[0].getImage());

		Download dl = new Download("0", context);
		dl.execute(id, "image", comicImage[0].getImage());
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
}
