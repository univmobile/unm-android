package org.unpidf.univmobile.data.operations;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.entities.ShibbolethPrepare;
import org.unpidf.univmobile.data.models.LoginDataModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-01-14.
 */
public class ShibbolethRetrieveOperation extends AbsOperation<Login> {


	private ShibbolethPrepare mPrepare;

	public ShibbolethRetrieveOperation(Context c, OperationListener<Login> listener, ShibbolethPrepare prepare) {
		super(c, listener);
		mPrepare = prepare;
	}

	@Override
	protected InputStream doRequest(int page) throws IOException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("apiKey", LoginDataModel.API_KEY));
		nameValuePairs.add(new BasicNameValuePair("loginToken", mPrepare.getToken()));
		nameValuePairs.add(new BasicNameValuePair("key", mPrepare.getKey()));

		HttpPost request = new HttpPost(getOperationUrl(page));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpClient mHttpClient = new DefaultHttpClient();
		HttpResponse response = mHttpClient.execute(request);
		return response.getEntity().getContent();
	}

	@Override
	protected Login parse(JSONObject json) throws JSONException {
		HttpPost request = new HttpPost();

		HttpPost requests = new HttpPost();

		return null;
	}

	@Override
	protected String getOperationUrl(int page) {
		return "https://univmobile-dev.univ-paris1.fr/json/session";
	}


}
