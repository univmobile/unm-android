package org.unpidf.univmobile.data.entities;

/**
 * Created by rviewniverse on 2015-02-24.
 */
public class Bookmark {

	private int id;
	private int poiId;
	private String poiName;
	private int poiCategoryId;
	private int poiUniversityId;
	private int rootCategoryId;

	public int getPoiId() {
		return poiId;
	}

	public void setPoiId(int poiId) {
		this.poiId = poiId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public int getPoiCategoryId() {
		return poiCategoryId;
	}

	public void setPoiCategoryId(int poiCategoryId) {
		this.poiCategoryId = poiCategoryId;
	}

	public int getPoiUniversityId() {
		return poiUniversityId;
	}

	public void setPoiUniversityId(int poiUniversityId) {
		this.poiUniversityId = poiUniversityId;
	}

	public int getRootCategoryId() {
		return rootCategoryId;
	}

	public void setRootCategoryId(int rootCategoryId) {
		this.rootCategoryId = rootCategoryId;
	}
}
