package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Comment;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.models.GeoDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class ReadCommentsOperation extends AbsOperation<List<Comment>> {

    private static final String URL = BASE_URL_API + "comments/search/findByPoiOrderByCreatedOnDesc?poiId=";
	private int mPoiID;

	public ReadCommentsOperation(Context c, OperationListener listener, int id) {
		super(c, listener);
        mPoiID = id;
	}

	@Override
	protected List<Comment> parse(JSONObject json) throws JSONException {
		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray commentsJson = _embedded.getJSONArray("comments");

		List<Comment> comments = new ArrayList<Comment>();


		for (int i = 0; i < commentsJson.length(); i++) {
			JSONObject commentJson = commentsJson.getJSONObject(i);
			Comment comment = new Gson().fromJson(commentJson.toString(), Comment.class);
			comments.add(comment);
		}
		return comments;
	}

	@Override
	protected String getOperationUrl(int page) {
		//String url = "http://vps111534.ovh.net/unm-backend/api/comments";
		String url = URL + mPoiID;

		if (page != 0) {
			url += "&page=" + page;
		}
		return url;
	}

	@Override
	protected List<Comment> combine(List<Comment> newData, List<Comment> oldData) {
		oldData.addAll(newData);
		return oldData;
	}

	@Override
	protected boolean shouldBePaged() {
		return true;
	}

}
