package org.unpidf.univmobile.data.entities;

import java.util.List;

/**
 * Created by Rokas on 2015-01-20.
 */
public class NavigationMenu {

	private int id;
	private String name;
	private int imageId;
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
}
