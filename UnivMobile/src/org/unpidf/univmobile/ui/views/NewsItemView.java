package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

/**
 * Created by Rokas on 2015-02-01.
 */
public class NewsItemView extends RelativeLayout {

	private boolean mIsExpanded = false;

	public NewsItemView(Context context) {
		super(context);
		init();
	}

	public NewsItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NewsItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_news_item, this, true);
		setOnClickListener(mExpandListener);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) findViewById(R.id.time_act), FontHelper.FONT.EXO_ITALIC);
		helper.loadFont((android.widget.TextView) findViewById(R.id.title_act), FontHelper.FONT.EXO_BOLD);
		helper.loadFont((android.widget.TextView) findViewById(R.id.content_act), FontHelper.FONT.EXO_REGULAR);
		helper.loadFont((android.widget.TextView) findViewById(R.id.action_button), FontHelper.FONT.EXO_BOLD);
	}

	private OnClickListener mExpandListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			View content = findViewById(R.id.content_container);
			if (mIsExpanded) {
				((ImageView) findViewById(R.id.ic_arrow)).setImageResource(R.drawable.ic_arrow_normal);
				collapse(content);
				mIsExpanded = false;
			} else {
				((ImageView) findViewById(R.id.ic_arrow)).setImageResource(R.drawable.ic_arrow_expanded);
				expand(content);
				mIsExpanded = true;
			}
		}
	};

	private void expand(final View v) {

		v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targetHeight = v.getMeasuredHeight() + getMeasuredHeight();

		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
				v.requestLayout();
				v.getParent().requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 0.5dp/ms
		a.setDuration((int) (targetHeight * 2 / v.getContext().getResources().getDisplayMetrics().density));

		v.startAnimation(a);
	}

	private void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
					v.requestLayout();
					v.getParent().requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 0.5dp/ms
		a.setDuration((int)(initialHeight * 2 / v.getContext().getResources().getDisplayMetrics().density));

		a.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		v.startAnimation(a);
	}

}
