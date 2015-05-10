package com.project.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.component.CustomDialog;
import com.project.control.Download;
import com.project.control.Download.onProgressListener;
import com.project.control.EpisodeProgress;
import com.project.control.FileExplorer;
import com.project.control.ImageLoader;
import com.project.main.GalleryActivity;
import com.project.main.MakkoActivity;
import com.project.model.ComicLite;
import com.project.model.Episode;
import com.project.model.EpisodeLite;
import com.project.model.UserEpsLite;
import com.project.model.oComic;
import com.project.view.R;

public class DetailEpisodeListAdapter extends BaseAdapter
{
	//    int z;
	private final int TIME_THREAD = 3000;

	Episode[] episodes;
	Activity  activity;
	int       resId;
	private static LayoutInflater inflater = null;
	ImageLoader imageLoader;
	public static CustomDialog pdialog;

	//check exist episode by id episode and username in sqlite
	boolean checkEpisode(String idEpisode, String user){
		boolean check = UserEpsLite.checkEps(UserEpsLite.query_user_eps+" WHERE username = '" + user + "' AND idEpisode='"+idEpisode+"'", activity.getApplicationContext());
		return check;
	}

	public DetailEpisodeListAdapter(Context context, int textViewResourceId, Episode[] episodes)
	{
		super();
		this.episodes = episodes;
		this.activity = (Activity) context;
		this.resId = textViewResourceId;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return episodes.length;
	}


	@Override
	public Episode getItem(int position)
	{
		// TODO Auto-generated method stub
		return episodes[position];
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		boolean isDownload = false;
		final Episode eps = getItem(position);
		View vi = convertView;
		final ViewHolder holder;
		final EpisodeProgress down = new EpisodeProgress();
		final String idComic = episodes[position].getIdComic();
		final String idEpisode = episodes[position].getId();

		final String judulComic = episodes[position].getJudulComic();
		final String judulEpisode = episodes[position].getJudul();
		String release = episodes[position].getRelease();
		String icon = episodes[position].getIcon();
		final String link = episodes[position].getDownLink();

		final int post = position;

		final boolean check = checkEpisode(idEpisode, oComic.username);
		final boolean checkFile = FileExplorer.getFileNames(FileExplorer.extStorageDirectory + "/" + idComic, idEpisode + ".zip");
		boolean dbAvail = false;
		/* boolean availComic = ComicLite.availComic(activity.getApplicationContext(), idComic);
        boolean availEpisode = EpisodeLite.availEps(activity.getApplicationContext(), idEpisode);*/
		if (convertView == null)
		{
			vi = inflater.inflate(R.layout.list_episode, null);
			holder = new ViewHolder();

			holder.title = (TextView) vi.findViewById(R.id.title);
			holder.episode = (TextView) vi.findViewById(R.id.episode);
			holder.rdate = (TextView) vi.findViewById(R.id.rdate);
			holder.img = (ImageView) vi.findViewById(R.id.img);
			holder.btn = (Button) vi.findViewById(R.id.dlbtn);
			holder.p_btn = (com.project.component.TextProgressBar) vi.findViewById(R.id.progressbar_Horizontal);

			vi.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) vi.getTag();
		}


		Typeface tf_bold = Typeface.createFromAsset(activity.getAssets(), "fonts/DINNextLTPro-Bold.otf");
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/DINNextLTPro-Regular.otf");
		Typeface tf_cb = Typeface.createFromAsset(activity.getAssets(), "fonts/DINNextLTPro-BoldCondensed.otf");

		holder.title.setTypeface(tf_bold);
		holder.episode.setTypeface(tf);
		holder.rdate.setTypeface(tf);
		holder.btn.setTypeface(tf_cb);
		holder.title.setText(judulComic);
		holder.episode.setText("EPISODE # " + judulEpisode);
		holder.rdate.setText(release);
		holder.img.setTag(icon);
		imageLoader.DisplayImage(icon, activity, holder.img);

		if (episodes[position].mDownloading)
		{
			holder.btn.setEnabled(false);
			Episode episode = episodes[position];
			//
			holder.p_btn.setVisibility(View.VISIBLE);
			holder.p_btn.setProgress(episode.mProgressValue);

			if (episode.mProgressValue <= 0)
			{
				//holder.btn.setText("Downloading..");
			}
			else if(episode.mProgressValue == 100){
				Log.e("episode.mProgressValue == 100", episode.mProgressInButton);
				holder.p_btn.setVisibility(View.GONE);
				holder.btn.setText("Read Now");
				holder.btn.setEnabled(true);
			}
			else
			{
				Log.e("episode.mProgressValue", episode.mProgressInButton);
				holder.p_btn.setText(episode.mProgressInButton);
			}

		}
		else
		{

			if(checkFile){
				isDownload=false;
				if(!check){
					holder.btn.setText("Download");
					dbAvail=false;
				}else{
					holder.btn.setText("Read Now");
					dbAvail=true;
				}
				holder.btn.setEnabled(true);
				holder.p_btn.setVisibility(View.GONE);

			}
			else{
				isDownload=true;
				holder.btn.setText("Download");
				holder.btn.setEnabled(true);
				holder.p_btn.setVisibility(View.GONE);
			}

		}

		final int down_size = down.arrList.size();

		final boolean isDownload1 = isDownload; 
		final boolean dbAvail1 = dbAvail;
		pdialog = new CustomDialog(activity);
		pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

