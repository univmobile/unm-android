package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Poi;

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class ReadPoiOperation extends AbsOperation<Poi> {

	private static final String POI_DETAILS = "pois/%s";

	private int mPoiID;
	private String mUrl;

	public ReadPoiOperation(Context c, OperationListener listener, int poiID, String url) {
		super(c, listener);
		mPoiID = poiID;
		mUrl = url;
	}

	@Override
	protected Poi parse(JSONObject json) throws JSONException {

		Poi poi = new Gson().fromJson(json.toString(), Poi.class);
		JSONObject links = json.getJSONObject("_links");
		JSONObject comments = links.getJSONObject("comments");
		poi.setCommentsUrl(comments.getString("href"));
		return poi;
	}

	@Override
	protected String getOperationUrl(int page) {
		if(mUrl != null) {
			return mUrl;
		}
		String url = BASE_URL_API + String.format(POI_DETAILS, mPoiID);

		return url;
	}

}
