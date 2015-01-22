package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
public class ShibbolethRetrieveOperation extends AsyncTask<String, Void, Boolean> {

	private Context mContext;
	private LoginDataModel.ShibbolethLoginObserver mShibbolethLoginObserver;

	public ShibbolethRetrieveOperation(Context c, ShibbolethLoginObserver observer) {
		mContext = c;
		mShibbolethLoginObserver = observer;
	}

	public void retrieveShibboleth(String loginToken, String key) {
		executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, loginToken, key);
	}

	protected Boolean doInBackground(String... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("apiKey", LoginDataModel.API_KEY));
		nameValuePairs.add(new BasicNameValuePair("loginToken", params[0]));
		nameValuePairs.add(new BasicNameValuePair("key", params[1]));

		JSONObject json = ApiManager.callAPIPost(LoginDataModel.TEST_URL_SESSION, nameValuePairs);

		Log.d("test", "json " + json.toString());
		return true;
	}

	protected void onPostExecute(Boolean result) {
		mShibbolethLoginObserver = null;
	}

}
