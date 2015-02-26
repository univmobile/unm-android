package org.unpidf.univmobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.NotificationEntity;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rviewniverse on 2015-01-27.
 */
public class NotificationsAdapter extends ArrayAdapter<NotificationEntity> {

	private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat mDateFormatParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


	public NotificationsAdapter(Context context, List<NotificationEntity> notifications) {
		super(context, 0, notifications);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_notification_item, null);

			TextView item = (TextView) convertView.findViewById(R.id.notification_text);
			((UnivMobileApp) getContext().getApplicationContext()).getFontHelper().loadFont(item, FontHelper.FONT.EXO_SEMI_BOLD);


			TextView time = (TextView) convertView.findViewById(R.id.time);
			((UnivMobileApp) getContext().getApplicationContext()).getFontHelper().loadFont(time, FontHelper.FONT.EXO2_REGULAR);

		}

		TextView item = (TextView) convertView.findViewById(R.id.notification_text);
		item.setText(getItem(position).getContent());

		TextView time = (TextView) convertView.findViewById(R.id.time);
		time.setText(getTimeString(getItem(position).getNotificationTime()));
		return convertView;
	}

	private String getTimeString(String time) {
		try {

			Date timeDate = mDateFormatParse.parse(time);

			long timeDifference = System.currentTimeMillis() - timeDate.getTime();
			long timeDifferenceMinutes = timeDifference / 1000 / 60;
			if (timeDifferenceMinutes > 60 * 24) { //more than one day
				return mDateFormat.format(timeDate);
			} else {
				String before = getContext().getString(R.string.before) + " ";
				if (timeDifferenceMinutes > 60) {
					int hours = (int) (timeDifferenceMinutes / 60);
					before += hours  +  " "+ getContext().getString(R.string.hours) +  " ";
					timeDifferenceMinutes = timeDifferenceMinutes - hours * 60;
				}

				before += timeDifferenceMinutes  +  " " + getContext().getString(R.string.minutes);
				return before;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
}
