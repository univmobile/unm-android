package org.unpidf.univmobile.dao;

import java.io.Serializable;

import org.json.JSONObject;

public class Poi implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4380315275015749480L;
	
	private String id;
	private String title;
	private String adress;
	private String phone;
	private Double latitude;
	private Double longitude;
	private String commentUrl;
	private String webUrl;
	
	public Poi(JSONObject json){
		this.id = json.optString("id");
		this.title = json.optString("name");
		String adress = json.optString("address");
		this.adress = ((adress == null || adress.equals("null")) ? null : adress);
		String phone = json.optString("phone");
		this.phone = ((phone == null || phone.equals("null")) ? null : phone);
		this.latitude = json.optDouble("lat");
		this.longitude = json.optDouble("lng");
		this.commentUrl = json.optJSONObject("comments").optString("url");
		String webUrl = json.optString("url");
		this.webUrl = ((webUrl == null || webUrl.equals("null")) ? null : webUrl);
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAdress() {
		return adress;
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
	
	public String getCommentUrl() {
		return commentUrl;
	}
	
	public String getWebUrl() {
		return webUrl;
	}

}
