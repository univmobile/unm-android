package org.unpidf.univmobile.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by rviewniverse on 2015-02-25.
 */
public class SqareImageView extends ImageView {
	public SqareImageView(Context context) {
		super(context);
	}

	public SqareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SqareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, heightMeasureSpec);
	}
}
