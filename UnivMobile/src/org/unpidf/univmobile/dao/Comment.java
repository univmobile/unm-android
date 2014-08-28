package org.unpidf.univmobile.dao;

import java.io.Serializable;

import org.json.JSONObject;

public class Comment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7776730909303931351L;
	private String userName;
	private String displayName;
	private String text;
	
	public Comment(JSONObject jsonOnject){
		this.userName = jsonOnject.optJSONObject("author").optString("username");
		this.displayName = jsonOnject.optJSONObject("author").optString("displayName");
		this.text = jsonOnject.optString("text");
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getText() {
		return text;
	}
	
	public String getUserName() {
		return userName;
	}
}
