package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.Poi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class ReadPoisOperation extends AbsOperation<List<Poi>> {

	private static final String POIS_SEARCH_UNIV_AND_CAT_ROOT = "pois/search/findByUniversityAndCategoryRoot?universityId=%d&categoryId=%d&size=200";
	private static final String POIS_SEARCH_CAT_ROOT = "pois/search/findByCategoryRoot?categoryId=%d&size=200";
	private static final String POIS_SEARCH_UNIV_AND_CAT_LIST = "pois/search/findByUniversityAndCategoryIn?universityId=%d&size=200&categories=";
	private static final String POIS_SEARCH_UNIV = "pois/search/findByUniversity?universityId=%d&size=200";
	private static final String POIS_SEARCH_CAT_LIST = "pois/search/findByCategoryIn?size=200&categories=";



	private List<Integer> mSelectedCategories;
	private int mRootCat;
	private int mUniversityID;
	private String mUrl;

	public ReadPoisOperation(Context c, OperationListener listener, List<Category> categories, int root, int universityId, String url) {
		super(c, listener);

		if (categories != null) {
			mSelectedCategories = new ArrayList<Integer>();
			for (Category cat : categories) {
				mSelectedCategories.add(cat.getId());
			}
		}
		mRootCat = root;
		mUniversityID = universityId;
		mUrl = url;
	}

	@Override
	protected List<Poi> parse(JSONObject json) throws JSONException {
		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray poisJson = _embedded.getJSONArray("pois");

		List<Poi> pois = new ArrayList<Poi>();


		for (int i = 0; i < poisJson.length(); i++) {
			JSONObject poiJson = poisJson.getJSONObject(i);
			Poi poi = new Gson().fromJson(poiJson.toString(), Poi.class);
			//if (poi.isActive()) {

				JSONObject links = poiJson.getJSONObject("_links");
				JSONObject comments = links.getJSONObject("comments");
				poi.setCommentsUrl(comments.getString("href"));
				pois.add(poi);
			//}
		}
		return pois;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url = null;
		if (mUrl == null) {
			if (mSelectedCategories != null) {
				if(mUniversityID != -1) {
					url = BASE_URL_API + String.format(POIS_SEARCH_UNIV_AND_CAT_LIST, mUniversityID);
				} else {
					url = BASE_URL_API + POIS_SEARCH_CAT_LIST;
				}
				for (int i = 0; i < mSelectedCategories.size(); i++) {
					url += mSelectedCategories.get(i);
					if (i < mSelectedCategories.size() - 1) {
						url += ",";
					}

				}
			} else if (mRootCat != -1) {
				if(mUniversityID != -1) {
					url = BASE_URL_API + String.format(POIS_SEARCH_UNIV_AND_CAT_ROOT, mUniversityID, mRootCat);
				} else {
					url = BASE_URL_API + String.format(POIS_SEARCH_CAT_ROOT, mRootCat);
				}
			} else {
				url = BASE_URL_API + String.format(POIS_SEARCH_UNIV, mUniversityID);
			}
		} else {
			url = mUrl;
		}
		if (page != 0) {
			url += "&page=" + page;
		}
		return url;
	}

	@Override
	protected List<Poi> combine(List<Poi> newData, List<Poi> oldData) {
		oldData.addAll(newData);
		return oldData;
	}

	@Override
	protected boolean shouldBePaged() {
		return true;
	}
}
