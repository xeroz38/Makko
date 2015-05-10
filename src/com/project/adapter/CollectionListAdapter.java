package com.project.adapter;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.component.CustomDialog;
import com.project.control.Download;
import com.project.control.FileExplorer;
import com.project.main.GalleryActivity;
import com.project.model.ComicLite;
import com.project.model.EpisodeLite;
import com.project.model.UserEpsLite;
import com.project.model.UserFavLite;
import com.project.model.oComic;
import com.project.view.R;

/**
 * This is adapter for expandable list-view for constructing the group and child
 * elements.
 */
public class CollectionListAdapter extends BaseExpandableListAdapter {

	ArrayList arrGroupelements;
	ArrayList arrChildelements;

	TextView tvPlayerName; 
	Button btnRead;
	String idCo;
	String idEp;

	private Context myContext;

	public static CustomDialog pdialog;

	File makkoImages = Environment.getExternalStorageDirectory();

	public CollectionListAdapter(Context context, ArrayList judulList, ArrayList judulEpsList) {
		myContext = context;
		arrGroupelements = judulList;
		arrChildelements = judulEpsList;

		pdialog = new CustomDialog(context);
		pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		AlertDialog deleteAlert = new AlertDialog.Builder(myContext).create();
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) myContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.child_row, null);
		}

		Typeface tf = Typeface.createFromAsset(myContext.getAssets(), "fonts/DINNextLTPro-Regular.otf");
		Typeface tf_cb = Typeface.createFromAsset(myContext.getAssets(), "fonts/DINNextLTPro-BoldCondensed.otf");	

		tvPlayerName = (TextView) convertView.findViewById(R.id.tvPlayerName);
		tvPlayerName.setTypeface(tf);
		tvPlayerName.setText("EPISODE # " + ((ArrayList)((ArrayList) arrChildelements.get(groupPosition)).get(childPosition)).get(1).toString());

		btnRead = (Button) convertView.findViewById(R.id.dlbtn);
		btnRead.setTypeface(tf_cb);
		btnRead.setTag(idCo);
		//		btnRead.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View arg0) {
		//				// TODO Auto-generated method stub
		//				idEp = ((ArrayList)((ArrayList) arrChildelements.get(groupPosition)).get(childPosition)).get(0).toString();
		//				idCo = ((ArrayList)arrGroupelements.get(groupPosition)).get(0).toString();
		//				oComic.idComic = idCo;
		//				oComic.idEpisode = idEp;
		//				pdialog.show();
		//				Intent intent=new Intent(myContext ,GalleryActivity.class);
		//				intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
		//				myContext.startActivity(intent);
		//			}
		//		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				idEp = ((ArrayList)((ArrayList) arrChildelements.get(groupPosition)).get(childPosition)).get(0).toString();
				idCo = ((ArrayList)arrGroupelements.get(groupPosition)).get(0).toString();
				oComic.idComic = idCo;
				oComic.idEpisode = idEp;
				pdialog.show();
				Intent intent=new Intent(myContext ,GalleryActivity.class);
				intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
				myContext.startActivity(intent);
			}
		});
		Button delEpisode = (Button) convertView.findViewById(R.id.delBtn);

		final AlertDialog delDialog = new AlertDialog.Builder(myContext).create();
		delDialog.setTitle("Delete Comic");
		delDialog.setMessage("Are You Sure To Delete Comic ?");
		delDialog.setButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				idEp = ((ArrayList)((ArrayList) arrChildelements.get(groupPosition)).get(childPosition)).get(0).toString();
				idCo = ((ArrayList)arrGroupelements.get(groupPosition)).get(0).toString();
				//				final File epsFile = new File(Download.extStorageDirectory+ "/" + idCo + "/" + idEp + ".zip");
				final File epsFile = new File(makkoImages + "/.Makko" + "/" + idCo + "/" + idEp + ".zip");

				try{
					//1. delete episode
					//2.delete user episode
					//3.delete file *.zip
					//delete episode
					EpisodeLite.deleteEpisode(myContext, idEp);
					//delete user episode
					UserEpsLite.deleteUserEpisode(myContext, idEp);
					//delete folder
					DeleteRecursive(epsFile);
					//					FileExplorer.deleteFile(epsFile);
					//Log.e("makkoFolder : ", ""+makkoFolder);
					/*Log.e("makkoFolder : ", makkoImages + "/.Makko" + "/" + idCo+ "/" + idEp + ".zip");
					Log.e("makkoFolder : ", Download.extStorageDirectory+ "/" + idCo + "/" + idEp + ".zip");*/
				}catch (Exception e) {
					// TODO: handle exception
					Log.e("exception : ", e.toString());
				}
				//Toast.makeText(myContext, idEp, Toast.LENGTH_SHORT).show();
			}
		});

		delDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});

		delEpisode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				delDialog.show();
			}
		});

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList) arrChildelements.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return arrGroupelements.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) myContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.group_row, null);
		}

		Typeface tf_bold = Typeface.createFromAsset(myContext.getAssets(), "fonts/DINNextLTPro-Bold.otf");

		TextView tvGroupName = (TextView) convertView.findViewById(R.id.tvGroupName);
		tvGroupName.setTypeface(tf_bold);

		tvGroupName.setText(((ArrayList)arrGroupelements.get(groupPosition)).get(1).toString());
		final String idComic =(((ArrayList)arrGroupelements.get(groupPosition)).get(0)).toString();
		//		File makkoImages = Environment.getExternalStorageDirectory();  
		final File makkoFolder = new File(makkoImages + "/.Makko" + "/" + idComic);

		final AlertDialog alertDialog = new AlertDialog.Builder(myContext).create();
		alertDialog.setTitle("Delete Comic");
		alertDialog.setMessage("Are You Sure To Delete Comic ?");
		alertDialog.setButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try{
					//delete row table user_eps
					UserEpsLite.deleteUserComic(myContext, idComic);
					//delete row table user_fav
					UserFavLite.deleteUserFav(myContext, idComic);

					//					//delete row table
					ComicLite.deleteComic(myContext, idComic);
					//					//delete row table episode
					EpisodeLite.deleteEpisodeComic(myContext, idComic);
					//delete folder
					DeleteRecursive(makkoFolder);	
					//					if(ComicLite.availComic(myContext, idComic)){
					//						Log.e("::", "create by ryan sikep comic");
					//					}
					//					if(EpisodeLite.availEpsComic(myContext, idComic)){
					//						Log.e("::", "create by ryan sikep episode");
					//					}

				}catch (Exception e) {
					// TODO: handle exception
					Log.e("::", "create by ryan sikep sekali");
				}
			}
		}); 
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alertDialog.show();
			}
		});

		return convertView;
	}

	void DeleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);

		fileOrDirectory.delete();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}