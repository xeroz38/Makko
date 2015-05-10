package com.project.view;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.project.adapter.EpisodeListAdapter;
import com.project.component.CustomDialog;
import com.project.control.NetRequest;
import com.project.control.NetRequest.OnSuccessListener;
import com.project.control.Utils;
import com.project.model.Episode;

public class ComicEpisodeTab extends Activity {

	private ListView listview;
	NetRequest nr;
	
	public CustomDialog pdialog;

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0, "Refresh");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		getData();
		//finish();
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Utils.antiClose(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_episode);

		
		pdialog = new CustomDialog(this);
		pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		pdialog.show();
		
		listview = (ListView) findViewById(R.id.list);

		listview.setPadding(15, 15, 15, 5);
		listview.setDivider(new PaintDrawable(Color.DKGRAY));
		listview.setDividerHeight(1);
		listview.setVerticalScrollBarEnabled(false);
		listview.setCacheColorHint(0x00000000);
		listview.setBackgroundResource(R.layout.backrepeat);

//		Arrays.sort(episodes, new Comparator<Episode>() {
//			@Override
//			public int compare(Episode e1, Episode e2) {
//				// TODO Auto-generated method stub
//				return e1.getId().compareTo(e2.getId());
//			}
//		});
		getData();
		
	}
	private void getData(){
		nr = new NetRequest(ComicEpisodeTab.this);
		
        nr.execute(Episode.url_fresh_episode);
		nr.setOnSuccessListener(new OnSuccessListener() {

			@Override
			public void doSuccess(String result) {
				// TODO Auto-generated method stub
				pdialog.dismiss();
				Episode[] episodes = Episode.getEpisode(Episode.url_fresh_episode);
				EpisodeListAdapter ela = new EpisodeListAdapter(ComicEpisodeTab.this, R.layout.list_episode, episodes);
				listview.setAdapter(ela);
			}

			@Override
			public void doError() {
				// TODO Auto-generated method stub
				pdialog.dismiss();
				final AlertDialog alertDialog = new AlertDialog.Builder(ComicEpisodeTab.this).create();
		        alertDialog.setCancelable(false);
				alertDialog.setTitle("");
				alertDialog.setMessage("Network Connection Error");
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry" ,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						alertDialog.dismiss();
						/*Intent intent = new Intent(ComicEpisodeTab.this, MakkoDetailActivity.class);
						intent.putExtra("id", id);
						startActivity(intent);						
						finish();*/
						
						/*Intent intent = new Intent(DetailInfoTab.this, MakkoDetailActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
						
						intent.putExtra("id", id);
//						intent.putExtra("curr", "1");
//						intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
						//pdialog.show();
						oComic.idComic = id;
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
//		Episode[] episodes = Episode.getEpisode(Episode.url_fresh_episode);
//		EpisodeListAdapter ela = new EpisodeListAdapter(this, R.layout.list_episode, episodes);
//		listview.setAdapter(ela);
	}
}
