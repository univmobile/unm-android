package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Poi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class SearchPoisOperation extends AbsOperation<List<Poi>> {

	private static final String POIS_SEARCH_KEY_UNIV = "pois/search/searchValueInUniversityAndCategoryRoot?val=%s&%d=1&universityId=%d";
	private static final String POIS_SEARCH_KEY = "pois/search/searchValueInCategoryRoot?val=%s&categoryId=%d";


	private String mSearchValue;
	private int mUniversityID;
	private boolean mIncludeUniv;
	private int mRootCat;

	public SearchPoisOperation(Context c, OperationListener listener, int universityId, String searchValue, boolean includeUniv, int rootCat) {
		super(c, listener);
		mUniversityID = universityId;
		mSearchValue = searchValue;
		mIncludeUniv = includeUniv;
		mRootCat = rootCat;
	}

	@Override
	protected List<Poi> parse(JSONObject json) throws JSONException {
		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray poisJson = _embedded.getJSONArray("pois");

		List<Poi> pois = new ArrayList<Poi>();


		for (int i = 0; i < poisJson.length(); i++) {
			JSONObject poiJson = poisJson.getJSONObject(i);
			Poi poi = new Gson().fromJson(poiJson.toString(), Poi.class);

			JSONObject links = poiJson.getJSONObject("_links");
			//JSONObject comments = links.getJSONObject("comments");
			//poi.setCommentsUrl(comments.getString("href"));
			pois.add(poi);
		}
		return pois;
	}

	@Override
	protected String getOperationUrl(int page) {
		if(mIncludeUniv) {
			return BASE_URL_API + String.format(POIS_SEARCH_KEY_UNIV, mSearchValue, mRootCat, mUniversityID);
		} else {
			return BASE_URL_API + String.format(POIS_SEARCH_KEY, mSearchValue, mRootCat);
		}
	}

}
