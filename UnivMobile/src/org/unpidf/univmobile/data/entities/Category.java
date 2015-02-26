package org.unpidf.univmobile.data.entities;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-08.
 */
public class Category {
	private int id;
	private String name;
	private String description;
	private boolean active;
	private String apiParisId;
	private String activeIconUrl;
	private String inactiveIconUrl;
	private String markerIconUrl;
	private List<Integer> legacyIds;
	private String self;

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

	public String getApiParisId() {
		return apiParisId;
	}

	public void setApiParisId(String apiParisId) {
		this.apiParisId = apiParisId;
	}

	public String getActiveIconUrl() {
		return activeIconUrl;
	}

	public void setActiveIconUrl(String activeIconUrl) {
		this.activeIconUrl = activeIconUrl;
	}

	public String getInactiveIconUrl() {
		return inactiveIconUrl;
	}

	public void setInactiveIconUrl(String inactiveIconUrl) {
		this.inactiveIconUrl = inactiveIconUrl;
	}

	public String getMarkerIconUrl() {
		return markerIconUrl;
	}

	public void setMarkerIconUrl(String markerIconUrl) {
		this.markerIconUrl = markerIconUrl;
	}

	public List<Integer> getLegacyIds() {
		return legacyIds;
	}

	public void setLegacyIds(List<Integer> legacyIds) {
		this.legacyIds = legacyIds;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}
}
