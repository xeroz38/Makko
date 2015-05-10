package com.project.main;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.project.adapter.CollectionListAdapter;
import com.project.adapter.DetailEpisodeListAdapter;
import com.project.adapter.EpisodeListAdapter;
import com.project.component.AdsDialog;
import com.project.component.ComicDetailMenu;
import com.project.component.ComicDetailMenu.OnMenuItemSelectedListener;
import com.project.component.ComicDetailMenuItem;
import com.project.component.CustomDialog;
import com.project.component.Decompress;
import com.project.component.zoom.HorizontalPager;
import com.project.component.zoom.MangaPage;
import com.project.control.NetRequest;
import com.project.control.NetRequest.OnSuccessListener;
import com.project.control.Utils;
import com.project.model.Ads;
import com.project.model.Comic;
import com.project.model.ComicLite;
import com.project.model.Episode;
import com.project.model.UserFavLite;
import com.project.model.oComic;
import com.project.view.MakkoComicsTab;
import com.project.view.R;

public class GalleryActivity extends Activity implements OnMenuItemSelectedListener {

	//	ComicGallery gallery;
	public static CustomDialog pdialog;

	private File[] imagelist;
	private File makkoFolder;
	private ComicDetailMenu mMenu;
	private AdsDialog adsdialog;
	private SharedPreferences sharedPreferences;
	private HorizontalPager realViewSwitcher;
	private NetRequest nr;

	boolean checkFav;
	private String zipFile = Environment.getExternalStorageDirectory()
			+ "/.Makko/" + oComic.idComic + "/" + oComic.idEpisode + ".zip";
	private String unzipLocation = Environment.getExternalStorageDirectory()
			+ "/.Makko/" + oComic.idComic + "/" + oComic.idEpisode + "/";

