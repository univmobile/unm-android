package org.unpidf.univmobile.data.operations;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.models.LoginDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-01-19.
 */
public class StandardLoginOperation extends AbsOperation<Login> {

	private static final String LOGIN_URL = "json/session?apiKey=%s&login=%s&password=%s";
	private String mName;
	private String mPass;

	public StandardLoginOperation(Context c, OperationListener listener, String name, String pass) {
		super(c, listener);
		mName = name;
		mPass = pass;
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
	protected String getOperationUrl(int page) {
		return BASE_URL + String.format(LOGIN_URL, LoginDataModel.API_KEY, mName, mPass);
	}

	@Override
	protected REQUEST getRequestType() {
		return REQUEST.POST;
	}
}
