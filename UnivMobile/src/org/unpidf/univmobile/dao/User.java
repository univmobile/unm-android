package org.unpidf.univmobile.dao;

import org.json.JSONObject;

public class User {
	private String id;
	private String uid;
	private String displayName;
	private String mail;

	public User(JSONObject json) {
		this.id = json.optString("id");
		JSONObject userJson = json.optJSONObject("user");
		if(userJson != null){
			this.displayName = userJson.optString("displayName");
			this.mail = userJson.optString("mail");
			this.uid = userJson.optString("uid");
		}
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getUid() {
		return uid;
	}

	public String getId() {
		return id;
	}

	public String getMail() {
		return mail;
	}
}
