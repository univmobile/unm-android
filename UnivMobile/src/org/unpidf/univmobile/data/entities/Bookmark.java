package org.unpidf.univmobile.data.entities;

/**
 * Created by rviewniverse on 2015-02-24.
 */
public class Bookmark {

	private int id;
	private String poiUrl;
	private Poi poi;

	public int getId() {
		return id;
	}

	public Poi getPoi() {
		return poi;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPoiUrl() {
		return poiUrl;
	}

	public void setPoiUrl(String poiUrl) {
		this.poiUrl = poiUrl;
	}

	public void setPoi(Poi poi) {
		this.poi = poi;
	}
}
