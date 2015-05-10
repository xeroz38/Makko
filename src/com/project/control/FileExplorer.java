package com.project.control;

import java.io.File;
import java.util.regex.PatternSyntaxException;

import android.os.Environment;

public class FileExplorer {
	// private List<String> item = null;

	// private List<String> path = null;

	// private String root="/";

	// private TextView myPath;
	// main path makko
	public static final String extStorageDirectory = Environment.getExternalStorageDirectory().toString() + "/.Makko";

	public static void createMainPath() {

	}

	// cek episode from device folder
	public static void checkEps(String idComic, String idEpisodes) {

	}

	public static boolean getFileNames(final String folder, final String idEps)
			throws PatternSyntaxException {
		// Log.e("id eps : ", idEps);
		// Log.e("folder : ", folder);
		boolean existFile = false;
		// ArrayList<String> myData = new ArrayList<String>();

		File fileDir = new File(folder);
		if (!fileDir.exists() || !fileDir.isDirectory()) {
			// return null;
			// Log.e("stat folder : ", "kosong");
			return existFile = false;
		}

		String[] files = fileDir.list();

		if (files.length == 0) {
			// return null;
			// Log.e("length size : ", ""+files.length);
			return existFile = false;
		}
		for (int i = 0; i < files.length; i++) {
			// Log.e("file : ", ""+files[i].toString());
			if (files[i].toString().equals(idEps)) {
				existFile = true;
			}
			/*
			 * if(fileNameFilterPattern == null ||
			 * files[i].matches(fileNameFilterPattern)) myData.add(files[i]);
			 */
		}
		// if (myData.size() == 0)
		// return null;
		return existFile;

		/*
		 * if (sort != 0) { // Collections.sort(myData,
		 * String.CASE_INSENSITIVE_ORDER); if (sort < 0) //
		 * Collections.reverse(myData); }
		 * 
		 * return myData;
		 */
	}
	public static void deleteFile(File fileDownload){
		try {
			fileDownload.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
