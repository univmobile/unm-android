package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.text.Html;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.entities.Poi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class PostCommentOperation extends AbsOperation<Poi> {


	private static final String COMMENTS = "comments/";
	private static final String CONTENT = "{\"title\":\"\", \"message\":\"%s\", \"active\":\"true\", \"poi\":\"http://vps111534.ovh.net/unm-backend/api/pois/%d\"}";

	private String mMessage;
	private Poi mPoi;

	public PostCommentOperation(Context c, OperationListener listener, String message, Poi poi) {
		super(c, listener);
		mMessage = message;
		mPoi = poi;
	}

	@Override
	protected Poi parse(JSONObject json) throws JSONException {
		return mPoi;
	}

	@Override
	protected String getOperationUrl(int page) {
		return BASE_URL_API + COMMENTS;
	}


	@Override
	protected REQUEST getRequestType() {
		return REQUEST.POST;
	}

	@Override
	protected String getBody() {
		return String.format(CONTENT, mMessage, mPoi.getId());
	}

	@Override
	protected void handleStatusLine(HttpResponse response) {
		if(response.getStatusLine().getStatusCode() != 201) {
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.UNAUTHORIZED);
		}
	}
}
