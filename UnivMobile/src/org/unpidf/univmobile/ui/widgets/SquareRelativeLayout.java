package org.unpidf.univmobile.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by rviewniverse on 2015-02-24.
 */
public class SquareRelativeLayout extends RelativeLayout {
	public SquareRelativeLayout(Context context) {
		super(context);
	}

	public SquareRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		super.onMeasure(heightMeasureSpec, heightMeasureSpec);
	}
}
