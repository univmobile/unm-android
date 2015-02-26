package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Bookmark;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-05.
 */
public class BookmarksListView extends RelativeLayout {


	public BookmarksListView(Context context) {
		super(context);
	}

	public BookmarksListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BookmarksListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(List<Bookmark> bookmarks, int maxCount, OnClickListener listener, final OnBookmarkClickListener onBookmarkClickListener) {
		LayoutInflater.from(getContext()).inflate(R.layout.view_bookmark_list, this, true);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) findViewById(R.id.bookmarks_name), FontHelper.FONT.EXO_BOLD);
		helper.loadFont((android.widget.TextView) findViewById(R.id.go_to_bookmarks_fragment), FontHelper.FONT.EXO_BOLD);

		LinearLayout l = (LinearLayout) findViewById(R.id.items_container);
		for (int i = 0; i < bookmarks.size() && i < maxCount; i++) {
			BookmarkItemView item = new BookmarkItemView(getContext(), bookmarks.get(i).getPoi().getName());

			final Bookmark b = bookmarks.get(i);
			item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onBookmarkClickListener != null) {
						onBookmarkClickListener.onBookmarkClicked(b.getPoi());
					}
				}
			});

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			l.addView(item, params);
		}


		View v = findViewById(R.id.go_to_bookmarks_fragment);
		if (listener == null) {
			v.setVisibility(View.GONE);
		} else {
			v.setOnClickListener(listener);
		}
	}

	public interface OnBookmarkClickListener {
		public void onBookmarkClicked(Poi poi);
	}

}
