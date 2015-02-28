package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Library;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.data.entities.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class ReadLibrariesOperation extends AbsOperation<List<Library>> {

	private static final String LINKS = "universityLibraries/search/findByUniversity?universityId=%d";

	private int mUniversityID;

	public ReadLibrariesOperation(Context c, OperationListener listener, int universityID) {
		super(c, listener);
		mUniversityID = universityID;
	}

	@Override
	protected List<Library> parse(JSONObject json) throws JSONException {

		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray librariesJson = _embedded.getJSONArray("universityLibraries");

		List<Library> libraries = new ArrayList<Library>();


		for (int i = 0; i < librariesJson.length(); i++) {
			JSONObject libraryJson = librariesJson.getJSONObject(i);
			Library library = new Gson().fromJson(libraryJson.toString(), Library.class);
			libraries.add(library);
		}
		return libraries;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url = BASE_URL_API + String.format(LINKS, mUniversityID);
		if (page != 0) {
			url += "&page=" + page;
		}
		return url;
	}

	@Override
	protected List<Library> combine(List<Library> newData, List<Library> oldData) {
		oldData.addAll(newData);
		return oldData;
	}

	@Override
	protected boolean shouldBePaged() {
		return true;
	}
}
