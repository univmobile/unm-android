package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

/**
 * Created by rviewniverse on 2015-02-05.
 */
public class MediaItemView  extends RelativeLayout {


	public MediaItemView(Context context, String text) {
		super(context);
		init(text);
	}


	private void init(String text) {
		LayoutInflater.from(getContext()).inflate(R.layout.view_media_item, this, true);

		TextView t = (TextView) findViewById(R.id.name);
		t.setText(text);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) findViewById(R.id.name), FontHelper.FONT.EXO_MEDIUM);
	}
}
