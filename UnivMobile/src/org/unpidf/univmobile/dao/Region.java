package org.unpidf.univmobile.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Region implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4380315275015749480L;
	private String id;
	private String label;
	private String url;
	private String urlProd;
	private List<University> listUniversity;
	
	public Region(JSONObject json){
		this.id = json.optString("id");
		this.label = json.optString("label");
		this.url = json.optString("url");
		this.urlProd = json.optString("url_prod");
	}
	
	public void setListUniversity(List<University> listUniversity) {
		this.listUniversity = listUniversity;
	}
	
	public List<University> getListUniversity() {
		if(listUniversity == null){
			return new ArrayList<University>();
		}
		return listUniversity;
	}
	
	public String getId() {
		return id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getUrlProd() {
		return urlProd;
	}
}
