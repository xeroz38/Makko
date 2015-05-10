package com.project.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.project.view.R;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ImageView iv = new ImageView(getContext());
		iv.setImageResource(R.drawable.loading);
		
		setContentView(iv);
		
//		WebView wv = new WebView(getContext());
//		
//		wv.loadUrl("file:///android_res/drawable/sonic.gif");
//		wv.setBackgroundColor(0x00000000);
//		
//		setContentView(wv);
	}
}