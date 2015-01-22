package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.unpidf.univmobile.data.ApiManager;
import org.unpidf.univmobile.data.models.LoginDataModel;
import org.unpidf.univmobile.data.models.LoginDataModel.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 2015-01-14.
 */
public class ShibbolethPrepareOperation extends AsyncTask<Void, Void, String[]> {

	private Context mContext;
	private ShibbolethLoginObserver mShibbolethLoginObserver;

	public ShibbolethPrepareOperation(Context c, ShibbolethLoginObserver observer) {
		mContext = c;
		mShibbolethLoginObserver = observer;
	}

	public void prepareShibboleth() {
		executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	protected String[] doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("apiKey", LoginDataModel.API_KEY));
		nameValuePairs.add(new BasicNameValuePair("prepare", null));

		JSONObject json = ApiManager.callAPIPost(LoginDataModel.TEST_URL_SESSION, nameValuePairs);

		if (json != null) {
			String loginToken = json.optString("loginToken");
			String key = json.optString("key");
			return new String[]{loginToken, key};
		}
		return null;
	}

	protected void onPostExecute(String[] result) {
		if (result != null && result.length >= 2) {
			mShibbolethLoginObserver.onShibbolethPrepareSuccessful(result[0], result[1]);
		} else {
			mShibbolethLoginObserver.onShibbolethPrepareFailed();
		}
		mShibbolethLoginObserver = null;
	}
}
