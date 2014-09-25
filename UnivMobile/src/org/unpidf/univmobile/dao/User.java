package org.unpidf.univmobile.dao;

import org.json.JSONObject;

public class User {
	private String id;
	private String displayName;
	private String mail;
	
	public User(JSONObject json) {
		this.id = json.optString("id");
		this.displayName = json.optString("displayName");
		this.mail = json.optString("mail");
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getId() {
		return id;
	}
	
	public String getMail() {
		return mail;
	}
}
