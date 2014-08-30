package org.unpidf.univmobile.wrapper;

import org.unpidf.univmobile.R;

import android.view.View;
import android.widget.TextView;

public class PoiGroupWrapper {

    private final View baseView;
    private TextView textView = null;

    public PoiGroupWrapper(View base) {
        this.baseView = base;
    }

    public TextView getTextView() {
        if (textView == null) {
            textView = (TextView) baseView.findViewById(R.id.textId);
        }
        return textView;
    }
}
