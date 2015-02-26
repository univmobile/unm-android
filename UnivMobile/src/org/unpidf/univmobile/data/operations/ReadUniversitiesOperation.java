package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.University;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-01-31.
 */
public class ReadUniversitiesOperation extends AbsOperation<List<University>> {

private static final String UNIVERSITIES = "universities/";
	private String mUrl;

	public ReadUniversitiesOperation(Context c, OperationListener listener, String url) {
		super(c, listener);
		mUrl = url;
	}

	@Override
	protected List<University> parse(JSONObject json) throws JSONException {

		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray universitiesJson = _embedded.getJSONArray("universities");

		List<University> universitiesList = new ArrayList<University>();


		for (int i = 0; i < universitiesJson.length(); i++) {
			JSONObject universityJson = universitiesJson.getJSONObject(i);
			University u = new Gson().fromJson(universityJson.toString(), University.class);

			JSONObject links = universityJson.getJSONObject("_links");
			JSONObject self = links.getJSONObject("self");
			u.setSelf(self.getString("href"));

			universitiesList.add(u);
		}

		return universitiesList;

	}

	@Override
	protected List<University> combine(List<University> newData, List<University> oldData) {
		return null;
	}

	@Override
	protected String getOperationUrl(int page) {
		if(mUrl == null) {
			return BASE_URL_API + UNIVERSITIES;
		}

		return mUrl;
	}

}
