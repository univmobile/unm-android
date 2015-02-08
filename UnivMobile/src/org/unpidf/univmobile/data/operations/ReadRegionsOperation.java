package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Region;
import org.unpidf.univmobile.data.entities.University;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 2015-02-05.
 */
public class ReadRegionsOperation extends AbsOperation<List<Region>> {

	public static final String Regions = "regions/";

	private static final String TEMP_URL = "http://vps111534.ovh.net:8082/regions";

	public ReadRegionsOperation(Context c, OperationListener listener) {
		super(c, listener);
	}

	@Override
	List<Region> parse(InputStream in) throws IOException, JSONException {
		try {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();

			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);
			JSONObject json = new JSONObject(responseStrBuilder.toString());

			JSONObject _embedded = json.getJSONObject("_embedded");
			JSONArray regionsJson = _embedded.getJSONArray("regions");

			List<Region> regionsList = new ArrayList<Region>();


			for (int i = 0; i < regionsJson.length(); i++) {
				JSONObject region = regionsJson.getJSONObject(i);
				Region r = new Region();
				r.setName(region.getString("name"));
				r.setUniversityUrl(region.getJSONObject("_links").getJSONObject("universities").getString("href"));
				regionsList.add(r);
			}

			return regionsList;
		} finally {
			in.close();
		}
	}

	@Override
	protected String getOperationUrl() {
		return TEMP_URL;
	}
}
