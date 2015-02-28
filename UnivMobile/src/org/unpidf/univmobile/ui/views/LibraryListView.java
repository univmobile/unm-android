package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Library;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-05.
 */
public class LibraryListView extends RelativeLayout {


	public LibraryListView(Context context) {
		super(context);
	}

	public LibraryListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LibraryListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(List<Library> libraries, int maxCount, OnClickListener listener, final OnLibraryClickListener myListener) {
		LayoutInflater.from(getContext()).inflate(R.layout.view_library_list, this, true);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) findViewById(R.id.library_name), FontHelper.FONT.EXO_BOLD);
		helper.loadFont((android.widget.TextView) findViewById(R.id.go_to_library_fragment), FontHelper.FONT.EXO_BOLD);

		LinearLayout l = (LinearLayout) findViewById(R.id.items_container);
		for (int i = 0; i < libraries.size() && i < maxCount; i++) {
			LibraryItemView item = new LibraryItemView(getContext(), libraries.get(i));
			final int poiID = libraries.get(i).getPoiId();
			item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(myListener != null) {
						myListener.onLibraryClicked(poiID);
					}
				}
			});
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			l.addView(item, params);
		}

		View v = findViewById(R.id.go_to_library_fragment);
		if(listener == null) {
			v.setVisibility(View.GONE);
		} else {
			v.setOnClickListener(listener);
		}
	}


	public void clear() {
		removeAllViews();
	}

	public interface OnLibraryClickListener {
		public void onLibraryClicked(int poiId);
	}
}
