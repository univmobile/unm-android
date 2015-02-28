package org.unpidf.univmobile.data.operations;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Poi;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class PostBookmarkOperation extends AbsOperation<Poi> {


	private static final String BOOKARKS = "bookmarks/";

	private static final String CONTENT = "{\"user\":\"http://vps111534.ovh.net/unm-backend/api/users/%s\",\"poi\":\"http://vps111534.ovh.net/unm-backend/api/pois/%d\"}";


	private String mUserId;
	private Poi mPoi;

	public PostBookmarkOperation(Context c, OperationListener listener, String userID, Poi poi) {
		super(c, listener);

		this.mUserId = userID;
		this.mPoi = poi;
	}

	@Override
	protected Poi parse(JSONObject json) throws JSONException {
		return mPoi;
	}

	@Override
	protected String getOperationUrl(int page) {
		return BASE_URL_API + BOOKARKS;
	}


	@Override
	protected REQUEST getRequestType() {
		return REQUEST.POST;
	}

	@Override
	protected String getBody() {

		return String.format(CONTENT, mUserId, mPoi.getId());
	}

	@Override
	protected void handleStatusLine(HttpResponse response) {
		if (response.getStatusLine().getStatusCode() != 201) {
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.UNAUTHORIZED);
		}
	}
}
