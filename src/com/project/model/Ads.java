package com.project.model;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class Ads {

	public static final String url_foot_ads ="http://android.makko.co/banner_xml.php?request=urlAds";
	public static final String url_pop_ads = "http://android.makko.co/banner_xml.php?request=urlAdsPopup";

	private String ads_link;

	public static Ads[] getAds(String url){

		Ads[] adsCollection= new Ads[0];// = new Comic[];

		//Log.d("url", url);
		try{
			URL urlLink = new URL(url);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(urlLink.openStream()));

			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("ad");

			//comicLenght=nodeList.getLength();

			adsCollection = new Ads[nodeList.getLength()];

			//txtWritter.setText("::"+nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				//Log.d("leght", ""+nodeList.getLength());
				Ads ads = new Ads();

				Node node = nodeList.item(i);

				NodeList properties = node.getChildNodes();

				for (int j=0;j<properties.getLength();j++){

					Node property = properties.item(j);
					//name property
					String name = property.getNodeName();

					if (name.equalsIgnoreCase("banner")){
						ads.setBanner(property.getFirstChild().getNodeValue());
						//Log.d("id comic : ", aComic.getId());
					}
					adsCollection[i] = ads;
				}


			}

		}catch (Exception e) {
			// TODO: handle exception
		}

		return adsCollection;
	}


	public String getBanner(){
		return ads_link;
	}

	public void setBanner(String id){
		this.ads_link=id;
	}
}
