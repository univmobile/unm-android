package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.NavigationMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class ReadMenusOperation extends AbsOperation<List<NavigationMenu>> {

	private static final String MENU_SEARCH_UNIV = "menues/search/findAllForUniversity?universityId=%d";

	private int mUniversityID;

	public ReadMenusOperation(Context c, OperationListener listener, int universityId) {
		super(c, listener);
		mUniversityID = universityId;
	}

	@Override
	protected List<NavigationMenu> parse(JSONObject json) throws JSONException {
		if(json != null) {
			JSONObject _embedded = json.getJSONObject("_embedded");
			JSONArray menusJson = _embedded.getJSONArray("menu");

			List<NavigationMenu> menus = new ArrayList<NavigationMenu>();

			for (int i = 0; i < menusJson.length(); i++) {
				JSONObject menuJson = menusJson.getJSONObject(i);
				NavigationMenu menu = new Gson().fromJson(menuJson.toString(), NavigationMenu.class);
				if (menu.isActive()) {
                    //if(!menu.getName().equals("Que faire à Paris"))
					menus.add(menu);
				}
			}
			return menus;
		}
		return null;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url = BASE_URL_API + String.format(MENU_SEARCH_UNIV, mUniversityID);
		if (page != 0) {
			url += "&page=" + page;
		}
		return url;
	}

	@Override
	protected List<NavigationMenu> combine(List<NavigationMenu> newData, List<NavigationMenu> oldData) {
		oldData.addAll(newData);
		return oldData;
	}

	@Override
	protected boolean shouldBePaged() {
		return true;
	}
}
