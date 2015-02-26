package org.unpidf.univmobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.entities.NotificationEntity;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.NewsItemView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-02.
 */
public class NewsAdapter extends ArrayAdapter<News> {

	private SimpleDateFormat mDateFormat;
	private DisplayImageOptions mOptions;
	public NewsAdapter(Context context, List<News> news) {
		super(context, 0, news);
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new NewsItemView(getContext());
		}
		((NewsItemView)convertView).populate(getItem(position), mDateFormat, mOptions);
		return convertView;
	}
}
