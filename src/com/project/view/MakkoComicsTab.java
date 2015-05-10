package com.project.view;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.project.adapter.ComicListAdapter;
import com.project.component.CustomDialog;
import com.project.control.NetRequest;
import com.project.control.NetRequest.OnSuccessListener;
import com.project.control.Utils;
import com.project.model.Comic;

public class MakkoComicsTab extends ListActivity {
	private NetRequest nr;
	private ComicListAdapter cla;

	public CustomDialog pdialog;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);

		getListView().setCacheColorHint(0x00000000);
		getListView().setBackgroundResource(R.layout.backrepeat);

//		Comic[] comics = Comic.getAllComic(Comic.url_list_comic);

//		ComicListAdapter cla = new ComicListAdapter(this, R.layout.list_comic, ID, TITLES, GENRE, WRITER, ARTIST, PDATE, THUMBNAILS, EPISODE);
//		ComicListAdapter cla = new ComicListAdapter(this, R.layout.list_comic, comics);
//		setListAdapter(cla);

		getData();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0, "Refresh");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		/*Intent intent = new Intent(MakkoComicsTab.this, MakkoActivity.class);
		startActivity(intent);
		finish();*/
		getData();
		return false;
	}
	
	private void getData(){
		nr = new NetRequest(MakkoComicsTab.this);
		
		pdialog = new CustomDialog(this);
		pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		pdialog.show();
        
		nr.execute(Comic.url_list_comic);
		nr.setOnSuccessListener(new OnSuccessListener() {
			
			@Override
			public void doSuccess(String result) {
				// TODO Auto-generated method stub
				pdialog.dismiss();
				Comic[] comics = Comic.getAllComic(Comic.url_list_comic);
				cla = new ComicListAdapter(MakkoComicsTab.this, R.layout.list_comic, comics);
				setListAdapter(cla);
			}
			
			@Override
			public void doError() {
				// TODO Auto-generated method stub
				//oComic.dialogError(MakkoComicsTab.this, dialog)
				pdialog.dismiss();
				//DialogControl.dialogError(MakkoComicsTab.this, dialog, 2);
//				
				//DialogControl.dialogError(MakkoComicsTab.this, dialog);
				
				//Toast.makeText(getApplicationContext(), "Network connection error", Toast.LENGTH_SHORT).show();
				final AlertDialog alertDialog = new AlertDialog.Builder(MakkoComicsTab.this).create();
		        alertDialog.setCancelable(false);
				alertDialog.setTitle("");
				alertDialog.setIcon(R.drawable.default_image);
				alertDialog.setMessage("Network Connection Error");
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry" ,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						alertDialog.dismiss();
						/*Intent intent = new Intent(MakkoComicsTab.this, MakkoActivity.class);
						startActivity(intent);
						finish();*/
						getData();
					}
				});
				
				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						alertDialog.dismiss();
						
					}
				});
				
				alertDialog.show();
				
			}
		});
	}

}