		final Episode episode = episodes[position];
		holder.btn.setOnClickListener(new OnClickListener()
		{
			private Episode mEpisode = episode;
			private final boolean downloadStat = isDownload1;
			//private boolean dbStat = dbAvail1;
			@Override
			public void onClick(View v)
			{

				if(downloadStat){
					//download
					try
					{
						holder.btn.setText("Downloading..");
						holder.btn.setEnabled(false);
						Download dl = new Download(idEpisode, activity);
						dl.execute(idComic, idEpisode, link);
						Episode episode = mEpisode;
						episode.mDownloading = true;
						dl.setOnProgressListener(new onProgressListener()
						{

							@Override
							public void doProgress(final int value, final int kb, final int total, String episode)
							{
								if (value % 20 == 0)
								{
									MakkoActivity.setProgressNotif(activity, value, post);
								}

								for (Episode e : episodes)
								{
									if (e.getId().equals(episode))
									{
										//	                                        Log.d("com.project.adapter", "found downloading row");
										e.mDownloading = true;
										e.mProgressInButton = value + " %";
										e.mProgressInBar = "" + kb + " kb" + " / " + total + " kb";

										boolean update = false;
										//	                                        Log.d("com.project.adapter", "value = " + value);
										//	                                        Log.d("com.project.adapter", "e.mProgressValue = " + e.mProgressValue);

										//insert to database when progress 100%
										if(value==100){
											//	                                        	if(!check){
											//                                        		Log.d("insertUserEps", "insertUserEps");
											try {
												UserEpsLite.insertUserEps(activity.getApplicationContext(), episode);
												if(ComicLite.availComic(activity.getApplicationContext(), idComic)!=true){
													//                                				Log.d("insertComic", "insertComic");
													ComicLite.insertComic(activity.getApplicationContext(), idComic);
												}
												//check sqlite episode
												if(EpisodeLite.availEps(activity.getApplicationContext(), episode)!=true){
													//                                				Log.d("insertEpisode", "insertEpisode");
													EpisodeLite.insertEpisode(activity.getApplicationContext(), episode);
												}
											} catch (Exception e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											//dbStat=true;
										}

										if (e.mProgressValue != value)
										{
											update = true;
										}

										e.mProgressValue = value;

										if (update)
										{
											//	                                            Log.d("com.project.adapter", "update");
											//                                                notifyDataSetChanged();
											Runnable runnable = new Runnable()
											{
												public void run()
												{
													try
													{
														notifyDataSetChanged();
													}
													catch (Exception e1)
													{
														e1.printStackTrace();
													}
												}
											};
											activity.runOnUiThread(runnable);
										}
										break;
									}
								}
							}

							@Override
							public void doFinish()
							{
								// TODO Auto-generated method stub
								activity.runOnUiThread(new Runnable()
								{

									@Override
									public void run()
									{
										// TODO Auto-generated method stub
										int index = 0;
										if (down_size > 0)
										{
											for (int a = 0; a < down_size; a++)
											{
												String down_id = down.arrList.get(a).getId();

												if (idEpisode.equals(down_id))
												{
													final EpisodeProgress progress = EpisodeProgress.arrList.get(a);
													progress.setValue(false);

													for (Episode e : episodes)
													{
														if (e.getId().equals(progress.getId()))
														{	
															index = a;
															e.mDownloading = false;
															Runnable runnable = new Runnable()
															{
																public void run()
																{
																	try
																	{
																		notifyDataSetChanged();
																	}
																	catch (Exception e1)
																	{
																		e1.printStackTrace();
																	}
																}
															};
															activity.runOnUiThread(runnable);
															break;
														}
													}
													break;
												}
											}
										}
										if(checkEpisode(idEpisode, oComic.username)==true){
											holder.btn.setText("Read Now");
											holder.btn.setEnabled(true);
											holder.p_btn.setVisibility(View.GONE);
										}else{
											holder.btn.setText("Download");
											holder.btn.setEnabled(true);
											holder.p_btn.setVisibility(View.GONE);
										}

										eps.isDownloading = false;

										oComic.judulComic = judulComic;
										oComic.judulEpisode = judulEpisode;

										if (MakkoActivity.notificationManager != null)
										{
											MakkoActivity.notificationManager.cancel(post);
										}
										MakkoActivity.setNotif(activity, oComic.judulComic, oComic.judulEpisode);

										//                                        Toast.makeText(activity.getApplicationContext(), "Download Finish", Toast.LENGTH_SHORT).show();
									}
								});
							}
						});
					}
					catch (Exception e)
					{
						holder.btn.setText("Download");
						holder.btn.setEnabled(true);
						holder.p_btn.setVisibility(View.GONE);
						Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
					}
				}else{
					if(checkEpisode(idEpisode, oComic.username)){
						oComic.idComic = idComic;
						oComic.idEpisode = idEpisode;
						Intent intent = new Intent(activity, GalleryActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						pdialog.show();
						activity.startActivity(intent);
					}else{
						try {
							UserEpsLite.insertUserEps(activity.getApplicationContext(), idEpisode);
							if(!ComicLite.availComic(activity.getApplicationContext(), idComic)){
								//        				Log.d("insertComic", "insertComic");
								ComicLite.insertComic(activity.getApplicationContext(), idComic);
							}
							//check sqlite episode
							if(!EpisodeLite.availEps(activity.getApplicationContext(), idEpisode)){
								//        				Log.d("insertEpisode", "insertEpisode");
								EpisodeLite.insertEpisode(activity.getApplicationContext(), idEpisode);
							}
							holder.btn.setText("Read Now");
							//dbStat=true;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
		});


		vi.setClickable(true);
		vi.setFocusable(true);

		return vi;
	}

	static class ViewHolder
	{
		TextView                              title;
		TextView                              episode;
		TextView                              rdate;
		ImageView                             img;
		Button                                btn;
		com.project.component.TextProgressBar p_btn;
	}
}
