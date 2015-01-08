package org.unpidf.univmobile.data.entities;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 2015-01-06.
 */
public class Region implements Parcelable {
	private int id;
	private String name;
	private String label;
	private String url;
	private List<University> universities;
	private boolean allowBonplans;


	public Region(Parcel in) {
		id = in.readInt();
		name = in.readString();
		label = in.readString();
		url = in.readString();
		universities = new ArrayList<University>();
		in.readList(universities, University.class.getClassLoader());
		allowBonplans = in.readInt() == 1 ? true : false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(label);
		dest.writeString(url);
		dest.writeList(universities);
		dest.writeInt(allowBonplans ? 1 : 0);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Region createFromParcel(Parcel in) {
			return new Region(in);
		}

		public Region[] newArray(int size) {
			return new Region[size];
		}
	};

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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<University> getUniversities() {
		return universities;
	}

	public void setUniversities(List<University> universities) {
		this.universities = universities;
	}

	public boolean isAllowBonplans() {
		return allowBonplans;
	}

	public void setAllowBonplans(boolean allowBonplans) {
		this.allowBonplans = allowBonplans;
	}
}
