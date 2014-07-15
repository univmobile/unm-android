package org.unpidf.univmobile.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Region implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4380315275015749480L;
	private String name;
	private List<University> listUniversity;
	
	public Region(String name){
		this.name = name;
	}
	
	public void setListUniversity(List<University> listUniversity) {
		this.listUniversity = listUniversity;
	}
	
	public String getName() {
		return name;
	}
	
	public List<University> getListUniversity() {
		if(listUniversity == null){
			return new ArrayList<University>();
		}
		return listUniversity;
	}
}
