package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

/**
 * Created by rviewniverse on 2015-02-08.
 */
public class CategoryItemView extends LinearLayout {

	private String mText;
	private boolean mMeasured = false;

	public CategoryItemView(Context context) {
		super(context);
		init();
	}

	public CategoryItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CategoryItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.veiw_category_item, this, true);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((TextView) findViewById(R.id.title), FontHelper.FONT.EXO_BOLD);
	}

	public void setCategoryText(String text) {
		mText = text;
		((TextView) findViewById(R.id.title)).setText(text);
	}

	public ImageView getImageView() {
		return ((ImageView) findViewById(R.id.category_image));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (width * 1.2);
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
	}

}
