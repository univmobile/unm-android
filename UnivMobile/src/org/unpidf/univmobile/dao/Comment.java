package org.unpidf.univmobile.dao;

import static org.unpidf.univmobile.dao.JSONEnabled.optString;

import java.io.Serializable;

import org.json.JSONObject;

public class Comment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7776730909303931351L;
	private String userName;
	private String displayName;
	private String text;

	public Comment(JSONObject jsonOnject) {
		this.userName = optString(jsonOnject.optJSONObject("author"),
				"username");
		this.displayName = optString(jsonOnject.optJSONObject("author"),
				"displayName");
		this.text = optString(jsonOnject, "text");
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getText() {
		return text;
	}

	public String getUserName() {
		return userName;
	}
}
