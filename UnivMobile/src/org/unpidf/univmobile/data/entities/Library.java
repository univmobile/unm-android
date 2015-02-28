package org.unpidf.univmobile.data.entities;

/**
 * Created by Rokas on 2015-02-27.
 */
public class Library {

	private int id;
	private int universityId;
	private boolean iconRuedesfacs;
	private int poiId;
	private String poiName;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUniversityId() {
		return universityId;
	}

	public void setUniversityId(int universityId) {
		this.universityId = universityId;
	}

	public boolean isIconRuedesfacs() {
		return iconRuedesfacs;
	}

	public void setIconRuedesfacs(boolean iconRuedesfacs) {
		this.iconRuedesfacs = iconRuedesfacs;
	}

	public int getPoiId() {
		return poiId;
	}

	public void setPoiId(int poiId) {
		this.poiId = poiId;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}
}
