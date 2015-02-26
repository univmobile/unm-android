package org.unpidf.univmobile.data.entities;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-01-06.
 */
public class Region {
	private int id;
	private String name;
	private String universityUrl;

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

	public String getUniversityUrl() {
		return universityUrl;
	}

	public void setUniversityUrl(String universityUrl) {
		this.universityUrl = universityUrl;
	}
}
