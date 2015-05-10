package com.project.view;

import java.util.Arrays;
import java.util.Comparator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.project.adapter.DetailEpisodeListAdapter;
import com.project.component.CustomDialog;
import com.project.control.NetRequest;
import com.project.control.NetRequest.OnSuccessListener;
import com.project.control.Utils;
import com.project.model.Episode;

public class DetailEpisodeTab extends ListActivity {
	private String id;

	NetRequest nr;
	
	public CustomDialog pdialog;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);

		getListView().setPadding(15, 15, 15, 15);
		getListView().setDivider(new PaintDrawable(Color.DKGRAY));
		getListView().setDividerHeight(1);
		getListView().setCacheColorHint(0x00000000);
		getListView().setBackgroundResource(R.layout.backrepeat);
		//bundle
		Bundle bundle = getIntent().getExtras();
		if(bundle!= null){
			//get id comic
			id = bundle.getString("id".toString());
		}

//		Episode[] episodes = Episode.getEpisode(Episode.url_list_episode+id);
		
//		Arrays.sort(episodes, new Comparator<Episode>() {
//			@Override
//			public int compare(Episode e1, Episode e2) {
//				// TODO Auto-generated method stub
////				return e1.getId().compareTo(e2.getId());
//				return e1.getJudul().compareTo(e2.getJudul());
//			}
//		});

//		DetailEpisodeListAdapter ela = new DetailEpisodeListAdapter(this, R.layout.list_episode, episodes);
//	  	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_episode, COUNTRIES));
//		DetailEpisodeListAdapter ela = new DetailEpisodeListAdapter(this, R.layout.list_episode, TITLES, TITLES2, EPISODE, RDATE, THUMBNAILS);
//		setListAdapter(ela);

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
//		Intent intent = new Intent(DetailEpisodeTab.this, MakkoDetailActivity.class);
//		startActivity(intent);
//		finish();
		//Toast.makeText(getApplicationContext(), "asd", Toast.LENGTH_SHORT).show();
		/*Intent intent = new Intent(DetailEpisodeTab.this, MakkoDetailActivity.class);
		intent.putExtra("id", id);
//		intent.setFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		//pdialog.show();
		oComic.idComic = id;
		startActivity(intent);
		finish();*/
		getData();
		return false;
	}
	
	private void getData() {
		// TODO Auto-generated method stub
		nr = new NetRequest(DetailEpisodeTab.this);
		
		pdialog = new CustomDialog(this);
		pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		pdialog.show();
        
        
        nr.execute(Episode.url_list_episode+id);
		nr.setOnSuccessListener(new OnSuccessListener() {

			@Override
			public void doSuccess(String result) {
				// TODO Auto-generated method stub
				pdialog.dismiss();
				Episode[] episodes = Episode.getEpisode(Episode.url_list_episode+id);
				DetailEpisodeListAdapter ela = new DetailEpisodeListAdapter(DetailEpisodeTab.this, R.layout.list_episode, episodes);
				setListAdapter(ela);
			}

			@Override 
			public void doError() {
				// TODO Auto-generated method stub
				pdialog.dismiss();
				final AlertDialog alertDialog = new AlertDialog.Builder(DetailEpisodeTab.this).create();
		        alertDialog.setCancelable(false);
				alertDialog.setTitle("");
				alertDialog.setMessage("Network Connection Error");
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry" ,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						alertDialog.dismiss();
						/*Intent intent = new Intent(DetailEpisodeTab.this, MakkoDetailActivity.class);
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
				//Toast.makeText(getApplicationContext(), "asd", Toast.LENGTH_SHORT).show();
			}
			
		});
	}
//	public boolean onKeyDown(int keyCode, KeyEvent event) { 
//		if (keyCode == KeyEvent.KEYCODE_BACK) 
//		{
//			Intent intent = new Intent(this, MakkoActivity.class);
//			intent.setFlags(intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//			startActivity(intent);
//			return true;
//		}
//		return super.onKeyDown(keyCode, event); 
//	} 

	/*    private Integer[] THUMBNAILS = {
    	R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6
    };

	static final String[] TITLES = new String[] {
	    "5 Menit Sebelum Tayang", "5 Menit Sebelum Tayang", "5 Menit Sebelum Tayang", "5 Menit Sebelum Tayang"
	};

	static final String[] TITLES2 = new String[] {
	    "Wanara", "Wanara", "Wanara", "Wanara"
	};

	static final String[] EPISODE = new String[] {
		"4", "3", "2", "1"
	};

	static final String[] RDATE = new String[] {
	    "22-08-2011", "25-07-2011", "16-07-2011", "01-07-2011"		
	};*/
}
