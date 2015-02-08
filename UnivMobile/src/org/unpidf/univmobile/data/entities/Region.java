package org.unpidf.univmobile.data.entities;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 2015-01-06.
 */
public class Region {
	private String name;
	private String universityUrl;

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
