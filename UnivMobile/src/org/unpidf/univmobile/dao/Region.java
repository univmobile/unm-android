package org.unpidf.univmobile.dao;

import static org.unpidf.univmobile.dao.JSONEnabled.optString;

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
	private List<University> listUniversities;
	
	public Region(JSONObject json){
		this.id = optString(json, "id");
		this.label = optString(json, "label");
		this.url = optString(json, "url");
		this.urlProd = optString(json, "url_prod");
	}
	
	public void setListUniversities(List<University> listUniversities) {
		this.listUniversities = listUniversities;
	}
	
	public List<University> getListUniversities() {
		if(listUniversities == null){
			return new ArrayList<University>();
		}
		return listUniversities;
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
