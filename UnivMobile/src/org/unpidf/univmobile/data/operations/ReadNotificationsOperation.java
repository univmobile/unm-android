package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.text.Html;
import android.util.Base64;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.entities.NotificationEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class ReadNotificationsOperation extends AbsOperation<List<NotificationEntity>> {

	private static final String NOTIFICATIONS_SEARCH_UNIV = "notifications/search/findNotificationsForUniversity?universityId=%d";
	private static final String NOTIFICATIONS_SEARCH_UNIV_DATTE = "notifications/search/findNotificationsForUniversitySince?universityId=%d&since=%s";



	private int mUniversityID;
	private String mDate;

	public ReadNotificationsOperation(Context c, OperationListener listener, int universityId, String date) {
		super(c, listener);
		mUniversityID = universityId;
		mDate = date;
	}

	@Override
	protected List<NotificationEntity> parse(JSONObject json) throws JSONException {
		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray notificationsJson = _embedded.getJSONArray("notifications");

		List<NotificationEntity> notifications = new ArrayList<NotificationEntity>();

		for (int i = 0; i < notificationsJson.length(); i++) {
			JSONObject notificationJson = notificationsJson.getJSONObject(i);
			NotificationEntity notification = new Gson().fromJson(notificationJson.toString(), NotificationEntity.class);
			notifications.add(notification);
		}
		return notifications;
	}


	@Override
	protected String getOperationUrl(int page) {
		String url = BASE_URL_API;
		if(mDate != null) {
			try {
				url +=  String.format(NOTIFICATIONS_SEARCH_UNIV_DATTE, mUniversityID, URLEncoder.encode(mDate, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			url +=  String.format(NOTIFICATIONS_SEARCH_UNIV, mUniversityID);
		}

		if (page != 0) {
			url += "&page=" + page;
		}
		return url;
	}

	@Override
	protected List<NotificationEntity> combine(List<NotificationEntity> newData, List<NotificationEntity> oldData) {
		oldData.addAll(newData);
		return oldData;
	}

	@Override
	protected boolean shouldBePaged() {
		return true;
	}

}
