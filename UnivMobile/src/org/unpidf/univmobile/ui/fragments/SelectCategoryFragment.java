package org.unpidf.univmobile.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadCategoriesOperation;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.views.CategoryItemView;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-24.
 */
public class SelectCategoryFragment extends AbsFragment {

	private static final String ARG_ROOT_CAT_ID = "arg_root_cat_id";


	private int mRootCatID;
	private ReadCategoriesOperation mReadCategoriesOperation;

	private List<Category> mCategories;
	private DisplayImageOptions mOptions;


	public static SelectCategoryFragment newInstance(int rootCatId) {
		SelectCategoryFragment fragment = new SelectCategoryFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_ROOT_CAT_ID, rootCatId);
		fragment.setArguments(args);
		return fragment;
	}

	public SelectCategoryFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mRootCatID = getArguments().getInt(ARG_ROOT_CAT_ID);
		}

		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mReadCategoriesOperation.clear();
		mReadCategoriesOperation = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_select_category, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

//		University univ = UniversitiesDataModel.getSavedUniversity(getActivity());
//		TextView current = (TextView) view.findViewById(R.id.current_univ_value);
//		current.setText(univ.getTitle());
//
////		mReadPoisOperation = new ReadPoisOperation(getActivity(), mReadPoisOperationListener, cat, -1, univID, null);
////		mReadPoisOperation.startOperation();
//		//init fonts
//		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
//		helper.loadFont((android.widget.TextView) view.findViewById(R.id.current_univ_title), FontHelper.FONT.EXO_ITALIC);
//		helper.loadFont((android.widget.TextView) view.findViewById(R.id.choose_univ), FontHelper.FONT.EXO_ITALIC);
//		helper.loadFont(current, FontHelper.FONT.EXO_REGULAR);

		mReadCategoriesOperation = new ReadCategoriesOperation(getActivity(), mReadCategoriesOperationListener, mRootCatID);
		mReadCategoriesOperation.startOperation();
	}


	private org.unpidf.univmobile.data.operations.OperationListener<List<Category>> mReadCategoriesOperationListener = new OperationListener<List<Category>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, final List<Category> result) {
			if (result != null && result.size() > 0) {
				mCategories = result;
				getView().findViewById(R.id.loading).setVisibility(View.GONE);
				initCategories(result);

			}
		}

		@Override
		public void onPageDownloaded(List<Category> result) {

		}
	};

	private void initCategories(List<Category> categories) {

		LinearLayout container = (LinearLayout) getView().findViewById(R.id.categories_container);
		if (categories != null && categories.size() > 0) {
			for (int i = 0; i < categories.size(); i += 3) {
				LinearLayout l = new LinearLayout(getActivity());
				l.setOrientation(LinearLayout.HORIZONTAL);
				l.setWeightSum(3);

				initCategoryView(categories, i, l);
				initCategoryView(categories, i + 1, l);
				initCategoryView(categories, i + 2, l);

				LinearLayout.LayoutParams paramsContainer = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				container.addView(l, paramsContainer);
			}

		}
	}

	private void initCategoryView(List<Category> categories, int position, LinearLayout container) {
		if (categories.size() > position) {
			CategoryItemView item1 = new CategoryItemView(getActivity());
			item1.setBackgroundResource(R.drawable.selector_color_green);
			item1.setOnClickListener(mOnCategoryClickListener);
			item1.setTag(categories.get(position));
			item1.setCategoryText(categories.get(position).getName());
			if (categories.get(position).getActiveIconUrl() != null && categories.get(position).getActiveIconUrl().length() > 0) {
				ImageLoader.getInstance().displayImage(ReadCategoriesOperation.CATEGORIES_IMAGE_URL + categories.get(position).getInactiveIconUrl(), item1.getImageView(), mOptions);
			}
			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, RelativeLayout.LayoutParams.WRAP_CONTENT, 1);
			container.addView(item1, params1);
		}
	}

	private View.OnClickListener mOnCategoryClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			HomeActivity a = (HomeActivity) getActivity();
			a.categorySelected((Category) v.getTag());
		}
	};
}
