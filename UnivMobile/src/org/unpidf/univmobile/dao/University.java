package org.unpidf.univmobile.dao;

import static org.unpidf.univmobile.dao.JSONEnabled.optString;

import java.io.Serializable;

import org.json.JSONObject;

public class University implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4380315275015749480L;
	private String id;
	private String title;
	private String json;
	private String shibbolethIdentityProvider;

	public University(){
	}

	public University(JSONObject jsonObject){
		this.json = jsonObject.toString();
		this.id = optString(jsonObject, "id");
		this.title = optString(jsonObject, "title");
		this.shibbolethIdentityProvider = optString(jsonObject.optJSONObject("shibboleth"), "identityProvider");
	}
	
	public String getJson() {
		return json;
	}

	public String getTitle() {
		return title;
	}

	public String getId() {
		return id;
	}
	
	public String getShibbolethIdentityProvider() {
		return shibbolethIdentityProvider;
	}

}
