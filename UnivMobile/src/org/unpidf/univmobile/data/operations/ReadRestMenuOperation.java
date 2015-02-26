package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.entities.RestoMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class ReadRestMenuOperation extends AbsOperation<List<RestoMenu>> {

	private static final String RESTO_MENU_SEARCH_POI = "restoMenus/search/findRestoMenuForPoi?poiId=%d";

	private int mPoiID;

	public ReadRestMenuOperation(Context c, OperationListener listener, int poiID) {
		super(c, listener);
		mPoiID = poiID;
	}

	@Override
	protected List<RestoMenu> parse(JSONObject json) throws JSONException {
		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray poisJson = _embedded.getJSONArray("restoMenus");

		List<RestoMenu> menues = new ArrayList<RestoMenu>();


		for (int i = 0; i < poisJson.length(); i++) {
			JSONObject menuJson = poisJson.getJSONObject(i);
			RestoMenu menu = new Gson().fromJson(menuJson.toString(), RestoMenu.class);

			menues.add(menu);
		}
		return menues;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url = BASE_URL_API + String.format(RESTO_MENU_SEARCH_POI, mPoiID);

		if (page != 0) {
			url += "&page=" + page;
		}
		return url;
	}

	@Override
	protected List<RestoMenu> combine(List<RestoMenu> newData, List<RestoMenu> oldData) {
		oldData.addAll(newData);
		return oldData;
	}


	@Override
	protected boolean shouldBePaged() {
		return true;
	}

	@Override
	protected REQUEST getRequestType() {
		return REQUEST.GET;
	}

	@Override
	protected String getBody() {
		return null;
	}
}
