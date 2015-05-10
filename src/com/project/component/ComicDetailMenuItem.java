package com.project.component;

public class ComicDetailMenuItem {

	private String mCaption = null;
	private int mImageResourceId = -1;
	private int mId = -1;

	public void setCaption(String caption) { 
		mCaption =caption;	
	}

	public String getCaption() { 
		return mCaption; 
	}

	public void setImageResourceId(int imageResourceId) { 
		mImageResourceId = imageResourceId; 
	}

	public int getImageResourceId() { 
		return mImageResourceId;	
	}

	public void setId(int id) { 
		mId = id; 
	}

	public int getId() { 
		return mId; 
	}

}
