package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class ReadCategoriesOperation extends AbsOperation<List<Category>> {

	public static final String CATEGORIES_IMAGE_URL = "http://vps111534.ovh.net/unm-backend/categoriesicons/";
	private static final String CATEGORIES = "categories/";
	private static final String CATEGORIES_CHILDREN = "categories/%d/children/";
	private int mCategoryID;

	public ReadCategoriesOperation(Context c, OperationListener listener, int categoryID) {
		super(c, listener);
		mCategoryID = categoryID;
	}

	@Override
	protected List<Category> parse(JSONObject json) throws JSONException {
		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray categoriesJson = _embedded.getJSONArray("categories");

		List<Category> categoriesList = new ArrayList<Category>();


		for (int i = 0; i < categoriesJson.length(); i++) {
			JSONObject categoryJson = categoriesJson.getJSONObject(i);
			Category cat = new Gson().fromJson(categoryJson.toString(), Category.class);

			JSONObject links = categoryJson.getJSONObject("_links");
			JSONObject self = links.getJSONObject("self");
			cat.setSelf(self.getString("href"));

			categoriesList.add(cat);
		}
		return categoriesList;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url = BASE_URL_API;
		if(mCategoryID != -1) {
			url += String.format(CATEGORIES_CHILDREN, mCategoryID);
		} else {
			url += CATEGORIES;
		}
		if (page != 0) {
			url += "?page=" + page;
		}
		return url;
	}

	@Override
	protected List<Category> combine(List<Category> newData, List<Category> oldData) {
		oldData.addAll(newData);
		return oldData;
	}

	@Override
	protected boolean shouldBePaged() {
		return true;
	}

}
