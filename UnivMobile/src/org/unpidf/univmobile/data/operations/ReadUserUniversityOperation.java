package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.entities.University;

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class ReadUserUniversityOperation extends AbsOperation<University> {

	private static final String USER_UNIV = "users/%s/university";

	private String mUserID;

	public ReadUserUniversityOperation(Context c, OperationListener listener, String userID) {
		super(c, listener);
		mUserID = userID;
	}

	@Override
	protected University parse(JSONObject json) throws JSONException {

		University univ = new Gson().fromJson(json.toString(), University.class);
		JSONObject links = json.getJSONObject("_links");
		JSONObject self = links.getJSONObject("self");
		univ.setSelf(self.getString("href"));


		return univ;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url = BASE_URL_API + String.format(USER_UNIV, mUserID);

		return url;
	}

}
