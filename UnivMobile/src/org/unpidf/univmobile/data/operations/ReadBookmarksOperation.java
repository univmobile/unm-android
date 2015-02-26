package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Bookmark;
import org.unpidf.univmobile.data.entities.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class ReadBookmarksOperation extends AbsOperation<List<Bookmark>> {

	private static final String BOOKMARKS = "users/%s/bookmarks";
	private String mUserId;

	public ReadBookmarksOperation(Context c, OperationListener listener, String userID) {
		super(c, listener);
		mUserId = userID;
	}

	@Override
	protected List<Bookmark> parse(JSONObject json) throws JSONException {
		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray categoriesJson = _embedded.getJSONArray("bookmarks");

		List<Bookmark> bookmarksList = new ArrayList<Bookmark>();


		for (int i = 0; i < categoriesJson.length(); i++) {
			JSONObject categoryJson = categoriesJson.getJSONObject(i);
			Bookmark b = new Bookmark();
			int id = categoryJson.getInt("id");
			b.setId(id);

			JSONObject links = categoryJson.getJSONObject("_links");
			JSONObject self = links.getJSONObject("poi");
			b.setPoiUrl(self.getString("href"));

			bookmarksList.add(b);
		}
		return bookmarksList;
	}

	@Override
	protected String getOperationUrl(int page) {
		return BASE_URL_API + String.format(BOOKMARKS, mUserId);
	}

}
