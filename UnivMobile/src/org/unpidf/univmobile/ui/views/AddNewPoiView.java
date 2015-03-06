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
public class AddNewPoiView extends RelativeLayout {

	private AddNewPoiViewInterface mAddNewPoiViewInterface;

	private DisplayImageOptions mOptions;

	private Category mCat;

	public AddNewPoiView(Context context) {
		super(context);
		init();
	}

	public AddNewPoiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AddNewPoiView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_add_new_poi, this, true);
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		//init fonts
		if (!isInEditMode()) {
			FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
			helper.loadFont((TextView) findViewById(R.id.title_view), FontHelper.FONT.EXO_MEDIUM);
			helper.loadFont((TextView) findViewById(R.id.categories_select_text), FontHelper.FONT.EXO_ITALIC);
			helper.loadFont((EditText) findViewById(R.id.name_edit), FontHelper.FONT.EXO_ITALIC);
			helper.loadFont((EditText) findViewById(R.id.address), FontHelper.FONT.EXO_ITALIC);
			helper.loadFont((EditText) findViewById(R.id.phone), FontHelper.FONT.EXO_ITALIC);
			helper.loadFont((EditText) findViewById(R.id.mail), FontHelper.FONT.EXO_ITALIC);
			helper.loadFont((EditText) findViewById(R.id.description_edit), FontHelper.FONT.EXO_ITALIC);
			helper.loadFont((TextView) findViewById(R.id.post_text), FontHelper.FONT.EXO_MEDIUM);

		}
		findViewById(R.id.close).setOnClickListener(mCloseClickListener);
		findViewById(R.id.categories_select_text).setOnClickListener(mSelectCategoryClickListener);
		findViewById(R.id.categories_select_icon).setOnClickListener(mSelectCategoryClickListener);
		findViewById(R.id.post_container).setOnClickListener(mPostListner);
		findViewById(R.id.main_container).setOnClickListener(mOnHideKeyboardClickListener);
	}

	public void clear() {
		mAddNewPoiViewInterface = null;
	}

	public void categorySelected(Category cat) {
		mCat = cat;
		findViewById(R.id.categories_select_text).setVisibility(View.GONE);
		ImageView catImage = (ImageView) findViewById(R.id.categories_select_icon);
		catImage.setVisibility(View.VISIBLE);

		if (cat.getActiveIconUrl() != null) {
			ImageLoader.getInstance().displayImage(ReadCategoriesOperation.CATEGORIES_IMAGE_URL + cat.getActiveIconUrl(), catImage, mOptions);
		}

	}

	private View.OnClickListener mOnHideKeyboardClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText myEditText = (EditText) findViewById(R.id.description_edit);
			InputMethodManager imm = (InputMethodManager)  getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		}
	};


	private OnClickListener mCloseClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			clearViews();
			hide();
		}
	};

	private OnClickListener mPostListner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mSelectCategoryClickListener != null) {
				if (mCat == null) {
					mAddNewPoiViewInterface.notAllFieldFilled();
					return;
				}

				String name = ((EditText) findViewById(R.id.name_edit)).getText().toString();
				if (name == null || name.length() == 0) {
					mAddNewPoiViewInterface.notAllFieldFilled();
					return;
				}

				String address = ((EditText) findViewById(R.id.address)).getText().toString();
				if (address == null || address.length() == 0) {
					mAddNewPoiViewInterface.notAllFieldFilled();
					return;
				}

				String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
				String mail = ((EditText) findViewById(R.id.mail)).getText().toString();


				String description = ((EditText) findViewById(R.id.description_edit)).getText().toString();
				if (description == null || description.length() == 0) {
					mAddNewPoiViewInterface.notAllFieldFilled();
					return;
				}

				findViewById(R.id.loading).setVisibility(View.VISIBLE);
				mAddNewPoiViewInterface.postPoi(mCat, name, address, phone, mail, description);
			}
		}
	};

	private OnClickListener mSelectCategoryClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mAddNewPoiViewInterface != null) {
				mAddNewPoiViewInterface.selectCategory();
			}
		}
	};


	public void loadFinished() {

		findViewById(R.id.loading).setVisibility(View.GONE);
	}

	private void clearViews() {
		loadFinished();
		findViewById(R.id.categories_select_text).setVisibility(View.VISIBLE);
		ImageView catImage = (ImageView) findViewById(R.id.categories_select_icon);
		catImage.setImageDrawable(null);
		catImage.setVisibility(View.GONE);

		((EditText) findViewById(R.id.name_edit)).setText("");
		((EditText) findViewById(R.id.address)).setText("");
		((EditText) findViewById(R.id.phone)).setText("");
		((EditText) findViewById(R.id.mail)).setText("");
		((EditText) findViewById(R.id.description_edit)).setText("");

	}

	public void hide() {
		clearViews();
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

	public void show(AddNewPoiViewInterface addNewPoiViewInterface) {
		mAddNewPoiViewInterface = addNewPoiViewInterface;
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

	public interface AddNewPoiViewInterface {
		public void selectCategory();

		public void notAllFieldFilled();

		public void postPoi(Category cat, String name, String address, String phone, String mail, String description);
	}
}