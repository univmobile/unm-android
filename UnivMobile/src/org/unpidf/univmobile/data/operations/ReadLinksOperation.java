package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.data.entities.Poi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class ReadLinksOperation extends AbsOperation<List<Link>> {

	private static final String LINKS = "links/search/findByUniversity?universityId=%d";

	private int mUniversityID;

	public ReadLinksOperation(Context c, OperationListener listener, int universityID) {
		super(c, listener);
		mUniversityID = universityID;
	}

	@Override
	protected List<Link> parse(JSONObject json) throws JSONException {

		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray linksJson = _embedded.getJSONArray("links");

		List<Link> links = new ArrayList<Link>();


		for (int i = 0; i < linksJson.length(); i++) {
			JSONObject linkJson = linksJson.getJSONObject(i);
			Link link = new Gson().fromJson(linkJson.toString(), Link.class);

			links.add(link);

		}
		return links;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url = BASE_URL_API + String.format(LINKS, mUniversityID);
		if (page != 0) {
			url += "&page=" + page;
		}
		return url;
	}

	@Override
	protected List<Link> combine(List<Link> newData, List<Link> oldData) {
		oldData.addAll(newData);
		return oldData;
	}

	@Override
	protected boolean shouldBePaged() {
		return true;
	}
}
