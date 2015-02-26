package org.unpidf.univmobile.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.models.SearchPoisDataModel;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.adapters.SearchResultsAdapter;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoisSearchFragment extends AbsFragment {

	private SearchPoisDataModel mModel;
	private SearchResultsAdapter mAdapter;

	public static PoisSearchFragment newInstance() {
		return new PoisSearchFragment();
	}

	public PoisSearchFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mModel = new SearchPoisDataModel(getActivity(), mDataModelListener);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_pois_search, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		EditText search_value = (EditText) view.findViewById(R.id.search_value);
		search_value.addTextChangedListener(mTextWatcher);
		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont(search_value, FontHelper.FONT.EXO2_REGULAR);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.title), FontHelper.FONT.EXO2_LIGHT);

		ListView list = (ListView) view.findViewById(R.id.list);

		mAdapter = new SearchResultsAdapter(getActivity(), mModel.getPois());
		list.setAdapter(mAdapter);
		list.setOnItemClickListener(mOnItemClickListener);

		view.findViewById(R.id.clear).setOnClickListener(mClearClickListener);
		view.findViewById(R.id.back).setOnClickListener(mBackClickListener);
	}

	private TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			mModel.searchPois(s.toString());
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			HomeActivity a = (HomeActivity) getActivity();
			a.showPoi(mAdapter.getItem(position), true);
		}
	};
	private View.OnClickListener mClearClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText edit = (EditText) getView().findViewById(R.id.search_value);
			edit.setText("");
		}
	};

	private View.OnClickListener mBackClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText myEditText = (EditText) getView().findViewById(R.id.search_value);
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);

			getActivity().onBackPressed();
		}
	};

	private SearchPoisDataModel.SearchPoisModelListener mDataModelListener = new SearchPoisDataModel.SearchPoisModelListener() {
		@Override
		public void showSearchResults(List<Poi> pois) {

			mAdapter.clear();
			if (pois != null) {

				mAdapter.addAll(pois);
			}
			mAdapter.notifyDataSetChanged();
		}
	};
}
