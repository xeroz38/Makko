package com.project.main;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.project.control.Utils;
import com.project.model.oComic;
import com.project.view.R;

public class SplashScreen extends Activity {
	protected int _splashTime = 2000;

	SharedPreferences sharedPreferences;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		
		sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
		final int login = sharedPreferences.getInt("login", 0);
		
		TimerTask task = new TimerTask()
		{

			@Override
			public void run() {
				finish();
				if (login == 0){
					Intent mainIntent = new Intent().setClass(SplashScreen.this, LoginActivity.class);
					startActivity(mainIntent);
				} else {
					Intent mainIntent = new Intent().setClass(SplashScreen.this, MakkoActivity.class);
					startActivity(mainIntent);
				}
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, _splashTime);
	}
}