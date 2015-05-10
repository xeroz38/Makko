package com.project.model;

import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

public class User {
	
	//link user check
	public static final String check_login ="http://android.makko.co/user.php?";
	
	private String result;

	public User(){
		
	}
		
	public static boolean check_user(String url){
		boolean check_stat = false;

		Log.e("url : ", url);
		
		try{
			URL urlLink = new URL(url);


			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(urlLink.openStream()));

			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("user");

			for (int i = 0; i < nodeList.getLength(); i++) {
				
				Log.d("leght", ""+nodeList.getLength());
				
				Node node = nodeList.item(i);

				NodeList properties = node.getChildNodes();

				for (int j=0;j<properties.getLength();j++){
					Node property = properties.item(j);
					//name property
					String name = property.getNodeName();

					Log.d("name : ", name);
					
					if (name.equalsIgnoreCase("result_member")){
						//check stat login
						if((property.getFirstChild().getNodeValue()).equals("true")){
							
							check_stat = true;
						
						}
					
					}

				}


			}

		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return check_stat;
		
	}
	
	String getResult() {
		return result;
	}

	void setResult(String result) {
		this.result = result;
	}
	
	
	
}
