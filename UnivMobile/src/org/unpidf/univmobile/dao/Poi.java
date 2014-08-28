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
	
	public Poi(JSONObject json){
		this.id = json.optString("id");
		this.title = json.optString("name");
		this.adress = json.optString("address", "");
		this.phone = json.optString("phone");
		this.latitude = json.optDouble("lat");
		this.longitude = json.optDouble("lng");
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

}
