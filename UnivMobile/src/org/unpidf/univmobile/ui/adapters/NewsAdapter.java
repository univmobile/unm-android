package org.unpidf.univmobile.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.ui.views.NewsItemView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-02.
 */
public class NewsAdapter extends ArrayAdapter<News> {

	private SimpleDateFormat mDateFormat;
	public NewsAdapter(Context context, List<News> news) {
		super(context, 0, news);
		mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new NewsItemView(getContext());
		}
		((NewsItemView)convertView).populate(getItem(position), mDateFormat);
		return convertView;
	}
}
