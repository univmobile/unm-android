package org.unpidf.univmobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-10.
 */
public class UniversityListAdapter extends ArrayAdapter<String> {

	public UniversityListAdapter(Context context, List<String> data) {
		super(context, 0, data);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_spinner_item, null);
		}

		((TextView) convertView).setText(getItem(position));

		((UnivMobileApp) getContext().getApplicationContext()).getFontHelper().loadFont(((TextView) convertView), FontHelper.FONT.EXO_REGULAR);

		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_spinner_item, null);
		}

		((TextView) convertView).setText(getItem(position));

		((UnivMobileApp) getContext().getApplicationContext()).getFontHelper().loadFont(((TextView) convertView), FontHelper.FONT.EXO_REGULAR);

		return convertView;
	}
}
