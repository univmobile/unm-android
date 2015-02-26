package org.unpidf.univmobile.data.operations;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.ErrorEntity;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class PostBookmarkOperation extends AbsOperation<Boolean> {


	private static final String BOOKARKS = "bookmarks/";

	private static final String CONTENT = "{\"user\":\"http://vps111534.ovh.net/unm-backend/api/users/%s\",\"poi\":\"http://vps111534.ovh.net/unm-backend/api/pois/%d\"}";


	private String mUserId;
	private int mPoiID;

	public PostBookmarkOperation(Context c, OperationListener listener, String userID, int poiID) {
		super(c, listener);

		this.mUserId = userID;
		this.mPoiID = poiID;
	}

	@Override
	protected Boolean parse(JSONObject json) throws JSONException {
		return true;
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

		return String.format(CONTENT, mUserId, mPoiID);
	}

	@Override
	protected void handleStatusLine(HttpResponse response) {
		if (response.getStatusLine().getStatusCode() != 201) {
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.UNAUTHORIZED);
		}
	}
}
