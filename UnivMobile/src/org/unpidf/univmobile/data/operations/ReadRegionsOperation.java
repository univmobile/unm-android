package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.entities.Region;
import org.unpidf.univmobile.data.entities.University;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-05.
 */
public class ReadRegionsOperation extends AbsOperation<List<Region>> {

	public static final String REGIONS = "regions/";


	public ReadRegionsOperation(Context c, OperationListener listener) {
		super(c, listener);
	}

	@Override
	protected List<Region> parse(JSONObject json) throws JSONException {

		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray regionsJson = _embedded.getJSONArray("regions");

		List<Region> regionsList = new ArrayList<Region>();


		for (int i = 0; i < regionsJson.length(); i++) {
			JSONObject region = regionsJson.getJSONObject(i);
			Region r = new Region();
			r.setId(region.getInt("id"));
			r.setName(region.getString("name"));
			r.setUniversityUrl(region.getJSONObject("_links").getJSONObject("universities").getString("href"));
			regionsList.add(r);
		}

		return regionsList;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url =  BASE_URL_API + REGIONS;
		if (page != 0) {
			url += "&page=" + page;
		}
		return url;
	}

	@Override
	protected List<Region> combine(List<Region> newData, List<Region> oldData) {
		oldData.addAll(newData);
		return oldData;
	}

	@Override
	protected boolean shouldBePaged() {
		return true;
	}
}
