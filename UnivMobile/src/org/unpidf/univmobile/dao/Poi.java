package org.unpidf.univmobile.dao;

import java.io.Serializable;

import org.json.JSONObject;

import static org.unpidf.univmobile.dao.JSONEnabled.optString;

public class Poi implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4380315275015749480L;
	
	private final String id;
	private final String title;
	private final String address;
	private final String phone;
	private final Double latitude;
	private final Double longitude;
	private final String commentsUrl;
	private final String webUrl;
	
	public Poi(JSONObject json){
		this.id = optString(json, "id");
		this.title = optString(json, "name");
		this.address = optString(json, "address");
		this.phone = optString(json,"phone");
		this.latitude = json.optDouble("lat");
		this.longitude = json.optDouble("lng");
		this.commentsUrl = optString(json.optJSONObject("comments"), "url");
		this.webUrl = optString(json,"url");
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getPhone() {
		return phone;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}
	
	public String getCommentsUrl() {
		return commentsUrl;
	}
	
	public String getWebUrl() {
		return webUrl;
	}

}
