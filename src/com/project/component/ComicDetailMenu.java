package com.project.component;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.project.main.GalleryDetailActivity;
import com.project.view.R;

public class ComicDetailMenu {

	private ArrayList<ComicDetailMenuItem> mMenuItems;
	private OnMenuItemSelectedListener mListener = null;
	private Context mContext = null;
	private LayoutInflater mLayoutInflater = null;
	private PopupWindow mPopupWindow = null;
	private boolean mIsShowing = false;
	private boolean mHideOnSelect = true;
	private int mRows = 0;
	private int mItemsPerLineInPortraitOrientation = 3;
	private int mItemsPerLineInLandscapeOrientation = 6;

	public interface OnMenuItemSelectedListener {
		public void MenuItemSelectedEvent(ComicDetailMenuItem selection);
	}

	public boolean isShowing() { return mIsShowing; }

	public void setHideOnSelect(boolean doHideOnSelect) { mHideOnSelect = doHideOnSelect; } 

	public void setItemsPerLineInPortraitOrientation(int count) { mItemsPerLineInPortraitOrientation = count; }

	public void setItemsPerLineInLandscapeOrientation(int count) { mItemsPerLineInLandscapeOrientation = count; }

	public synchronized void setMenuItems(ArrayList<ComicDetailMenuItem> items) throws Exception {
		if (mIsShowing) {
			throw new Exception("Menu list may not be modified while menu is displayed.");
		}
		mMenuItems = items;
	}

	public ComicDetailMenu(Context context, OnMenuItemSelectedListener listener, LayoutInflater lo) {
		mListener = listener;
		mMenuItems = new ArrayList<ComicDetailMenuItem>();
		mContext = context;
		mLayoutInflater = lo;
	}

	public synchronized void show(View v) {
		mIsShowing = true;
		boolean isLandscape = false;
		int itemCount = mMenuItems.size();
		if (itemCount<1) return; //no menu items to show
		if (mPopupWindow != null) return; //already showing
		Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		if (display.getWidth() > display.getHeight()) isLandscape = true;
		View mView= mLayoutInflater.inflate(R.layout.custom_menu, null);
		mPopupWindow = new PopupWindow(mView,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT, false);
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mPopupWindow.setWidth(display.getWidth());
		mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

		Typeface tf_bold = Typeface.createFromAsset(mContext.getAssets(), "fonts/DINNextLTPro-Bold.otf");


		Button collection_button = (Button) mView.findViewById(R.id.collection_button);
		collection_button.setTypeface(tf_bold);
		collection_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GalleryDetailActivity.class);
				GalleryDetailActivity.setTab = 1;
				mContext.startActivity(intent);
			}
		});
		Button favorite_button = (Button) mView.findViewById(R.id.favorite_button);
		favorite_button.setTypeface(tf_bold);
		favorite_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GalleryDetailActivity.class);
				GalleryDetailActivity.setTab = 2;
				mContext.startActivity(intent);
			}
		});

		RelativeLayout rl = (RelativeLayout) mView.findViewById(R.id.body_menu);
		rl.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				hide();
			}
		});

		int divisor = mItemsPerLineInPortraitOrientation;
		if (isLandscape) divisor = mItemsPerLineInLandscapeOrientation;
		int remainder = 0;
		if (itemCount < divisor) {
			mRows = 1;
			remainder = itemCount;
		} else {
			mRows = (itemCount / divisor);
			remainder = itemCount % divisor;
			if (remainder != 0) mRows++;
		}
		TableLayout table = (TableLayout)mView.findViewById(R.id.custom_menu_table);
		table.removeAllViews();
		for (int i=0; i < mRows; i++) {
			TableRow row = null;
			TextView tv = null;
			ImageView iv = null;
			//create headers
			row = new TableRow(mContext);
			row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			for (int j=0; j< divisor; j++) {
				if (i*divisor+j >= itemCount) break;
				final ComicDetailMenuItem cmi = mMenuItems.get(i*divisor+j);
				View itemLayout = mLayoutInflater.inflate(R.layout.custom_menu_item, null);
				tv = (TextView)itemLayout.findViewById(R.id.custom_menu_item_caption);
				tv.setText(cmi.getCaption());
				iv = (ImageView)itemLayout.findViewById(R.id.custom_menu_item_icon);
				iv.setImageResource(cmi.getImageResourceId());
				itemLayout.setOnClickListener( new OnClickListener() {
					@Override
					public void onClick(View v) {
						mListener.MenuItemSelectedEvent(cmi);
						if (mHideOnSelect) hide();
					}
				});
				row.addView(itemLayout);
			}
			table.addView(row);
		}
	}

	public synchronized void hide() {
		mIsShowing = false;
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
		return;
	}
}
