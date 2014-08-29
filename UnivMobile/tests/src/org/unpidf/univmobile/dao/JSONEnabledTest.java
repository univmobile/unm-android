package org.unpidf.univmobile.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import org.json.JSONObject;

import static org.unpidf.univmobile.dao.JSONEnabled.optString;

public class JSONEnabledTest {

	@Test
	public void testOptString_nullJSONObject() throws Exception {
		
		final JSONObject json = null;
		
		assertNull(optString(json, "name"));
	}

	@Test
	public void testOptString_emptyJSONObject() throws Exception {
		
		final JSONObject json = new JSONObject();
				
		assertNull(optString(json, "name"));
	}

	@Test
	public void testOptString_nullProperty() throws Exception {
		
		final JSONObject json = new JSONObject().put("name", (String) null);
				
		assertNull(optString(json, "name"));
	}

	@Test
	public void testOptString_nonNullProperty() throws Exception {
		
		final JSONObject json = new JSONObject().put("name", "Emmanuel");
				
		assertEquals("Emmanuel", optString(json, "name"));
	}
}
