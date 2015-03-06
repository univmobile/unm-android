package org.unpidf.univmobile.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by rviewniverse on 2015-01-06.
 */
public class University implements Parcelable {

	private int id;
	private String title;
	private String self;
	private String regionName;
	private String mobileShibbolethUrl;

	public University() {

	}

	public University(int id, String title, String self, String regionName, String shibboleth) {
		this.id = id;
		this.title = title;
		this.self = self;
		this.regionName = regionName;
		mobileShibbolethUrl = shibboleth;
	}

	public University(Parcel in) {
		id = in.readInt();
		title = in.readString();
		self = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(self);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public University createFromParcel(Parcel in) {
			return new University(in);
		}

		public University[] newArray(int size) {
			return new University[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getMobileShibbolethUrl() {
		return mobileShibbolethUrl;
	}

	public void setMobileShibbolethUrl(String mobileShibbolethUrl) {
		this.mobileShibbolethUrl = mobileShibbolethUrl;
	}
}
