package org.unpidf.univmobile.data.operations;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.ErrorEntity;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class PostLastReadNotificationOperation extends AbsOperation<Boolean> {


	private static final String NOTIFICATIONS = "/notifications/lastRead?userId=%s&notificationId=%d";


	private String mUserId;
	private int mNotificationID;

	public PostLastReadNotificationOperation(Context c, OperationListener listener, String userID, int notificationID) {
		super(c, listener);

		this.mUserId = userID;
		this.mNotificationID = notificationID;
	}

	@Override
	protected Boolean parse(JSONObject json) throws JSONException {
		return true;
	}

	@Override
	protected String getOperationUrl(int page) {
		return BASE_URL_API + String.format(NOTIFICATIONS, mUserId, mNotificationID);
	}


}
