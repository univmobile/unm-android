package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.Comment;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.entities.RestoMenu;
import org.unpidf.univmobile.data.operations.ReadCategoriesOperation;
import org.unpidf.univmobile.ui.adapters.CommentsAdapter;
import org.unpidf.univmobile.ui.uiutils.ColorsHelper;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-07.
 */
public class PoiDetailsView extends LinearLayout {

	private PoiDetailsInterface mPoiDetailsInterface;

	private Poi mPoi;
	private int mSelectedTab = 1;
	private int mSelectedCategoryTab;
	private boolean mMeasured = false;

	private EditText mCommentsFooter;
	private TextView mCommentsHeader;

	private DisplayImageOptions mOptions;

	public PoiDetailsView(Context context) {
		super(context);
		init();
	}

	public PoiDetailsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PoiDetailsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_poi_details, this, true);

		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

		initCloseButton();
		//init fonts
		if (!isInEditMode()) {
			FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
			helper.loadFont((TextView) findViewById(R.id.poi_name), FontHelper.FONT.EXO_MEDIUM);
			helper.loadFont((TextView) findViewById(R.id.bookmarks_name), FontHelper.FONT.EXO_BOLD_ITALIC);
			helper.loadFont((TextView) findViewById(R.id.pin), FontHelper.FONT.EXO_ITALIC);
			helper.loadFont((TextView) findViewById(R.id.phone), FontHelper.FONT.EXO_ITALIC);
			helper.loadFont((TextView) findViewById(R.id.mail), FontHelper.FONT.EXO_ITALIC);
			helper.loadFont((TextView) findViewById(R.id.tab_menu), FontHelper.FONT.EXO_BOLD);
			helper.loadFont((TextView) findViewById(R.id.research_text), FontHelper.FONT.EXO_MEDIUM);
			helper.loadFont((TextView) findViewById(R.id.post_text), FontHelper.FONT.EXO_MEDIUM);

            helper.loadFont((TextView) findViewById(R.id.poi_description_value), FontHelper.FONT.EXO_MEDIUM);

            helper.loadFont((TextView) findViewById(R.id.poi_site_title), FontHelper.FONT.EXO_BOLD);
            helper.loadFont((TextView) findViewById(R.id.poi_site_value), FontHelper.FONT.EXO_MEDIUM);

            helper.loadFont((TextView) findViewById(R.id.poi_welcome_title), FontHelper.FONT.EXO_BOLD);
            helper.loadFont((TextView) findViewById(R.id.poi_welcome_value), FontHelper.FONT.EXO_MEDIUM);

            helper.loadFont((TextView) findViewById(R.id.poi_disciplines_title), FontHelper.FONT.EXO_BOLD);
            helper.loadFont((TextView) findViewById(R.id.poi_disciplines_value), FontHelper.FONT.EXO_MEDIUM);

            helper.loadFont((TextView) findViewById(R.id.poi_work_hours_title), FontHelper.FONT.EXO_BOLD);
            helper.loadFont((TextView) findViewById(R.id.poi_work_hours_value), FontHelper.FONT.EXO_MEDIUM);

            helper.loadFont((TextView) findViewById(R.id.poi_close_hours_title), FontHelper.FONT.EXO_BOLD);
            helper.loadFont((TextView) findViewById(R.id.poi_close_hours_value), FontHelper.FONT.EXO_MEDIUM);

            helper.loadFont((TextView) findViewById(R.id.poi_floor_title), FontHelper.FONT.EXO_BOLD);
            helper.loadFont((TextView) findViewById(R.id.poi_floor_value), FontHelper.FONT.EXO_MEDIUM);

            helper.loadFont((TextView) findViewById(R.id.poi_acces_title), FontHelper.FONT.EXO_BOLD);
            helper.loadFont((TextView) findViewById(R.id.poi_acces_value), FontHelper.FONT.EXO_MEDIUM);

		}

	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (findViewById(R.id.tabs_container).getWidth() > 0 && !mMeasured) {
			mMeasured = true;
			initTabsSize();
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void clear() {
		mPoiDetailsInterface = null;
	}

	public void populate(PoiDetailsInterface listener, Poi poi, Category cat, int categoryTab, boolean isBookmarked) {
		mPoiDetailsInterface = listener;
		mPoi = poi;
		mOnTabClickListener.onClick(findViewById(R.id.tab_info));
		clearComments();
		mSelectedCategoryTab = categoryTab;
		initHeader(poi, mSelectedCategoryTab);
		//initComments(comments, tab);
		initColors(mSelectedCategoryTab);
		initTabs();
		initCategoryIcon(cat);
		show();

		initBookmark(isBookmarked);


	}

	public Poi getPoi() {
		return mPoi;
	}

	public void loadFinished(boolean isBookmarked) {
		initBookmark(isBookmarked);
		findViewById(R.id.loading).setVisibility(View.GONE);
	}

	private void initBookmark(boolean isBookmarked) {
		TextView bookarkText = (TextView) findViewById(R.id.bookmarks_name);
		if (isBookmarked) {
			bookarkText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_clear_small);
			bookarkText.setOnClickListener(mOnBookmarkRemoveClickListener);
		} else {

			bookarkText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_bookmark);
			bookarkText.setOnClickListener(mOnBookmarkClickListener);
		}
	}

	public void populateComments(List<Comment> comments) {
		View progress = findViewById(R.id.progressBar1);
		progress.setVisibility(View.GONE);

		ListView list = (ListView) findViewById(R.id.comments_list);
		list.setVisibility(View.VISIBLE);

		int top_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, getResources().getDisplayMetrics());
		int right_left_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
		int bottom_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

		if (comments == null) {
			comments = new ArrayList<Comment>();
		}

		CommentsAdapter adapter = new CommentsAdapter(getContext(), comments, mSelectedCategoryTab);

		if (comments.size() == 0 && mCommentsHeader == null) {
			mCommentsHeader = new TextView(getContext());
			mCommentsHeader.setText(getResources().getString(R.string.no_results));
			mCommentsHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
			mCommentsHeader.setTextColor(ColorsHelper.getColorByTab(getContext(), false, mSelectedCategoryTab));
			mCommentsHeader.setGravity(Gravity.CENTER_HORIZONTAL);
			mCommentsHeader.setPadding(0, 50, 0, 50);
			((UnivMobileApp) getContext().getApplicationContext()).getFontHelper().loadFont(mCommentsHeader, FontHelper.FONT.EXO_MEDIUM);
			list.addHeaderView(mCommentsHeader);
		}

		list.setAdapter(adapter);

		if (mCommentsFooter == null) {
			mCommentsFooter = new EditText(getContext());
			mCommentsFooter.setBackgroundResource(ColorsHelper.getAddCommentBackgroundResource(mSelectedCategoryTab));

			mCommentsFooter.setGravity(Gravity.TOP);
			mCommentsFooter.setPadding(right_left_px, top_px, right_left_px, bottom_px);
			mCommentsFooter.setTextColor(ColorsHelper.getColorByTab(getContext(), false, mSelectedCategoryTab));
			mCommentsFooter.setMinLines(3);

			mCommentsFooter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
			((UnivMobileApp) getContext().getApplicationContext()).getFontHelper().loadFont(mCommentsFooter, FontHelper.FONT.EXO_MEDIUM);

			list.addFooterView(mCommentsFooter);
		} else {
			mCommentsFooter.setText("");
		}
	}

	public void populateRestoMenu(List<RestoMenu> menus) {
		if (menus == null || menus.size() == 0) {
			findViewById(R.id.tab_menu).setVisibility(View.GONE);
		} else {
			findViewById(R.id.tab_menu).setVisibility(View.VISIBLE);
			WebView webView = (WebView) findViewById(R.id.resto_menu_container);
			String html = "<html><head></head><body>";
			for (RestoMenu m : menus) {
				html += m.getDescription();
			}
			html += "</body></html>";
			webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");
		}
	}

	private void clearComments() {
		View progress = findViewById(R.id.progressBar1);
		if (progress.getVisibility() != View.VISIBLE) {
			ListView list = (ListView) findViewById(R.id.comments_list);
			if (mCommentsFooter != null) {
				list.removeFooterView(mCommentsFooter);
				mCommentsFooter = null;
			}
			if (mCommentsHeader != null) {
				list.removeHeaderView(mCommentsHeader);
				mCommentsFooter = null;
			}
			progress.setVisibility(View.VISIBLE);
			list.setVisibility(View.INVISIBLE);
		}

	}

	private void initHeader(Poi poi, int tab) {
		TextView name = (TextView) findViewById(R.id.poi_name);
		name.setText(poi.getName());

		TextView pin = (TextView) findViewById(R.id.pin);
		pin.setText(poi.getAddress());

		TextView phone = (TextView) findViewById(R.id.phone);
		phone.setText(poi.getPhones());

		TextView mail = (TextView) findViewById(R.id.phone);
		mail.setText(poi.getEmail());


        //init description
		TextView description = (TextView) findViewById(R.id.poi_description_value);
		if (poi.getDescription() != null && poi.getDescription().length() > 0) {
			description.setText(Html.fromHtml(poi.getDescription()));
		} else {
			description.setVisibility(View.GONE);
		}

        TextView siteTitle = (TextView) findViewById(R.id.poi_site_title);
        TextView siteValue = (TextView) findViewById(R.id.poi_site_value);
        if (poi.getUrl() != null && poi.getUrl().length() > 0) {
            siteValue.setText(Html.fromHtml(poi.getUrl()));
        } else {
            siteTitle.setVisibility(View.GONE);
            siteValue.setVisibility(View.GONE);
        }

        TextView welcomeTitle = (TextView) findViewById(R.id.poi_welcome_title);
        TextView welcomeValue = (TextView) findViewById(R.id.poi_welcome_value);
        if (poi.getPublicWelcome() != null && poi.getPublicWelcome().length() > 0) {
            welcomeValue.setText(Html.fromHtml(poi.getPublicWelcome()));
        } else {
            welcomeTitle.setVisibility(View.GONE);
            welcomeValue.setVisibility(View.GONE);
        }

        TextView disciplinesTitle = (TextView) findViewById(R.id.poi_disciplines_title);
        TextView disciplinesValue = (TextView) findViewById(R.id.poi_disciplines_value);
        if (poi.getDisciplines() != null && poi.getDisciplines().length() > 0) {
            disciplinesValue.setText(Html.fromHtml(poi.getDisciplines()));
        } else {
            disciplinesTitle.setVisibility(View.GONE);
            disciplinesValue.setVisibility(View.GONE);
        }

        TextView workHoursTitle = (TextView) findViewById(R.id.poi_work_hours_title);
        TextView workHoursValue = (TextView) findViewById(R.id.poi_work_hours_value);
        if (poi.getOpeningHours() != null && poi.getOpeningHours().length() > 0) {
            workHoursValue.setText(Html.fromHtml(poi.getOpeningHours()));
        } else {
            workHoursTitle.setVisibility(View.GONE);
            workHoursValue.setVisibility(View.GONE);
        }

        TextView closeHoursTitle = (TextView) findViewById(R.id.poi_close_hours_title);
        TextView closeHoursValue = (TextView) findViewById(R.id.poi_close_hours_value);
        if (poi.getClosingHours() != null && poi.getClosingHours().length() > 0) {
            closeHoursValue.setText(Html.fromHtml(poi.getClosingHours()));
        } else {
            closeHoursTitle.setVisibility(View.GONE);
            closeHoursValue.setVisibility(View.GONE);
        }

        TextView floorTitle = (TextView) findViewById(R.id.poi_floor_title);
        TextView floorValue = (TextView) findViewById(R.id.poi_floor_value);
        if (poi.getFloor() != null && poi.getFloor().length() > 0) {
            floorValue.setText(Html.fromHtml(poi.getFloor()));
        } else {
            floorTitle.setVisibility(View.GONE);
            floorValue.setVisibility(View.GONE);
        }

        TextView accessTitle = (TextView) findViewById(R.id.poi_acces_title);
        TextView accessValue = (TextView) findViewById(R.id.poi_acces_value);
        if (poi.getItinerary() != null && poi.getItinerary().length() > 0) {
            accessValue.setText(Html.fromHtml(poi.getItinerary()));
        } else {
            accessTitle.setVisibility(View.GONE);
            accessValue.setVisibility(View.GONE);
        }

	}

	private void initColors(int tab) {
		findViewById(R.id.title_container).setBackgroundColor(ColorsHelper.getColorByTab(getContext(), true, tab));
		findViewById(R.id.info_container).setBackgroundColor(ColorsHelper.getColorByTab(getContext(), false, tab));
		findViewById(R.id.seperator_line).setBackgroundColor(ColorsHelper.getColorByTab(getContext(), false, tab));

        int color = ColorsHelper.getColorByTab(getContext(), false, tab);
        ((TextView) findViewById(R.id.poi_description_value)).setTextColor(color);

        ((TextView) findViewById(R.id.poi_site_title)).setTextColor(color);
        ((TextView) findViewById(R.id.poi_site_value)).setTextColor(color);

        ((TextView) findViewById(R.id.poi_welcome_title)).setTextColor(color);
        ((TextView) findViewById(R.id.poi_welcome_value)).setTextColor(color);

        ((TextView) findViewById(R.id.poi_disciplines_title)).setTextColor(color);
        ((TextView) findViewById(R.id.poi_disciplines_value)).setTextColor(color);

        ((TextView) findViewById(R.id.poi_work_hours_title)).setTextColor(color);
        ((TextView) findViewById(R.id.poi_work_hours_value)).setTextColor(color);

        ((TextView) findViewById(R.id.poi_close_hours_title)).setTextColor(color);
        ((TextView) findViewById(R.id.poi_close_hours_value)).setTextColor(color);

        ((TextView) findViewById(R.id.poi_floor_title)).setTextColor(color);
        ((TextView) findViewById(R.id.poi_floor_value)).setTextColor(color);

        ((TextView) findViewById(R.id.poi_acces_title)).setTextColor(color);
        ((TextView) findViewById(R.id.poi_acces_value)).setTextColor(color);

		findViewById(R.id.bookmarks_name).setBackgroundResource(ColorsHelper.getBookmarksBackgroundResource(tab));
	}


	public void hide() {
		final int height = getHeight();
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				setY((height * interpolatedTime));
				requestLayout();
				if (interpolatedTime == 1) {
					setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 0.5dp/ms
		a.setDuration((int) (height * 2 / getResources().getDisplayMetrics().density));
		startAnimation(a);
	}

	public void show() {
		setVisibility(View.VISIBLE);
		final int height = getHeight();
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				setY(height - (height * interpolatedTime));
				requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 0.5dp/ms
		a.setDuration((int) (height * 2 / getResources().getDisplayMetrics().density));
		startAnimation(a);
	}

	private void initCloseButton() {
		findViewById(R.id.close).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hide();
			}
		});
	}

	private void initTabsContent() {


		View info = findViewById(R.id.poi_description_container);
		View comments = findViewById(R.id.comments_container);
		View webview = findViewById(R.id.resto_menu_container);

		info.setVisibility(View.GONE);
		comments.setVisibility(View.GONE);
		webview.setVisibility(View.GONE);

		View v = findViewById(R.id.research_container);
		v.setOnClickListener(mPostCommentClickListener);
		TextView search = (TextView) findViewById(R.id.research_text);
		TextView post = (TextView) findViewById(R.id.post_text);

		search.setVisibility(View.INVISIBLE);
		post.setVisibility(View.INVISIBLE);
		switch (mSelectedTab) {
			case 1:
				v.setBackgroundResource(R.drawable.selector_color_gray);
				search.setVisibility(View.VISIBLE);
				info.setVisibility(View.VISIBLE);
				break;
			case 2:
				v.setBackgroundResource(R.drawable.selector_color_red);
				post.setVisibility(View.VISIBLE);
				comments.setVisibility(View.VISIBLE);
				break;
			case 3:
				v.setBackgroundResource(R.drawable.selector_color_gray);
				search.setVisibility(View.VISIBLE);
				webview.setVisibility(View.VISIBLE);
				break;
		}
	}

	private void initTabsSize() {

		if (mCommentsFooter != null) {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mCommentsFooter.getWindowToken(), 0);
		}


		View v = findViewById(R.id.tabs_container);
		View tabInfo = findViewById(R.id.tab_info);
		View tabComments = findViewById(R.id.tab_comments);
		View tabMenu = findViewById(R.id.tab_menu);

		tabInfo.setSelected(false);
		tabComments.setSelected(false);
		tabMenu.setSelected(false);

		tabInfo.setOnClickListener(mOnTabClickListener);
		tabComments.setOnClickListener(mOnTabClickListener);
		tabMenu.setOnClickListener(mOnTabClickListener);

		int width = v.getWidth();

		int widthFirst = 0;
		int widthSecond = 0;
		if (mSelectedTab == 1) {
			tabInfo.setSelected(true);
			widthFirst = (int) (width * 0.6);
			widthSecond = (int) (width * 0.4);
		} else if (mSelectedTab == 2) {
			tabComments.setSelected(true);
			widthFirst = (int) (width * 0.4);
			widthSecond = (int) (width * 0.6);
		} else if (mSelectedTab == 3) {
			tabMenu.setSelected(true);
			widthFirst = (int) (width * 0.5);
			widthSecond = widthFirst;
		}

		tabComments.setX(widthFirst + ((RelativeLayout.LayoutParams) tabComments.getLayoutParams()).leftMargin);
		tabComments.getLayoutParams().width = widthSecond;
		tabInfo.getLayoutParams().width = widthFirst;
		tabComments.requestLayout();
		tabInfo.requestLayout();
		requestLayout();
	}

	private void initCategoryIcon(Category cat) {

		ImageView catImage = (ImageView) findViewById(R.id.category_image);
		catImage.setBackgroundResource(ColorsHelper.getCircleByTab(getContext(), mSelectedCategoryTab));
		catImage.setImageDrawable(null);
		if (cat != null && cat.getActiveIconUrl() != null) {
			ImageLoader.getInstance().displayImage(ReadCategoriesOperation.CATEGORIES_IMAGE_URL + cat.getInactiveIconUrl(), catImage, mOptions);
		}
	}

	private void initTabs() {
		if (mSelectedCategoryTab != 2) {
			findViewById(R.id.tab_comments).setVisibility(View.GONE);
		} else {
			findViewById(R.id.tab_comments).setVisibility(View.VISIBLE);
		}
	}

	private OnClickListener mOnTabClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int selectedTab = 1;
			switch (v.getId()) {
				case R.id.tab_info:
					selectedTab = 1;
					break;
				case R.id.tab_comments:
					selectedTab = 2;
					break;
				case R.id.tab_menu:
					selectedTab = 3;
					break;
			}
			if (selectedTab != mSelectedTab) {
				mSelectedTab = selectedTab;
				initTabsSize();
				initTabsContent();
			}
		}
	};

	private OnClickListener mOnBookmarkRemoveClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mPoiDetailsInterface != null) {
				findViewById(R.id.loading).setVisibility(View.VISIBLE);
				mPoiDetailsInterface.removeBookmark(mPoi);
			}
		}
	};
	private OnClickListener mOnBookmarkClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mPoiDetailsInterface != null) {
				findViewById(R.id.loading).setVisibility(View.VISIBLE);
				mPoiDetailsInterface.postBookmark(mPoi);
			}
		}
	};
	private OnClickListener mPostCommentClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mSelectedTab == 2) {
				if (mPoiDetailsInterface != null) {
					if (mCommentsFooter != null && mCommentsFooter.getText().length() > 3) {
						findViewById(R.id.loading).setVisibility(View.VISIBLE);
						mPoiDetailsInterface.postComment(mCommentsFooter.getText().toString(), mPoi);
					}
				}
			}
		}
	};

	public interface PoiDetailsInterface {
		void postComment(String comment, Poi poi);

		void postBookmark(Poi poi);

		void removeBookmark(Poi poi);
	}
}
