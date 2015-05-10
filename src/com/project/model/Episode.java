package com.project.model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

public class Episode {

	public static final String url_fresh_episode ="http://android.makko.co/episode.php?request=freshEpisode";

	public static final String url_list_episode ="http://android.makko.co/episode.php?request=listEpisode&id=";

	public static final String url_down_link ="http://android.makko.co/episode.php?request=downLink&id=";

	public static final String url_detail_episode ="http://android.makko.co/episode.php?request=detailEpisode&id=";
	public static final String url_share="http://android.makko.co/episode.php?request=share&id=";

	String id;
	String judul;
	String release;
	String idComic;
	String judulComic;
	String icon;
	String fb_share;
	String tw_share;
	private String downLink;

	public boolean isDownloading;
    public String mProgressInButton;
    public String mProgressInBar;
    public int mProgressValue;
    public boolean mDownloading;


    public static Episode[] getEpisode(String url){

		Episode[] episodeCollection= new Episode[0];

		try{
			URL urlLink = new URL(url);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(urlLink.openStream()));
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("episode");


			episodeCollection = new Episode[nodeList.getLength()];
			//Log.e("episode node lenght:", ""+nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {

				Episode aEpisode = new Episode();

				Node node = nodeList.item(i);

				NodeList properties = node.getChildNodes();

				//Log.e("node list lenght:", ""+properties.getLength());

				for (int j=0;j<properties.getLength();j++){

					Node property = properties.item(j);
					//name property
					String name = property.getNodeName();


					if (name.equalsIgnoreCase("idEpisode")){
						aEpisode.setId(property.getFirstChild().getNodeValue());
						//Log.e("id episode :", aEpisode.getId());
					} else if (name.equalsIgnoreCase("judulEpisode")){
						aEpisode.setJudul(property.getFirstChild().getNodeValue());
						//Log.e("judul episode :", aEpisode.getJudul());
					} else if (name.equalsIgnoreCase("release")){
						aEpisode.setRelease(property.getFirstChild().getNodeValue());
						//Log.e("release episode :", aEpisode.getRelease());
					} else if (name.equalsIgnoreCase("link")){
						aEpisode.setDownLink(property.getFirstChild().getNodeValue());
						//Log.e("icon comic :", aEpisode.getIcon());
					} else if (name.equalsIgnoreCase("idComic")){
						aEpisode.setIdComic(property.getFirstChild().getNodeValue());
						//Log.e("id comic :", aEpisode.getIdComic());
					} else if (name.equalsIgnoreCase("judulComic")){
						aEpisode.setJudulComic(property.getFirstChild().getNodeValue());
						//Log.e("judul comic :", aEpisode.getJudulComic());
					} else if (name.equalsIgnoreCase("iconComic")){
						aEpisode.setIcon(property.getFirstChild().getNodeValue());
						//Log.e("icon comic :", aEpisode.getIcon());
					} else if (name.equalsIgnoreCase("fb_share")){
						aEpisode.setFb_share(property.getFirstChild().getNodeValue());
						//Log.e("icon comic :", aEpisode.getIcon());
					} else if (name.equalsIgnoreCase("tw_share")){
						aEpisode.setTw_share(property.getFirstChild().getNodeValue());
						//Log.e("icon comic :", aEpisode.getIcon());
					}
					episodeCollection[i] = aEpisode;
				}

			}

		}catch (Exception e) {
			// TODO: handle exception
		}

		return episodeCollection;
	}

	public String getId(){

		return id;
	}

	public void setId(String id){
		this.id=id;
	}

	public String getIdComic(){

		return idComic;
	}

	public void setIdComic(String idComic){
		this.idComic=idComic;
	}

	public String getJudul(){

		return judul;
	}

	public void setJudul(String judul){
		this.judul=judul;
	}

	public String getJudulComic(){

		return judulComic;
	}

	public void setJudulComic(String judulComic){
		this.judulComic=judulComic;
	}

	public String getRelease(){
		return release;
	}

	public void setRelease(String release){
		this.release = release;
	}

	public String getIcon(){
		return icon;
	}

	public void setIcon(String icon){
		this.icon = icon;
	}



	public String getDownLink() {
		return downLink;
	}



	public void setDownLink(String downLink) {
		this.downLink = downLink;
	}

	public String getFb_share() {
		return fb_share;
	}

	public void setFb_share(String fb_share) {
		this.fb_share = fb_share;
	}

	public String getTw_share() {
		return tw_share;
	}

	public void setTw_share(String tw_share) {
		this.tw_share = tw_share;
	}

}
