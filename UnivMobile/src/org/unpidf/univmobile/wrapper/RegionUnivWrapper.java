package org.unpidf.univmobile.wrapper;

import org.unpidf.univmobile.R;

import android.view.View;
import android.widget.TextView;

public class RegionUnivWrapper {

	private View baseView;
	private TextView textView = null;

	public RegionUnivWrapper(View base) {
		this.baseView = base;
	}

	public TextView getTextView() {
		if (textView == null) {
			textView = (TextView) baseView.findViewById(R.id.textId);
		}
		return (textView);
	}
	
}
