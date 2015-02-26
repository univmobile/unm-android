package org.unpidf.univmobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class UniversitiesAdapter extends ArrayAdapter<University> {

	public UniversitiesAdapter(Context context, List<University> universities) {
		super(context, 0, universities);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_university_item, null);
		}

		((TextView) convertView).setText(getItem(position).getTitle());

		((UnivMobileApp) getContext().getApplicationContext()).getFontHelper().loadFont(((TextView) convertView), FontHelper.FONT.EXO2_LIGHT);

		return convertView;
	}
}