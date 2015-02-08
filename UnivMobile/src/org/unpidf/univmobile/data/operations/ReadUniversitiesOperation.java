package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.University;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 2015-01-31.
 */
public class ReadUniversitiesOperation extends AbsOperation<List<University>> {


	private String mUrl;

	public ReadUniversitiesOperation(Context c, OperationListener listener, String url) {
		super(c, listener);
		mUrl = url;
	}

	@Override
	protected List<University> parse(InputStream in) throws IOException, JSONException {
		try {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();

			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);
			JSONObject json = new JSONObject(responseStrBuilder.toString());

			JSONObject _embedded = json.getJSONObject("_embedded");
			JSONArray universitiesJson = _embedded.getJSONArray("universities");

			List<University> universitiesList = new ArrayList<University>();


			for (int i = 0; i < universitiesJson.length(); i++) {
				University u = new Gson().fromJson(universitiesJson.getJSONObject(i).toString(), University.class);
				universitiesList.add(u);
			}

			return universitiesList;
		} finally {
			in.close();
		}

	}

	@Override
	protected String getOperationUrl() {
		return mUrl;
	}

}
