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
import org.unpidf.univmobile.data.entities.ErrorEntity;
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

	public static final String URL = "json/session?apiKey=%s&loginToken=%s&key=%s";


	private ShibbolethPrepare mPrepare;

	public ShibbolethRetrieveOperation(Context c, OperationListener<Login> listener, ShibbolethPrepare prepare) {
		super(c, listener);
		mPrepare = prepare;
	}


	@Override
	protected Login parse(JSONObject json) throws JSONException {
		if (json.has("error")) {
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.UNAUTHORIZED);
			return null;
		} else {

			String token = json.getString("id");
			JSONObject user = json.getJSONObject("user");
			String id = user.getString("uid");
			String name = user.getString("displayName");
			String email = user.getString("mail");

			Login l = new Login(name, token, id, email);
			return l;
		}
	}

	@Override
	protected REQUEST getRequestType() {
		return REQUEST.POST;
	}

	@Override
	protected String getOperationUrl(int page) {
		return BASE_URL + String.format(URL, LoginDataModel.API_KEY, mPrepare.getToken(), mPrepare.getKey());
	}


}
