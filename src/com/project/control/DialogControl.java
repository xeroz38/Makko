package com.project.control;

import com.project.component.CustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

public class DialogControl {
	
	public static void dialogError(final Activity activity, CustomDialog dialog, int con){
		String b1 = null;
		String b2="Cancel";
		
		if(con==1){
			b1="OK";
		}else if(con==2){
			b1="Retry";
		}
		
		dialog.dismiss();
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setCancelable(false);
		alertDialog.setTitle("");
		alertDialog.setMessage("Network Connection Error");
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, b1 ,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		
		if(con==2){
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					alertDialog.dismiss();
					
				}
			});
		}

		alertDialog.show();
	}
	
	
}
