package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.ShibbolethPrepare;
import org.unpidf.univmobile.data.models.LoginDataModel;
import org.unpidf.univmobile.data.models.LoginDataModel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-01-14.
 */
public class ShibbolethPrepareOperation extends AbsOperation<ShibbolethPrepare> {

	private static final String URL = "json/session?apiKey=%s&prepare=";

	public ShibbolethPrepareOperation(Context c, OperationListener<ShibbolethPrepare> listener) {
		super(c, listener);
	}


//	@Override
//	protected InputStream doRequest(int page) throws IOException {
//		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//		nameValuePairs.add(new BasicNameValuePair("apiKey", LoginDataModel.API_KEY));
//		nameValuePairs.add(new BasicNameValuePair("prepare", null));
//
//		HttpPost request = new HttpPost(getOperationUrl(page));
//		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//		HttpClient mHttpClient = new DefaultHttpClient();
//		HttpResponse response = mHttpClient.execute(request);
//		return response.getEntity().getContent();
//	}

	@Override
	protected ShibbolethPrepare parse(JSONObject json) throws JSONException {
		String loginToken = json.optString("loginToken");
		String key = json.optString("key");
		return new ShibbolethPrepare(loginToken, key);
	}

	@Override
	protected String getOperationUrl(int page) {
		return BASE_URL + String.format(URL, LoginDataModel.API_KEY);
	}

	@Override
	protected REQUEST getRequestType() {
		return REQUEST.POST;
	}
}

