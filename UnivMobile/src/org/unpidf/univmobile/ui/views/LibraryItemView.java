package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Library;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

/**
 * Created by rviewniverse on 2015-02-05.
 */
public class LibraryItemView extends RelativeLayout {


	public LibraryItemView(Context context, Library lib) {
		super(context);
		init(lib);
	}


	private void init(Library lib) {
		LayoutInflater.from(getContext()).inflate(R.layout.view_library_item, this, true);

		TextView t = (TextView) findViewById(R.id.library_bookmark_content);
		t.setText(lib.getPoiName());

		if (!lib.isIconRuedesfacs()) {
			findViewById(R.id.library_icons).setVisibility(View.INVISIBLE);
		}
		if (lib.getPoiId() == -1) {
			findViewById(R.id.library_bookmark_ic).setVisibility(View.GONE);
		}

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((TextView) findViewById(R.id.library_bookmark_content), FontHelper.FONT.EXO_MEDIUM);
	}
}
