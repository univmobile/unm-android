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
 * Created by Rokas on 2015-01-19.
 */
public class StandardLoginOperation extends AsyncTask<String, Void, Boolean> {

	private Context mContext;
	private StandardLoginObserver mLoginObserver;

	public StandardLoginOperation(Context c, StandardLoginObserver observer) {
		mContext = c;
		mLoginObserver = observer;
	}


	public void standartLogin(String user, String password) {
		executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user, password);
	}

	protected Boolean doInBackground(String... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("apiKey", LoginDataModel.API_KEY));
		nameValuePairs.add(new BasicNameValuePair("login", params[0]));
		nameValuePairs.add(new BasicNameValuePair("password", params[1]));

		JSONObject json = ApiManager.callAPIPost(LoginDataModel.TEST_URL_SESSION, nameValuePairs);

		return false;
	}

	protected void onPostExecute(Boolean result) {
		if(mLoginObserver != null) {

		}
	}


}
