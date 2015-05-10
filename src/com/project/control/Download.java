package com.project.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.project.model.ComicLite;
import com.project.model.EpisodeLite;
import com.project.model.UserEpsLite;

public class Download extends AsyncTask<String, Integer, String> {

	public File fileDownload;
    private String mEpisode;

    int id =0;
    EpisodeProgress down;
    Context context;
    int value;
    public Download(String episode, Context context){
        mEpisode = episode;
        this.context= context;
        createMainPath();
	}
	public static final String extStorageDirectory = Environment.getExternalStorageDirectory()
			.toString() + "/.Makko";
	public static final String link="http://android.makko.co/assets/comic/";

	public onProgressListener opl;

	public void createMainPath(){
		try {
			File downloadsDirectory = new File(extStorageDirectory);
			downloadsDirectory.mkdirs();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


	@Override
	protected String doInBackground(String... url) {
		// TODO Auto-generated method stub
		downloadBook(url[0],url[1], url[2]);
		return null;
	}

	@Override
	protected void onPostExecute(String unused) {
		//    	if(EpisodeListAdapter.pbar != null){
		//            EpisodeListAdapter.pbar.setProgress(0);
		//            EpisodeListAdapter.pbar.setVisibility(View.GONE);
		//        }
		//        if(DetailEpisodeListAdapter.progressBar != null){
		//        	DetailEpisodeListAdapter.progressBar.setProgress(0);
		//            DetailEpisodeListAdapter.progressBar.dismiss();
		//        }
		if (opl != null)
		{
			opl.doFinish();			
		}
	}

	public boolean downloadBook(String idComic, String idEps, String link) {

		boolean flag = false;
		Uri ebookLinke = Uri.parse(link);
		int count;

		down = new EpisodeProgress();
		down.addProgress(idEps, true, 0, 0, 0);
		try {
			File downloadsDirectory = new File(extStorageDirectory+"/"+idComic);
			downloadsDirectory.mkdirs();
			URL u = new URL(ebookLinke.toString());
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect(); 

			String[] fileName0;
			String fileName;
			String cleanFileName = idEps.replace("?", "").replace(":", "")
					.replace("-", "");
			fileName0 = cleanFileName.split("\\. by");
			int endString = fileName0[0].length();
			if (fileName0[0].indexOf("[") > 0) {
				endString = fileName0[0].indexOf("[");
			}
			if (fileName0.length < 2) {
				fileName = fileName0[0].substring(0, endString);
			} else {
				fileName = fileName0[0];
			}

			String extension = "";
			if (link.contains(".zip")) {
				extension = "zip";
			} else if (MimeTypeMap.getFileExtensionFromUrl(link) != "") {
				extension = MimeTypeMap.getFileExtensionFromUrl(link);
			} else {
				//extension = "epub";
			}
			FileOutputStream f = new FileOutputStream(new File(
					extStorageDirectory+"/"+idComic, fileName.replaceAll("[^a-zA-Z0-9]",
							" ").trim()
							+ "." + extension));

			fileDownload = new File(
					extStorageDirectory+"/"+idComic, fileName.replaceAll("[^a-zA-Z0-9]",
							" ").trim()
							+ "." + extension);
			InputStream in = c.getInputStream();

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = in.read(buffer)) > 0) {
				f.write(buffer, 0, len1);
			}

			// progress download
			int lenghtOfFile = c.getContentLength();
			InputStream input = new BufferedInputStream(u.openStream());
			byte data[] = new byte[1024];
			int total = 0;
	
			EpisodeProgress down = new EpisodeProgress();
			final int down_size = down.arrList.size();
			
			int id = 0;
			
			if(down_size>0){

				for(int a = 0 ; a < down_size ; a++){

					String down_id = down.arrList.get(a).getId();
					
					if(idEps.equals(down_id)){
						id = a;
						//down.arrList.get(a).setValue(false);

						//Log.e("status akhir", ""+down.arrList.get(a).getValue());
					}
				}
			}
			
			//save total download
			down.arrList.get(id).setTotal(Math.round(lenghtOfFile/1000));
			
			Log.e("value : ", ""+down.arrList.get(id).getTotal() );
	
			while ((count = input.read(data)) != -1) {
				total += count;
//              publishProgress((int)((total*100)/lenghtOfFile));
//              if(EpisodeListAdapter.pbar != null){
//                    EpisodeListAdapter.pbar.setProgress((int)((total*100)/lenghtOfFile));
//              }
				value = (int)((total*100)/lenghtOfFile);
				if(opl != null)
				{
					opl.doProgress(value, Math.round(total/1000), Math.round(lenghtOfFile/1000), mEpisode);
				}

				f.write(data, 0, count);
				System.out.println(""+total);
				Log.e("sad", ""+(total*100)/lenghtOfFile);
				down.arrList.get(id).setProgress((total*100)/lenghtOfFile);

				down.arrList.get(id).setKb(Math.round(total/1000));
			}

			f.flush();
			f.close();
			in.close();
			Log.e("status download : ", "Download completed. You may access the ebook from My Downloads.");
			down.arrList.get(id).setValue(false);
//			UserEpsLite.insertUserEps(context, idEps);

//			if(ComicLite.availComic(context, idComic)!=true){
//				ComicLite.insertComic(context, idComic);
//			}
			//check sqlite episode
//			if(EpisodeLite.availEps(context, idEps)!=true){
//				EpisodeLite.insertEpisode(context, idEps);
//			}

			flag = true;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("e : ", e.toString());
			FileExplorer.deleteFile(fileDownload);
			down.arrList.get(id).setValue(false);
			flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FileExplorer.deleteFile(fileDownload);
			down.arrList.get(id).setValue(false);
			Log.e("e : ", e.toString());
			flag = true;
		}
		return flag;
	}

	public interface onProgressListener
	{
		public abstract void doProgress(int value, int kb, int total, String episode);
		public abstract void doFinish();
	}

	public void setOnProgressListener(onProgressListener listener)
	{
		opl = listener;
	}
}
