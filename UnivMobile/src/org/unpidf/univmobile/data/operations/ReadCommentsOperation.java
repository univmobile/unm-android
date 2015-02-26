package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Comment;
import org.unpidf.univmobile.data.entities.Poi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class ReadCommentsOperation extends AbsOperation<List<Comment>> {
	private String mUrl;

	public ReadCommentsOperation(Context c, OperationListener listener, String url) {
		super(c, listener);
		mUrl = url;
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
		String url = mUrl;

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
