package com.project.control;

import java.util.ArrayList;

import com.project.adapter.DetailEpisodeListAdapter;

import android.R.bool;


public class EpisodeProgress{
	
	public String id;
	public boolean value = false;
	public int progress;
	private int kb;
	private int total;
	
	public static ArrayList<EpisodeProgress> arrList = new ArrayList<EpisodeProgress>();
	
	//public static int[] _value=new int[500];
	
	public static void addProgress(String index, Boolean value, int progress, int kb, int total){		
		arrList.add(getEpisodeProgress(index, value, progress, kb, total));
	}
	
	private static EpisodeProgress getEpisodeProgress(String id, Boolean value, int progress, int kb, int total){
		EpisodeProgress tempObj=new EpisodeProgress();
		tempObj.setId(id);
		tempObj.setValue(value);
		tempObj.setProgress(progress);
		tempObj.setKb(kb);
		tempObj.setTotal(total);
//		tempObj.index=index;
//		tempObj.value=value;
//		arrList.add(tempObj);
		return tempObj;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getKb() {
		return kb;
	}

	public void setKb(int kb) {
		this.kb = kb;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
}
