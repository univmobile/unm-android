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

import java.util.List;

/**
 * Created by Rokas on 2015-01-27.
 */
public class NotificationsAdapter extends ArrayAdapter<NotificationEntity> {

	public NotificationsAdapter(Context context, List<NotificationEntity> notifications) {
		super(context, 0, notifications);
	}

	@Override
	public int getCount() {
		return 3;
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


		return convertView;
	}
}
