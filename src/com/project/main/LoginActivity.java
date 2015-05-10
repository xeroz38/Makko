package com.project.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.*;

import com.project.adapter.ComicListAdapter;
import com.project.component.CustomDialog;
import com.project.control.DialogControl;
import com.project.control.NetRequest;
import com.project.control.NetRequest.OnSuccessListener;
import com.project.control.Utils;
import com.project.model.User;
import com.project.model.oComic;
import com.project.view.R;

public class LoginActivity extends Activity {

	private TextView nametxt;
	private EditText name;
	private TextView passtxt;
	private EditText pass;
	private TextView forgottxt;
	private Button loginbtn;

	private TextView create_account1;
	private TextView create_account2;
	private TextView alt_login;
	
	private Button fblogin;
	private Button twlogin;
	
	private WebView wv;

	public static CustomDialog pdialog;

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		
		
		wv = (WebView) findViewById(R.id.fb);
		
		nametxt = (TextView) findViewById(R.id.nametext);
		name = (EditText) findViewById(R.id.name);
		passtxt = (TextView) findViewById(R.id.passwordtext);
		pass = (EditText) findViewById(R.id.password);
		forgottxt = (TextView) findViewById(R.id.forgotpassword);
		loginbtn = (Button) findViewById(R.id.login_btn);
		create_account1 = (TextView) findViewById(R.id.create_account1);
		create_account2 = (TextView) findViewById(R.id.create_account2);
		alt_login = (TextView) findViewById(R.id.alt_login);
		fblogin = (Button) findViewById(R.id.fb_btn);
		twlogin = (Button) findViewById(R.id.tw_btn);


		Typeface tf_bc = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-BoldCondensed.otf");
		Typeface tf_mc = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-MediumCond.otf");
		
		nametxt.setTypeface(tf_bc);
		name.setTypeface(tf_bc);
		passtxt.setTypeface(tf_bc);
		pass.setTypeface(tf_bc);
		forgottxt.setTypeface(tf_mc);
		create_account1.setTypeface(tf_bc);
		create_account2.setTypeface(tf_bc);
		alt_login.setTypeface(tf_bc);

		
		pdialog = new CustomDialog(this);
		pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		loginbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pdialog.show();
				
				NetRequest nr=new NetRequest(LoginActivity.this);
		        nr.execute("http://android.makko.co");
		        nr.setOnSuccessListener(new OnSuccessListener() {
					@Override
					public void doSuccess(String result) {
						// TODO Auto-generated method stub
						pdialog.dismiss();
						try{
							//Log.e("login :", "succes net request");
							if(User.check_user(User.check_login+"email="+name.getText().toString()+"&password="+pass.getText().toString())){
								//login true
								Utils.SavePreferences("login", 1, LoginActivity.this);
								pdialog.show();
								Intent intent = new Intent(LoginActivity.this, MakkoActivity.class);
								startActivity(intent);
							}else{
								// login error
								Toast.makeText(getApplicationContext(), "Invalid Username & Password", Toast.LENGTH_SHORT).show();
							}
						}catch (Exception e) {
							// TODO: handle exception
						}
						
					}

					@Override
					public void doError() {
						// TODO Auto-generated method stub
						pdialog.dismiss();
						//Toast.makeText(getApplicationContext(), "network error", Toast.LENGTH_SHORT).show();
						DialogControl.dialogError(LoginActivity.this, pdialog, 1);
					}
		        });
			}
		});
		
		forgottxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pdialog.show();
				Intent intent = new Intent(LoginActivity.this, MakkoActivity.class);
				startActivity(intent);
			}
		});

		create_account2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
//				startActivity(intent);
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.setData(Uri.parse("http://makko.co/join.php"));
				startActivity(intent);
			}
		});
		
		
		fblogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				wv.loadUrl("http://makko.co/join.php?login&oauth_provider=facebook");
				wv.setVisibility(View.VISIBLE);
//				set keyboard virtual
				wv.requestFocus(View.FOCUS_DOWN);
				wv.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch (event.getAction()){
							case MotionEvent.ACTION_DOWN:
							case MotionEvent.ACTION_UP:
								if(!v.hasFocus()){
									v.requestFocus();
								}
								break;
						}
						return false;
					}
				});
				
				wv.setWebViewClient(new WebViewClient(){
					@Override
					public boolean shouldOverrideUrlLoading(WebView wv, String url){
						wv.loadUrl(url);
						return true;
					}
					
					@Override
					public void onPageStarted(WebView wv, String url, Bitmap bm){
						if (url.contains("from=fb")){
							wv.setVisibility(View.GONE);
							pdialog.show();
							Intent intent = new Intent(LoginActivity.this, MakkoActivity.class);
							intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivity(intent);
						}
					}
					
					@Override
					public void onLoadResource(WebView wv, String url){
						pdialog.show();
					}
					
					@Override
					public void onPageFinished(WebView wv, String url){
						if(pdialog != null){
							pdialog.dismiss();
						}
					}
				});
			}
		});
		
		twlogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				wv.loadUrl("http://makko.co/join.php?login&oauth_provider=twitter");
				wv.setVisibility(View.VISIBLE);
//				set keyboard virtual
				wv.requestFocus(View.FOCUS_DOWN);
				wv.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch (event.getAction()){
							case MotionEvent.ACTION_DOWN:
							case MotionEvent.ACTION_UP:
								if(!v.hasFocus()){
									v.requestFocus();
								}
							break;
						}
						return false;
					}
				});
				
				wv.setWebViewClient(new WebViewClient(){
					@Override
					public boolean shouldOverrideUrlLoading(WebView wv, String url){
						wv.loadUrl(url);
						return true;
					}
					
					@Override
					public void onPageStarted(WebView wv, String url, Bitmap bm){
						if (url.contains("from=fb")){
							wv.setVisibility(View.GONE);
							pdialog.show();
							Intent intent = new Intent(LoginActivity.this, MakkoActivity.class);
							intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivity(intent);
						}
					}
					
					@Override
					public void onLoadResource(WebView wv, String url){
						pdialog.show();
					}
					
					@Override
					public void onPageFinished(WebView wv, String url){
						if(pdialog != null){
							pdialog.dismiss();
						}
					}
				});
			}
		});
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if (wv.getVisibility() == View.VISIBLE){
				wv.setVisibility(View.GONE);
			} else {
				finish();
			}
		}
		return true; 
	}
}