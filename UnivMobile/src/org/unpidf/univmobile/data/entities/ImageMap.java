package org.unpidf.univmobile.data.entities;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class ImageMap {
	int id;
	String name;
	String url;
	String description;
	Bitmap image;
	boolean active;

	List<Poi> pois;
	String poisUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Poi> getPois() {
		return pois;
	}

	public void setPois(List<Poi> pois) {
		this.pois = pois;
	}

	public String getPoisUrl() {
		return poisUrl;
	}

	public void setPoisUrl(String poisUrl) {
		this.poisUrl = poisUrl;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
}
