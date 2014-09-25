package org.unpidf.univmobile.dao;

import org.json.JSONObject;

public class MapInfo {
	private double lat;
	private double lng;
	private int zoom;
	
	public MapInfo(JSONObject json) {
		this.lat = json.optDouble("lat");
		this.lng = json.optDouble("lng");
		this.zoom = json.optInt("zoom");
	}

	public double getLat() {
		return lat;
	}
	
	public double getLng() {
		return lng;
	}
	
	public int getZoom() {
		return zoom;
	}
}
