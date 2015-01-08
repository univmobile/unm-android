package org.unpidf.univmobile.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Rokas on 2015-01-06.
 */
public class University implements Parcelable {

	private int id;
	private String title;

	public University(Parcel in) {
		id = in.readInt();
		title = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
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
}
