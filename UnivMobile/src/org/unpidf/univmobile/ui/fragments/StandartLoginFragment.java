package org.unpidf.univmobile.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.models.LoginDataModel;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.uiutils.FontHelper;


public class StandartLoginFragment extends AbsFragment {

	private LoginDataModel mLoginDataModel;

	public static StandartLoginFragment newInstance() {
		StandartLoginFragment fragment = new StandartLoginFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public StandartLoginFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mLoginDataModel != null) {
			mLoginDataModel.clear();
			mLoginDataModel = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_standard_login, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);


		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.title), FontHelper.FONT.EXO_SEMI_BOLD);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.email_title), FontHelper.FONT.EXO_SEMI_BOLD);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.email_value), FontHelper.FONT.EXO_SEMI_BOLD);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.password_title), FontHelper.FONT.EXO_SEMI_BOLD);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.password_value), FontHelper.FONT.EXO_SEMI_BOLD);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.accept_button), FontHelper.FONT.EXO_SEMI_BOLD);


		view.findViewById(R.id.accept_button).setOnClickListener(mOnLoginButtonClickListener);
		((EditText) getView().findViewById(R.id.email_value)).setOnEditorActionListener(mOnEditorActionListener);
		((EditText) getView().findViewById(R.id.password_value)).setOnEditorActionListener(mOnEditorActionListener);
		view.findViewById(R.id.main_container).setOnClickListener(mOnHideKeyboardClickListener);
	}

	private void login() {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((EditText) getView().findViewById(R.id.password_value)).getWindowToken(), 0);

		String name = ((EditText) getView().findViewById(R.id.email_value)).getText().toString();
		String password = ((EditText) getView().findViewById(R.id.password_value)).getText().toString();

		if (name.length() > 3 && password.length() > 3) {
			mLoginDataModel = new LoginDataModel(getActivity());
			mLoginDataModel.standardLogin(mLoginDataModelListener, name, password);
		}
	}


	private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			boolean handled = false;
			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				((EditText) getView().findViewById(R.id.password_value)).requestFocus();
				handled = true;
			} else if (actionId == EditorInfo.IME_ACTION_SEND) {
				login();
				handled = true;
			}
			return handled;
		}
	};

	private View.OnClickListener mOnLoginButtonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			login();
		}
	};

	private View.OnClickListener mOnHideKeyboardClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText myEditText = (EditText) getView().findViewById(R.id.email_value);
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		}
	};

	private LoginDataModel.StandardLoginDataModelListener mLoginDataModelListener = new LoginDataModel.StandardLoginDataModelListener() {
		@Override
		public void showLoadingIndicator() {
			getView().findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
		}

		@Override
		public void loginSuccessful(Login login) {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
			((HomeActivity) getActivity()).logedIn(login);

		}

		@Override
		public void loginFailed(ErrorEntity error) {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
		}


		@Override
		public void onError(ErrorEntity mError) {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
			handleError(mError);
		}
	};


}
