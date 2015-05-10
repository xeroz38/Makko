package com.project.adapter;

import com.project.component.CustomDialog;
import com.project.control.Download;
import com.project.control.Download.onProgressListener;
import com.project.control.EpisodeProgress;
import com.project.control.FileExplorer;
import com.project.control.ImageLoader;
import com.project.main.GalleryActivity;
import com.project.main.MakkoActivity;
import com.project.model.*;
import com.project.view.R;

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
import android.widget.*;

public class EpisodeListAdapter extends BaseAdapter
{
	Episode[] episodes;
	private Activity activity;

	int resId;
	private static LayoutInflater inflater = null;
	ImageLoader imageLoader;
	public static CustomDialog pdialog;
	//    private final int TIME_THREAD = 3000;


	public EpisodeListAdapter(Context context, int textViewResourceId, Episode[] episodes)
	{
		super();
		this.episodes = episodes;
		this.activity = (Activity) context;
		this.resId = textViewResourceId;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());

		//	    progressBar = new ProgressDialog(context);
		//		progressBar.setCancelable(true);
		//		progressBar.setMessage("Downloading...");
		//		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//		progressBar.setProgress(0);
		//
		//		pbar = (ProgressBar) activity.findViewById(R.id.progressbar_Horizontal);
		//		pbar.setProgress(0);
	}
	boolean checkEpisode(String idEpisode, String user){
		boolean check = UserEpsLite.checkEps(UserEpsLite.query_user_eps+" WHERE username = '" + user + "' AND idEpisode='"+idEpisode+"'", activity.getApplicationContext());
		return check;
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
		final Episode eps = getItem(position);
		View vi = convertView;
		//		final Episode eps = getItem(position);

		final ViewHolder holder;
		//final EpisodeProgress down = new EpisodeProgress();
		//        boolean get_index = false;
		final boolean check = UserEpsLite.checkEps(UserEpsLite.query_user_eps + " WHERE username = '" + oComic.username + "' AND idEpisode='" + episodes[position].getId() + "'", activity);
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


		//        if (episodes[position].mDownloading)
		//        {
		//            holder.p_btn.setVisibility(View.VISIBLE);
		//        }
		//        else
		//        {
		//            holder.p_btn.setVisibility(View.GONE);
		//        }

		Log.d("com.project.adapter", "episodes[position].mDownloading = " + episodes[position].mDownloading);
		Log.d("com.project.adapter", "episodes[position].getId() = " + episodes[position].getId());


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
				holder.p_btn.setVisibility(View.GONE);
				holder.btn.setText("Read Now");
				holder.btn.setEnabled(true);
			}
			else
			{
				holder.p_btn.setText(episode.mProgressInButton);
				//                holder.p_btn.setText(episode.mProgressInBar);
			}
			if (episodes[position].mDownloading)
			{
				holder.p_btn.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.p_btn.setVisibility(View.GONE);
				holder.btn.setEnabled(true);
			}
		}
		else
		{
			//           final boolean check = UserEpsLite.checkEps(UserEpsLite.query_user_eps + " WHERE username = '" + oComic.username + "' AND idEpisode='" + episodes[position].getId() + "'", activity);

			if (check)
			{
				holder.btn.setText("Read Now");
				holder.btn.setEnabled(true);
				holder.p_btn.setVisibility(View.GONE);
			}
			else
			{
				holder.btn.setText("Download");
				holder.btn.setEnabled(true);
				holder.p_btn.setVisibility(View.GONE);
			}
		}

		holder.title.setText(episodes[position].getJudulComic());
		holder.episode.setText("EPISODE # " + episodes[position].getJudul());
		holder.rdate.setText(episodes[position].getRelease());

		final String idComic = episodes[position].getIdComic();
		final String idEpisode = episodes[position].getId();

		final String judulComic = episodes[position].getJudulComic();
		final String judulEpisode = episodes[position].getJudul();

		final String link = episodes[position].getDownLink();
		final int post = position;

		//        final boolean check = UserEpsLite.checkEps(UserEpsLite.query_user_eps + " WHERE username = '" + oComic.username + "' AND idEpisode='" + idEpisode + "'", activity);

		pdialog = new CustomDialog(activity);
		pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		final int down_size = EpisodeProgress.arrList.size();

		Log.e(" down_size : ", ""+down_size);
		//        if (down_size > 0)
		//        {
		//            for (int a = 0; a < down_size; a++)
		//            {
		//
		//                String down_id = EpisodeProgress.arrList.get(a).getIndex();
		//                final Boolean down_value = EpisodeProgress.arrList.get(a).getValue();
		//                //Log.e("id eps : "+idEpisode, down_id+", "+down_value);
		//
		////                if (idEpisode.equals(down_id) && down_value == true)
		////                {
		////                    get_index = true;
		////                    //Log.e("down value : ", ""+down_value);
		////
		////                    final int index_progress = a;
		////                    //Log.e("idEpisode", "trruuee");
		//////                    holder.btn.setText("Downloading..");
		////                    holder.btn.setEnabled(false);
		////                    // Toast.makeText(activity.getApplicationContext(), "Downloading..", Toast.LENGTH_SHORT).show();
		////                    ///thread
		////
		////                    //holder.btn.setText("Downloading.."+z);
		////                    Thread currentThread = new Thread(new Runnable()
		////                    {
		////                        //
		////                        @Override
		////                        public void run()
		////                        {
		////                            Log.d("com.project.adapter", "creating new thread");
		////                            while (EpisodeProgress.arrList.get(index_progress).getValue())
		////                            {
		////                                // process();
		////
		////                                try
		////                                {
		////                                    Thread.sleep(TIME_THREAD);
		////                                    //Log.e("status bottom:", ""+down_value);
		//////					            	Log.e("progress:", ""+down.arrList.get(index_progress).getProgress());
		////                                    final int progress = EpisodeProgress.arrList.get(index_progress).getProgress();
		////                                    final int kb = EpisodeProgress.arrList.get(index_progress).getKb();
		////                                    final int total = EpisodeProgress.arrList.get(index_progress).getTotal();
		////                                    //Log.e("value from adapter : ", ""+total );
		////
		////
		////                                    Log.d("com.project.adapter", "progress = " + progress);
		////                                    Log.d("com.project.adapter", "kb = " + kb);
		////                                    Log.d("com.project.adapter", "total = " + total);
		////
		////                                    EpisodeProgress episodeProgress = EpisodeProgress.arrList.get(index_progress);
		////                                    Log.d("com.project.adapter", "episodeProgress.getIndex() = " + episodeProgress.getIndex());
		////
		////                                    for (Episode episode : episodes)
		////                                    {
		////                                        if (episode.getId().equals(episodeProgress.getIndex()))
		////                                        {
		////                                            episode.mProgressInButton = progress + " %";
		////                                            episode.mProgressInBar = "" + kb + " kb" + " / " + total + " kb";
		////                                            episode.mProgressValue = progress;
		////
		////                                            try
		////                                            {
		////                                                notifyDataSetChanged();
		////                                            }
		////                                            catch (Exception e)
		////                                            {
		////                                                e.printStackTrace();
		////                                            }
		////                                            break;
		////                                        }
		////                                    }
		////
		//////                                    activity.runOnUiThread(new Runnable() {
		//////
		//////										@Override
		//////										public void run() {
		//////											// TODO Auto-generated method stub
		//////
		//////
		//////							            	holder.btn.setText(progress + " %");
		//////
		//////											holder.p_btn.setVisibility(View.VISIBLE);
		//////											holder.p_btn.setProgress(progress);
		//////											holder.p_btn.setText("" + kb + " kb" + " / " + total + " kb");
		//////										}
		//////									});
		////                                }
		////                                catch (Exception e)
		////                                {
		////                                    System.out.println("Error: " + e);
		////                                }
		////                            }
		////
		////                            if (!EpisodeProgress.arrList.get(index_progress).getValue())
		////                            {
		////
		////                                //Log.e("status berakhir:", ""+down.arrList.get(index_progress).getValue());
		////                                EpisodeProgress episodeProgress = EpisodeProgress.arrList.get(index_progress);
		////                                for (Episode episode : episodes)
		////                                {
		////                                    if (episode.getId().equals(episodeProgress.getIndex()))
		////                                    {
		////                                        episode.mDownloading = false;
		////
		////                                        oComic.judulComic = judulComic;
		////                                        oComic.judulEpisode = judulEpisode;
		////                                        try
		////                                        {
		////                                            notifyDataSetChanged();
		////                                        }
		////                                        catch (Exception e)
		////                                        {
		////                                            e.printStackTrace();
		////                                        }
		////                                        break;
		////                                    }
		////                                }
		////
		//////								activity.runOnUiThread(new Runnable() {
		//////									public void run() {
		//////
		//////										holder.btn.setText("Read Now");
		//////										holder.btn.setEnabled(true);
		//////										holder.p_btn.setVisibility(View.GONE);
		////////
		////////										oComic.judulComic = judulComic;
		////////										oComic.judulEpisode = judulEpisode;
		//////									}
		//////									});
		////                            }
		////                        }
		////                    });
		////
		////                    currentThread.start();
		////                }
		////				else{
		////					if(check==true){
		////						holder.btn.setText("Read Now");
		////					}else{
		////						holder.btn.setText("Download");
		////					}
		////				}
		//            }
		//
		////            if (!get_index)
		////            {
		////                if (check == true)
		////                {
		////                    holder.btn.setText("Read Now");
		////                    holder.btn.setEnabled(true);
		////                }
		////                else
		////                {
		////                    holder.btn.setText("Download");
		////                    holder.btn.setEnabled(true);
		////                }
		////            }
		//
		//        }
		//        else
		//        {
		//
		//            if (check == true)
		//            {
		//                holder.btn.setText("Read Now");
		//                holder.btn.setEnabled(true);
		//            }
		//            else
		//            {
		//                holder.btn.setText("Download");
		//                holder.btn.setEnabled(true);
		//            }
		//        }

		if (!episodes[position].getIcon().equals("N/A"))
		{
			holder.img.setTag(episodes[position].getIcon());
			imageLoader.DisplayImage(episodes[position].getIcon(), activity, holder.img);
		}
		else
		{
			holder.img.setImageResource(R.drawable.default_image_icon);
		}

		final boolean checkFile = FileExplorer.getFileNames(FileExplorer.extStorageDirectory + "/" + idComic, idEpisode + ".zip");

		final Episode episode = episodes[position];
		final ViewHolder holder1 = holder;
		final boolean checkFile1 = checkFile;
		final String idEpisode1 = idEpisode;
		final String idComic1 = idComic;
		final int post1 = post;
		final int down_size1 = down_size;
		final String judulComic1 = judulComic;
		final String judulEpisode1 = judulEpisode;
		final boolean check1 = check;
		holder.btn.setOnClickListener(new OnClickListener()
		{
			private Episode mEpisode = episode;
			private final ViewHolder mHolder = holder1;
			private final boolean mCheckFile = checkFile1;
			private final String mIdEpisode = idEpisode1;
			private final String mIdComic = idComic1;
			private final int mPost = post1;
			private final int mDown_size = down_size1;
			private final String mJudulComic = judulComic1;
			private final String mJudulEpisode = judulEpisode1;
			private final boolean mCheck = check1;

			@Override
			public void onClick(View v)
			{
				if (mHolder.btn.getText().equals("Download"))
				{
					//					eps.isDownloading = true;
					if (mCheckFile)
					{
						Log.d("com.project.adapter", "check file ok");

						//insert to favorite user
						try
						{
							UserEpsLite.insertUserEps(activity.getApplicationContext(), mIdEpisode);
							//UserEpsLite.insertUserEps(activity.getApplicationContext(), username, idEpisode);
							mHolder.btn.setText("Read Now");
							//check sqlite comic
							if (ComicLite.availComic(activity.getApplicationContext(), mIdComic) != true)
							{
								ComicLite.insertComic(activity.getApplicationContext(), mIdComic);
							}
							//check sqlite episode
							if (EpisodeLite.availEps(activity.getApplicationContext(), mIdEpisode) != true)
							{
								EpisodeLite.insertEpisode(activity.getApplicationContext(), mIdEpisode);
							}
							//check=true;
						}
						catch (Exception e)
						{
							Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						//                        Log.d("com.project.adapter", "download");
						//download
						try
						{
							holder.btn.setText("Downloading..");
							holder.btn.setEnabled(false);
							Toast.makeText(activity.getApplicationContext(), "Download in Progress", Toast.LENGTH_LONG).show();
							//                            Episode[] epsLink = Episode.getEpisode(Episode.url_down_link + mIdEpisode);

							Episode episode = mEpisode;
							//                            String link = episode.getDownLink();
							//                            Log.d("com.project.adapter", "link = " + link);
							//                            Log.d("com.project.adapter", "epsLink[0].getIdComic() = " + episode.getIdComic());

							Download dl = new Download(mIdEpisode, activity.getApplicationContext());

							//                            Log.d("com.project.adapter", "idEpisode = " + mIdEpisode);
							dl.execute(mIdComic, mIdEpisode, link);
							//                            EpisodeProgress.addProgress(mIdEpisode, true, 0, 0, 0);
							episode.mDownloading = true;

							dl.setOnProgressListener(new onProgressListener()
							{
								@Override
								public void doProgress(final int value, final int kb, final int total, String episode)
								{
									if (value % 10 == 0)
									{
										MakkoActivity.setProgressNotif(activity, value, mPost);
									}

									Log.d("com.project.adapter", "episode = " + episode);
									for (final Episode e : episodes)
									{
										if (e.getId().equals(episode))
										{
											Log.d("com.project.adapter", "found downloading row");
											e.mDownloading = true;
											e.mProgressInButton = value + " %";
											e.mProgressInBar = "" + kb + " kb" + " / " + total + " kb";

											boolean update = false;
											Log.d("com.project.adapter", "value = " + value);
											Log.d("com.project.adapter", "e.mProgressValue = " + e.mProgressValue);

											//insert to database when progress 100%
											if(value==100){
												if(check){
													UserEpsLite.insertUserEps(activity.getApplicationContext(), episode);
												}
												if(ComicLite.availComic(activity.getApplicationContext(), idComic)!=true){
													ComicLite.insertComic(activity.getApplicationContext(), idComic);
												}
												//check sqlite episode
												if(EpisodeLite.availEps(activity.getApplicationContext(), episode)!=true){
													EpisodeLite.insertEpisode(activity.getApplicationContext(), episode);
												}
											}
											if (e.mProgressValue != value)
											{
												update = true;
											}

											e.mProgressValue = value;

											if (update)
											{
												Log.d("com.project.adapter", "update");
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

									//									// TODO Auto-generated method stub
									//									activity.runOnUiThread(new Runnable() {
									//
									//										@Override
									//										public void run() {
									//											// TODO Auto-generated method stub
									//											if (value % 10 == 0){
									//												MakkoActivity.setProgressNotif(activity, value, post);
									//											}
									//
									//
									//											holder.btn.setText(value + " %");
									//											holder.btn.setEnabled(false);
									//											holder.p_btn.setVisibility(View.VISIBLE);
									//											holder.p_btn.setProgress(value);
									//											holder.p_btn.setText("" + kb + " kb" + " / " + total + " kb");
									//										}
									//									});

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
											if (mDown_size > 0)
											{

												for (int a = 0; a < mDown_size; a++)
												{

													String down_id = EpisodeProgress.arrList.get(a).getId();
													Log.e(""+mDown_size, down_id);
													if (mIdEpisode.equals(down_id))
													{
														//                                                    	Log.e(mIdEpisode, down_id);
														EpisodeProgress progress = EpisodeProgress.arrList.get(a);
														progress.setValue(false);
														for (Episode e : episodes)
														{

															if (e.getId().equals(progress.getId()))
															{
																e.mDownloading = false;
																//                                                                Log.e("1", "1");
																Runnable runnable = new Runnable()
																{
																	public void run()
																	{
																		try
																		{
																			//                                                                        	Log.e("2", "2");
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
											//											holder.btn.setText("Read Now");
											//											holder.btn.setEnabled(true);
											//											holder.p_btn.setVisibility(View.GONE);
											//											eps.isDownloading = false;
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
											oComic.judulComic = mJudulComic;
											oComic.judulEpisode = mJudulEpisode;

											if (MakkoActivity.notificationManager != null)
											{
												MakkoActivity.notificationManager.cancel(mPost);
											}
											MakkoActivity.setNotif(activity, oComic.judulComic, oComic.judulEpisode);
										}
									});
								}
							});

							//                            mHolder.btn.setText("Downloading..");
							//                            mHolder.btn.setEnabled(false);

							//                            UserEpsLite.insertUserEps(activity.getApplicationContext(), mIdEpisode);
							//                            if (ComicLite.availComic(activity.getApplicationContext(), mIdComic) != true)
							//                            {
							//                                ComicLite.insertComic(activity.getApplicationContext(), mIdComic);
							//                            }
							//                            if (EpisodeLite.availEps(activity.getApplicationContext(), mIdEpisode) != true)
							//                            {
							//                                EpisodeLite.insertEpisode(activity.getApplicationContext(), mIdEpisode);
							//                            }
						}
						catch (Exception e)
						{
							holder.btn.setText("Download");
							holder.btn.setEnabled(true);
							holder.p_btn.setVisibility(View.GONE);
							Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
						}
					}
				}
				else
				{
					if (mCheck)
					{
						oComic.idComic = mIdComic;
						oComic.idEpisode = mIdEpisode;
						Intent intent = new Intent(activity, GalleryActivity.class);
						pdialog.show();
						activity.startActivity(intent);
					}
				}
			}
		});

		//        notifyDataSetChanged();

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
