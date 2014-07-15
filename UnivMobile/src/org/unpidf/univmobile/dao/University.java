package org.unpidf.univmobile.dao;

import java.io.Serializable;

public class University implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4380315275015749480L;
	private String name;
	
	public University(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
