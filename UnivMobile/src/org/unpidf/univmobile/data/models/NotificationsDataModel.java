package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.entities.NotificationEntity;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.PostLastReadNotificationOperation;
import org.unpidf.univmobile.data.operations.ReadNotificationsOperation;
import org.unpidf.univmobile.data.repositories.SharedPreferencesRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-12.
 */
public class NotificationsDataModel extends AbsDataModel {
	private static final String PREF_LAST_READ_NOTIFICATION_TIME = "pref_last_read_notification_time";

	private Context mContext;
	private NotificationsModelListener mListener;

	private ReadNotificationsOperation mReadNotificationsOperation;
	private PostLastReadNotificationOperation mPostLastReadNotificationOperation;

	private List<NotificationEntity> mNotifications;

	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public NotificationsDataModel(Context c) {
		mContext = c;
	}

	@Override
	public void clear() {
		clearOperation(mReadNotificationsOperation);
		mReadNotificationsOperation = null;

		clearOperation(mPostLastReadNotificationOperation);
		mPostLastReadNotificationOperation = null;


		mContext = null;
		mListener = null;
	}

	public void getNotifications(NotificationsModelListener listener) {
		clearOperation(mReadNotificationsOperation);
		mReadNotificationsOperation = null;

		mListener = listener;

		int univID = UniversitiesDataModel.getSavedUniversity(mContext).getId();

		String date = null;
		long lastReadDate = SharedPreferencesRepo.getLong(mContext, PREF_LAST_READ_NOTIFICATION_TIME);
		if (lastReadDate != -1) {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			date = mDateFormat.format(c.getTime());
		}
		mReadNotificationsOperation = new ReadNotificationsOperation(mContext, mReadNotificationsOperationListener, univID, date);
		mReadNotificationsOperation.startOperation();
	}


	public void saveNotificationsReadDate() {
		Login login = ((UnivMobileApp) mContext.getApplicationContext()).getLogin();
		if (login != null && mNotifications != null && mNotifications.size() > 0) {

			mPostLastReadNotificationOperation = new PostLastReadNotificationOperation(mContext, null, login.getId(), mNotifications.get(0).getId());
			mPostLastReadNotificationOperation.startOperation();
		}
		SharedPreferencesRepo.saveLong(mContext, PREF_LAST_READ_NOTIFICATION_TIME, System.currentTimeMillis());
	}

	private int getNewNotificationsCount() {
		long lastReadDate = SharedPreferencesRepo.getLong(mContext, PREF_LAST_READ_NOTIFICATION_TIME);
		if (lastReadDate == -1) {
			return mNotifications.size();
		}

		int totalCount = 0;
		for (NotificationEntity notification : mNotifications) {
			try {
				Date notificationDate = mDateFormat.parse(notification.getNotificationTime());
				if (notificationDate.getTime() > lastReadDate) {
					totalCount++;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return totalCount;
	}

	private OperationListener<List<NotificationEntity>> mReadNotificationsOperationListener = new OperationListener<List<NotificationEntity>>() {
		@Override
		public void onOperationStarted() {
			if (mListener != null) {
				mListener.showLoadingIndicator();
			}
		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<NotificationEntity> result) {
			if (error != null && mListener != null) {
				mListener.onError(error);
			}
			clearOperation(mReadNotificationsOperation);
			mReadNotificationsOperation = null;

			if (mListener != null) {
				if (result != null) {
					mNotifications = result;
					mListener.notificationsReceived(getNewNotificationsCount(), result);
				}
			}
		}

		@Override
		public void onPageDownloaded(List<NotificationEntity> result) {
		}
	};


	public interface NotificationsModelListener extends ModelListener {
		void showLoadingIndicator();

		void notificationsReceived(int newNotificationsCount, List<NotificationEntity> notifications);

	}
}
