package org.unpidf.univmobile.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 2015-01-06.
 */
public class Regions implements Parcelable {

	private String url;
	private List<Region> regions;

	public Regions(Parcel in) {
		url = in.readString();
		regions = new ArrayList<Region>();
		in.readList(regions, Region.class.getClassLoader());

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(url);
		dest.writeList(regions);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Regions createFromParcel(Parcel in) {
			return new Regions(in);
		}

		public Regions[] newArray(int size) {
			return new Regions[size];
		}
	};

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Region> getRegions() {
		return regions;
	}

	public void setRegions(List<Region> regions) {
		this.regions = regions;
	}

	public Region getRegionByID(int id) {
		for (Region region : regions) {
			if (region.getId() == id) {
				return region;
			}
		}
		return null;
	}
}
