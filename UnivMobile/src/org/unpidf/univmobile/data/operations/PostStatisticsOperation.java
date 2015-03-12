package org.unpidf.univmobile.data.operations;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.University;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class PostStatisticsOperation extends AbsOperation<Boolean> {

	private static final String STATS = "usageStats/";
	private String mUniv;


	public PostStatisticsOperation(Context c, String university) {
		super(c, null);
		mUniv = university;
	}

	@Override
	protected Boolean parse(JSONObject json) throws JSONException {
		return true;
	}

	@Override
	protected String getOperationUrl(int page) {
		return BASE_URL_API + STATS;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		clear();
		super.onPostExecute(result);
	}

	@Override
	protected REQUEST getRequestType() {
		return REQUEST.POST;
	}

	@Override
	protected String getBody() {

		try {
			JSONObject json = new JSONObject();
			json.put("source", "A");
			json.put("university", mUniv);
			return json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "{}";
	}
}
