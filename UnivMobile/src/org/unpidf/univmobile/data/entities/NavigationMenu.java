package org.unpidf.univmobile.data.entities;

import java.util.List;

/**
 * Created by rviewniverse on 2015-01-20.
 */
public class NavigationMenu implements Comparable<NavigationMenu>{

	private int id;
	private int imageId;
	private String name;
	private boolean active;
	private int ordinal;
	private String url;
	private String content;
	private String grouping;
	private int universityId;
	private List<NavigationMenu> childs;

	public NavigationMenu(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public NavigationMenu(int id, String name, int imageId, List<NavigationMenu> childs) {
		this.id = id;
		this.name = name;
		this.imageId = imageId;
		this.childs = childs;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public List<NavigationMenu> getChilds() {
		return childs;
	}

	public void setChilds(List<NavigationMenu> childs) {
		this.childs = childs;
	}

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public int getUniversityId() {
		return universityId;
	}

	public void setUniversityId(int universityId) {
		this.universityId = universityId;
	}

	@Override
	public int compareTo(NavigationMenu another) {
		int lhs = this.ordinal;
		int rhs = another.getOrdinal();
		return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
	}

}
