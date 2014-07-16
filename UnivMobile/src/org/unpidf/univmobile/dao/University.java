package org.unpidf.univmobile.dao;

import java.io.Serializable;

import org.json.JSONObject;

public class University implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4380315275015749480L;
	private String id;
	private String title;
	
	public University(){
	}
	
	public University(JSONObject jsonObject){
		this.id = jsonObject.optString("id");
		this.title = jsonObject.optString("title");
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getId() {
		return id;
	}
	
}
