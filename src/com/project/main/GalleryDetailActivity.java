package com.project.main;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.project.control.Utils;
import com.project.view.ComicCollectionTab;
import com.project.view.ComicFavoriteTab;
import com.project.view.R;

public class GalleryDetailActivity extends TabActivity {

	public static TabHost gTabHost;
	public static int setTab = 1;
	private ImageView iv;

	private void setupTabHost() {
		gTabHost = (TabHost) findViewById(android.R.id.tabhost);
		gTabHost.setup();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_detail);

		setupTabHost();
		gTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		setupTab(new TextView(this), "", ComicCollectionTab.class, 0);
		setupTab(new TextView(this), "MY COMIC COLLECTION", ComicCollectionTab.class, 8);
		setupTab(new TextView(this), "MY FAVORITE COMICS", ComicFavoriteTab.class, 8);

		gTabHost.setCurrentTab(setTab);
		gTabHost.getTabWidget().getChildTabViewAt(0).setClickable(false);
	}

	private void setupTab(final View view, final String tag, Class<?> cls, int vis) {
		View tabview = createTabView(gTabHost.getContext(), tag);

		iv.setVisibility(vis);

		Intent intent;
		intent = new Intent().setClass(this, cls);

		TabSpec setContent = gTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		gTabHost.addTab(setContent);
	}

	private View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);

		iv = (ImageView) view.findViewById(R.id.makkologo);

		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		Typeface tf_bold = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Bold.otf");
		
		tv.setTextSize(11);
		tv.setTypeface(tf_bold);
		tv.setText(text);
		return view;
	}
}