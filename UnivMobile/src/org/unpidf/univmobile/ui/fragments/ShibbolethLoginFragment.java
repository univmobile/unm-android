package org.unpidf.univmobile.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.models.LoginDataModel;
import org.unpidf.univmobile.ui.activities.HomeActivity;

/**
 * Created by Rokas on 2015-01-13.
 */
public class ShibbolethLoginFragment extends Fragment {

	private WebView webView;
	private LoginDataModel mLoginModel;

	private String mLoginToken;
	private String mKey;

	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static ShibbolethLoginFragment newInstance(int sectionNumber) {
		ShibbolethLoginFragment fragment = new ShibbolethLoginFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ShibbolethLoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_shibboleth_loginshibbolet, container, false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initWebView();
		mLoginModel = new LoginDataModel(getActivity());
		mLoginModel.prepareShibboleth(mShibbolethLoginObserver);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//((HomeActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}


	private void initWebView() {
		webView = (WebView) getView().findViewById(R.id.shibboWebView);
		CookieSyncManager.createInstance(getActivity());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();

		WebSettings ws = webView.getSettings();
		ws.setSaveFormData(false);
		webView.setWebViewClient(client);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setJavaScriptEnabled(true);
	}

	private WebViewClient client = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			System.out.println("---URL WEB = " + url);
			if (url.startsWith(LoginDataModel.TEST_URL_SUCCESS)) {
				Toast.makeText(getActivity(), "Succès !!!", Toast.LENGTH_SHORT).show();
				mLoginModel.retrieveShibboleth(mLoginToken, mKey, mShibbolethLoginObserver);
				view.setVisibility(View.GONE);
			}
			view.loadUrl(url);
			return true;
		}
	};

	private LoginDataModel.ShibbolethLoginObserver mShibbolethLoginObserver = new LoginDataModel.ShibbolethLoginObserver() {

		@Override
		public void onShibbolethConnectedSuccessful(String json) {
			Toast.makeText(getActivity(), "Connecté !", Toast.LENGTH_SHORT).show();
//				startActivity(new Intent(ConnectionShibbolethActivity.this, UserProfilActivity.class));
//				ConnectionShibbolethActivity.this.setResult(RESULT_OK);
//				finish();
		}

		@Override
		public void onShibbolethConnectedFailed() {
			Toast.makeText(getActivity(), "Echec de la connexion !", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onShibbolethPrepareSuccessful(String token, String key) {
			mLoginToken = token;
			mKey = key;
			String url = mLoginModel.getShibbolethLoginUrl(token);
			System.out.println("---URL WEBVIEW= " + url);
			webView.loadUrl(url);
		}

		@Override
		public void onShibbolethPrepareFailed() {
			Toast.makeText(getActivity(), "Erreur...", Toast.LENGTH_SHORT).show();
		}
	};
}

