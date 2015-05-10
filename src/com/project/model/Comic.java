package com.project.model;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class Comic {

	public static final String url_list_comic ="http://android.makko.co/comic.php?request=listComic";
	public static final String url_detail_comic ="http://android.makko.co/comic.php?request=comicDetails&id=";
	public static final String url_head_comic ="http://android.makko.co/comic.php?request=headComic&id=";
	public static final String url_favorite_comic ="http://android.makko.co/comic.php?request=favoriteComic";
	public static final String url_count_comic ="http://android.makko.co/comic.php?request=cekCountComic";

	private String idComic;
	private String title;
	private String genre;
	private String writter;
	private String artist;
	private String date;
	private String info;
	private String image;
	private String count;
	private String head;

	
	public static Comic[] getAllComic(String url){

		Comic[] comicCollection= new Comic[0];// = new Comic[];

		//Log.d("url", url);
		try{
			URL urlLink = new URL(url);


			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(urlLink.openStream()));

			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("comic");

			//comicLenght=nodeList.getLength();

			comicCollection = new Comic[nodeList.getLength()];

			//txtWritter.setText("::"+nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				//Log.d("leght", ""+nodeList.getLength());
				Comic aComic = new Comic();

				Node node = nodeList.item(i);

				NodeList properties = node.getChildNodes();

				for (int j=0;j<properties.getLength();j++){

					Node property = properties.item(j);
					//name property
					String name = property.getNodeName();

					if (name.equalsIgnoreCase("idComic")){
						aComic.setId(property.getFirstChild().getNodeValue());
						//Log.d("id comic : ", aComic.getId());
					} else if (name.equalsIgnoreCase("judulComic")){
						aComic.setTitle(property.getFirstChild().getNodeValue());
						//Log.d("judul comic : ", aComic.getTitle());
					} else if (name.equalsIgnoreCase("genre")){
						aComic.setGenre(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("writter")){
						aComic.setWritter(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("artist")){
						aComic.setArtist(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("publishedDate")){
						aComic.setDate(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("comicInfo")){
						aComic.setInfo(property.getFirstChild().getNodeValue());
						//Log.d("comic info : ", aComic.getInfo());
					} else if (name.equalsIgnoreCase("imageComic")){
						aComic.setImage(property.getFirstChild().getNodeValue());
						//Log.d("image : ", aComic.getImage());
					} else if (name.equalsIgnoreCase("count")){
						aComic.setCount(property.getFirstChild().getNodeValue());
						//Log.e("count :", aComic.getCount());
					} else if (name.equalsIgnoreCase("headComic")){
						aComic.setHead(property.getFirstChild().getNodeValue());
						//Log.e("head :", aComic.getHead());
					} 
					comicCollection[i] = aComic;
				}


			}

		}catch (Exception e) {
			// TODO: handle exception
		}

		return comicCollection;
	}


	public String getId(){

		return idComic;
	}

	public void setId(String id){
		this.idComic=id;

	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title=title;
	}

	public String getGenre(){
		return genre;
	}

	public void setGenre(String genre){
		this.genre=genre;
	}

	public String getWritter(){
		return writter;
	}

	public void setWritter(String writter){
		this.writter=writter;
	}

	public String getArtist(){
		return artist;
	}

	public void setArtist(String artist){
		this.artist=artist;
	}

	public String getDate(){
		return date;
	}

	public void setDate(String date){
		this.date=date;
	}

	public String getInfo(){
		return info;
	}

	public void setInfo(String info){
		this.info=info;
	}

	public String getImage(){
		return image;
	}

	public void setImage(String image){
		this.image=image;
	}
	public String getCount(){
		return count;
	}

	public void setCount(String count){
		this.count=count;
	}	
	public String getHead(){
		return head;
	}

	public void setHead(String head){
		this.head=head;
	}	
}
