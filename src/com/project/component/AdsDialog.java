package com.project.component;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.project.model.Ads;
import com.project.view.R;

public class AdsDialog extends Dialog implements OnClickListener {
	
	private WebView ads;
	private ImageView close;
	private Intent intent;

	private Double val;

	
	public AdsDialog(Context context) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.ads_dialog);
		
		ads = (WebView) findViewById(R.id.iklan_web);

//		bg_ads.setOnClickListener(this);
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View vw = inflater.inflate(R.layout.ads_dialog, null);
//		ads = (WebView) findViewById(R.id.iklan_web);
		final Ads[] db_ads = Ads.getAds(Ads.url_pop_ads);

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
		ads.setInitialScale(getScale(context));
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
					getContext().startActivity(intent);
					if(db_ads.length>0){
						ads.loadUrl(db_ads[0].getBanner());
					}
					dismiss();
				}
			}
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				//Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
				super.onReceivedError(view, errorCode, description, failingUrl);
//				ads.setVisibility(View.GONE);
			}
		});
		
		close = (ImageView) findViewById(R.id.close);
		close.setOnClickListener(this);
	}
	
	public int getScale(Context c){
	    Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	    int width = display.getWidth(); 
	    int height = display.getHeight();
	    int rotation = display.getRotation();
	    
		if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180){
			val = new Double(width)/new Double(600);
			Log.e(""+width, "1");
		} else {
			val = new Double(height)/new Double(600); //780 because there is white spot
			Log.e(""+height, "2");
		}
		val = val * 100d;

	    return val.intValue();
	}

	@Override
	public void onClick(View v) {		
		if (v == close){
			dismiss();
		} else if (v == ads){
			dismiss();
			intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_BROWSABLE);
			intent.setData(Uri.parse("http://makko.co"));
			getContext().startActivity(intent);
		}
	}

}
