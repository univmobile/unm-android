package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

/**
 * Created by Rokas on 2015-02-02.
 */
public class NewsListHeaderView extends RelativeLayout {
	public NewsListHeaderView(Context context) {
		super(context);
		init();
	}

	public NewsListHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NewsListHeaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_list_header, this, true);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) findViewById(R.id.title), FontHelper.FONT.EXO_BOLD);
	}

}