	Vector<MangaPage> mangaPages;
	private final int preparedPage = 2;
	private int currentIndex;
	private String fb_share ="N/A";
	private String tw_share ="N/A";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);

		String idEpisode = oComic.idEpisode;

		//get link share for twitter n facebook
		Episode[] episode = Episode.getEpisode(Episode.url_share+idEpisode);
		Log.e("episode.length", ""+idEpisode+"::"+episode.length);
		if(episode.length>0){
			fb_share = episode[0].getFb_share();
			tw_share = episode[0].getTw_share();
		}

		if(DetailEpisodeListAdapter.pdialog != null) DetailEpisodeListAdapter.pdialog.dismiss();
		if(EpisodeListAdapter.pdialog != null) EpisodeListAdapter.pdialog.dismiss();
		if(CollectionListAdapter.pdialog != null) CollectionListAdapter.pdialog.dismiss();

		checkFav = UserFavLite.checkFav(UserFavLite.query_user_fav+" WHERE username = '" + oComic.username + "' AND idComic='"+oComic.idComic+"'", getApplicationContext());

		Decompress d = new Decompress(zipFile, unzipLocation); 
		d.unzip(); 

		File makkoImages = Environment.getExternalStorageDirectory();  
		makkoFolder = new File(makkoImages + "/.Makko" + "/" + oComic.idComic + "/" + oComic.idEpisode + "/");
		imagelist = makkoFolder.listFiles(new FilenameFilter(){ 
			@Override  
			public boolean accept(File dir, String name) {  
				return ((name.endsWith(".jpg"))||(name.endsWith(".png")));  
			}
		});

		Arrays.sort(imagelist, new Comparator<File>() {
			public int compare(File f1, File f2){
				return f1.getName().compareTo(f2.getName());
			}
		});



		int totalImage = imagelist.length;

		realViewSwitcher = (HorizontalPager) findViewById(R.id.horizontalPager);
		realViewSwitcher.setOnScreenSwitchListener(new MyOnScreenSwitchListener());

		mangaPages = new Vector<MangaPage>();
		for (int i = 0; i < totalImage; i++)
		{
			MangaPage mp = new MangaPage(this, imagelist[i].getAbsolutePath());
			realViewSwitcher.addView(mp.imageView);
			mangaPages.add(mp);
		}

		sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);


		adsdialog = new AdsDialog(GalleryActivity.this);
		adsdialog.setCancelable(false);

		final TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (!GalleryActivity.this.isFinishing()){
							adsdialog.show();
						}
					}
				};
				runOnUiThread(runnable);
			}
		};
		final Timer timer = new Timer();
		
		nr = new NetRequest(GalleryActivity.this);
		nr.execute(Ads.url_pop_ads);
		
		nr.setOnSuccessListener(new OnSuccessListener() {
			
			@Override
			public void doSuccess(String result) {
				// TODO Auto-generated method stub
				timer.schedule(task, 5000);
				Log.e("SDASDSA", "GAGAL");

			}
			
			@Override
			public void doError() {
				// TODO Auto-generated method stub
				Log.e("SDASDSA", "SUKSES");
			}
		});



		pdialog = new CustomDialog(this);
		pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		mMenu = new ComicDetailMenu(this, this, getLayoutInflater());
		mMenu.setHideOnSelect(true);
		mMenu.setItemsPerLineInPortraitOrientation(5);
		mMenu.setItemsPerLineInLandscapeOrientation(5);
		loadMenuItems();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int EpPage = sharedPreferences.getInt(oComic.idEpisode, 0);
		Log.e("load", ""+EpPage);
		//        loadAndPreparePage(0);
		Log.i("Makko", "child count : " + realViewSwitcher.getChildCount());

		jumpToPage(EpPage);
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			doMenu();
			return true; 
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
		{
			if (mMenu.isShowing()) {
				mMenu.hide();
				return true;
			} else {
				DeleteRecursive(makkoFolder);
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event); 
	} 

	void DeleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);

		fileOrDirectory.delete();
	}

	private void loadMenuItems() {
		ArrayList<ComicDetailMenuItem> menuItems = new ArrayList<ComicDetailMenuItem>();
		ComicDetailMenuItem cmi = new ComicDetailMenuItem();
		cmi.setCaption("SELECT\nPAGE");
		cmi.setImageResourceId(R.drawable.makko_menu_1);
		cmi.setId(1);
		menuItems.add(cmi);
		cmi = new ComicDetailMenuItem();
		cmi.setCaption("SELECT\nEPISODE");
		cmi.setImageResourceId(R.drawable.makko_menu_2);
		cmi.setId(2);
		menuItems.add(cmi);
		cmi = new ComicDetailMenuItem();
		cmi.setCaption("ADD TO MY\nFAVORITE");
		cmi.setImageResourceId(R.drawable.makko_menu_3);
		cmi.setId(3);
		menuItems.add(cmi);
		cmi = new ComicDetailMenuItem();
		cmi.setCaption("SHARE\nFACEBOOK");
		cmi.setImageResourceId(R.drawable.makko_menu_4);
		cmi.setId(4);
		menuItems.add(cmi);		
		cmi = new ComicDetailMenuItem();
		cmi.setCaption("SHARE\nTWITTER");
		cmi.setImageResourceId(R.drawable.makko_menu_5);
		cmi.setId(5);
		menuItems.add(cmi);
		if (!mMenu.isShowing())
			try {
				mMenu.setMenuItems(menuItems);
			} catch (Exception e) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Egads!");
				alert.setMessage(e.getMessage());
				alert.show();
			}
	}

	private void doMenu() {
		if (mMenu.isShowing()) {
			mMenu.hide();
		} else {
			mMenu.show(findViewById(R.id.LinearLayout01));
		}
	}

	@Override
	public void MenuItemSelectedEvent(ComicDetailMenuItem selection) {
		int sel = selection.getId();

		switch (sel)
		{
		case 1: {
			Intent intent=new Intent(this,MakkoPageActivity.class);
			pdialog.show();
			startActivity(intent);
			break;
		}

		case 2: {
			Intent intent=new Intent(this,MakkoEpisodeActivity.class);
			pdialog.show();
			startActivity(intent);
			break;
		}
		case 3: {
			if (!checkFav)
			{
				try {
					UserFavLite.insertUserFav(getApplicationContext(), oComic.username, oComic.idComic);
					if(!ComicLite.availComic(getApplicationContext(), oComic.idComic)){
						ComicLite.insertComic(getApplicationContext(), oComic.idComic);
					}
					MakkoDetailActivity mk = new MakkoDetailActivity();
					mk.downloadImage(oComic.idComic, getApplicationContext());
					Toast.makeText(getApplicationContext(), "Added To Favorite", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else{
				Toast.makeText(getApplicationContext(), "My Favorite Comic", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case 4: {
			if(!fb_share.equals("N/A")){
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW).addCategory(Intent.CATEGORY_BROWSABLE);
				//intent.setData(Uri.parse("http://www.facebook.com/share.php?u=http://makko.co/read/Wanara/22"));
				intent.setData(Uri.parse(fb_share));
				//Toast.makeText(getApplicationContext(), fb_share, Toast.LENGTH_SHORT).show();
				startActivity(intent);
				//Toast.makeText(getApplicationContext(), fb_share, Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "Link Share Facebook Not Available", Toast.LENGTH_SHORT).show();
			}
		}
		break;
		case 5: {
			if(!tw_share.equals("N/A")){
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW).addCategory(Intent.CATEGORY_BROWSABLE);
				//				intent.setData(Uri.parse("https://twitter.com/intent/tweet?original_referer=http://makko.co/read/Wanara/22&source=tweetbutton&text=Makko - Online Comic Publisher - Wanara&url=http://makko.co/read/Wanara/22&via=makko"));
				intent.setData(Uri.parse(tw_share));
				startActivity(intent);
			}
			else{
				Toast.makeText(getApplicationContext(), "Link Share Twitter Not Available", Toast.LENGTH_SHORT).show();
			}
		}
		break;
		default: break;
		}
	}


	private void jumpToPage(int page)
	{
		unloadAllPages();
		loadAndPreparePage(page);
		//        realViewSwitcher.snapToScreen(page, 10);
		realViewSwitcher.setCurrentScreen(page, false);
		// check all buffered bitmap state
		//        for (int i = 0; i < mangaPages.size(); i++)
		//        {
		//            MangaPage mp = mangaPages.get(i);
		//            System.out.println("page index " + i + " : " + mp.getBitmapSize());
		//        }
	}

	private void loadAndPreparePage(int page)
	{   // unload previous page outside buffer num
		if (page - preparedPage - 1 >= 0)
		{
			System.out.println("unload ------ : " + (page - preparedPage - 1));
			mangaPages.get(page - preparedPage - 1).unloadImage();
		}
		// preload previous page
		for (int i = Math.max(0, page - preparedPage); i < page; i++)
		{
			System.out.println("preload : " + i);
			mangaPages.get(i).loadImage();
		}

		// loading current page
		System.out.println("loading page : " + page);
		mangaPages.get(page).loadImage();

		// preload next page
		for (int i = page + 1; i <= Math.min(mangaPages.size() - 1, page + preparedPage); i++)
		{
			System.out.println("preload : " + i);
			mangaPages.get(i).loadImage();
		}
		// unload next page outside buffer num
		if (page + preparedPage + 1 < mangaPages.size())
		{
			System.out.println("unload ------ : " + (page + preparedPage + 1));
			mangaPages.get(page + preparedPage + 1).unloadImage();
		}
	}

	private void unloadAllPages()
	{
		for (MangaPage mp : mangaPages)
		{
			mp.unloadImage();
		}
	}

	private class MyOnScreenSwitchListener implements HorizontalPager.OnScreenSwitchListener
	{

		public void onScreenSwitched(int index)
		{
			if (currentIndex != index)
			{
				currentIndex = index;
				Utils.SavePreferences(oComic.idEpisode, index, GalleryActivity.this);
				Log.e("save", ""+index);
				loadAndPreparePage(index);
				Log.i("Activity", "switched to screen: " + index);
			}
		}

	}
}