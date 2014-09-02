package org.unpidf.univmobile.wrapper;

import org.unpidf.univmobile.R;

import android.view.View;
import android.widget.TextView;

public class CommentWrapper {

    private final View baseView;
    private TextView textView = null;
    private TextView descView = null;

    public CommentWrapper(View base) {
        this.baseView = base;
    }

    public TextView getPseudoView() {
        if (textView == null) {
            textView = (TextView) baseView.findViewById(R.id.pseudoId);
        }
        return textView;
    }

    public TextView getCommentView() {
        if (descView == null) {
            descView = (TextView) baseView.findViewById(R.id.textId);
        }
        return descView;
    }
}
