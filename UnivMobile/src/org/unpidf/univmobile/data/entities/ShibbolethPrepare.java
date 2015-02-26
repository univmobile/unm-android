package org.unpidf.univmobile.data.entities;

/**
 * Created by rviewniverse on 2015-02-13.
 */
public class ShibbolethPrepare {

	private String key;
	private String token;

	public ShibbolethPrepare(String key, String token) {
		this.key = key;
		this.token = token;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
