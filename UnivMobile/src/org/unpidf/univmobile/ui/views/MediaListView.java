package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-05.
 */
public class MediaListView extends RelativeLayout {


	public MediaListView(Context context) {
		super(context);
	}

	public MediaListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MediaListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(List<Link> links, int maxCount, OnClickListener listener) {
		LayoutInflater.from(getContext()).inflate(R.layout.view_media_list, this, true);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) findViewById(R.id.media_name), FontHelper.FONT.EXO_BOLD);
		helper.loadFont((android.widget.TextView) findViewById(R.id.go_to_media_fragment), FontHelper.FONT.EXO_BOLD);

		LinearLayout l = (LinearLayout) findViewById(R.id.items_container);

		for(int i = 0; i < maxCount && i<links.size(); i++) {
			MediaItemView item = new MediaItemView(getContext(), links.get(i).getLabel());
			final String url = links.get(i).getUrl();
			item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					getContext().startActivity(i);
				}
			});
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			l.addView(item, params);
		}


		View v = findViewById(R.id.go_to_media_fragment);
		if(listener == null) {
			v.setVisibility(View.GONE);
		} else {
			v.setOnClickListener(listener);
		}
	}
}
