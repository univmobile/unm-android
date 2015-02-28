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

	private static final String BOOKMARKS = "bookmarks?user=%s";
	private String mUserId;


	public ReadBookmarksOperation(Context c, OperationListener listener, String userID) {
		super(c, listener);
		mUserId = userID;
	}

	@Override
	protected List<Bookmark> parse(JSONObject json) throws JSONException {
		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray bookmarksJson = _embedded.getJSONArray("bookmarks");

		List<Bookmark> bookmarksList = new ArrayList<Bookmark>();


		for (int i = 0; i < bookmarksJson.length(); i++) {
			JSONObject bookmarkJson = bookmarksJson.getJSONObject(i);

			Bookmark bookmark = new Gson().fromJson(bookmarkJson.toString(), Bookmark.class);

			bookmarksList.add(bookmark);
		}
		return bookmarksList;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url =  BASE_URL_API + String.format(BOOKMARKS, mUserId);

		if (page != 0) {
			url += "?page=" + page;
		}
		return url;

	}

	@Override
	protected List<Bookmark> combine(List<Bookmark> newData, List<Bookmark> oldData) {
		oldData.addAll(newData);
		return oldData;
	}

	@Override
	protected boolean shouldBePaged() {
		return true;
	}


}
