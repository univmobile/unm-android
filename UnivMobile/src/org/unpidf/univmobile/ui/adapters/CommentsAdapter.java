package org.unpidf.univmobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Comment;
import org.unpidf.univmobile.ui.uiutils.ColorsHelper;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-11.
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {

	private int mSelectedTab;

	private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat mDateFormatParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public CommentsAdapter(Context context, List<Comment> comment, int tab) {
		super(context, 0, comment);
		mSelectedTab = tab;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_poi_comment_item, null);
		}

		TextView name = (TextView) convertView.findViewById(R.id.user_name);
		name.setTextColor(ColorsHelper.getColorByTab(getContext(), false, mSelectedTab));
		name.setText(getItem(position).getAuthor());
		((UnivMobileApp) getContext().getApplicationContext()).getFontHelper().loadFont(name, FontHelper.FONT.EXO_BOLD);

		TextView date = (TextView) convertView.findViewById(R.id.post_date);
		date.setText(getTimeString(getItem(position).getPostedOn()));
		((UnivMobileApp) getContext().getApplicationContext()).getFontHelper().loadFont(date, FontHelper.FONT.EXO_BOLD_ITALIC);

		TextView message = (TextView) convertView.findViewById(R.id.message);
		message.setText(getItem(position).getMessage());
		((UnivMobileApp) getContext().getApplicationContext()).getFontHelper().loadFont(message, FontHelper.FONT.EXO_MEDIUM);

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
