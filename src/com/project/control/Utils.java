package com.project.control;


import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class Utils {
	public static void CopyStream(InputStream is, OutputStream os)
	{
		final int buffer_size=1024;
		try
		{
			byte[] bytes=new byte[buffer_size];
			for(;;)
			{
				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){}
	}

	public static void antiClose(Activity act)
	{
		if (!act.isTaskRoot()) {
			final Intent intent = act.getIntent();
			final String intentAction = intent.getAction();
			if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) &&
					intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
				act.finish();
			}
		}
	}

	public static void SavePreferences(String ep, int page, Activity act) {
		SharedPreferences sharedPreferences = act.getSharedPreferences("shared", act.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(ep, page);
		editor.commit();
	}
}