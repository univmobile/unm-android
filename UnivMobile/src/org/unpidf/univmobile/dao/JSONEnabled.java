package org.unpidf.univmobile.dao;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * Utilities for JSON parsing.
 */
public abstract class JSONEnabled {

	/**
	 * Circumvent a bug from org.json: If a String field is null,
	 * {@link JSONObject#optString(String)} returns the
	 * non-null <tt>"null"</tt> String, rather than a <tt>null</tt> value.
	 * This implementation returns <tt>null</tt> when the field is null.
     * <br>
     * Also, the <tt>jsonObject</tt> parameter may be passed <tt>null</tt>, and the method
     * will return <tt>null.</tt>
	 */
	public static String optString(final JSONObject jsonObject, final String fieldName) {
		
		if (jsonObject == null || jsonObject.isNull(fieldName)) {
			
			return null;
		}
		
		return jsonObject.optString(fieldName);
	}
}
