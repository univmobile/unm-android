package org.unpidf.univmobile.ui.fragments;

import android.app.Activity;
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
import org.unpidf.univmobile.data.entities.ShibbolethPrepare;
import org.unpidf.univmobile.data.models.LoginDataModel;

/**
 * Created by rviewniverse on 2015-01-13.
 */
public class ShibbolethLoginFragment extends AbsFragment {

	private WebView webView;
	private LoginDataModel mLoginModel;

	private ShibbolethPrepare mShibbolethPrepare;


	public static ShibbolethLoginFragment newInstance() {
		ShibbolethLoginFragment fragment = new ShibbolethLoginFragment();
		Bundle args = new Bundle();
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
	public void onDestroyView() {
		super.onDestroyView();
		if (mLoginModel != null) {
			mLoginModel.clear();
			mLoginModel = null;
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initWebView();
		mLoginModel = new LoginDataModel(getActivity());
		mLoginModel.prepareShibboleth(mShibbolethLoginListener);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
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
			if (url.endsWith("success.sso")) {
				Toast.makeText(getActivity(), "Succès !!!", Toast.LENGTH_SHORT).show();
				mLoginModel.retrieveShibboleth(mShibbolethLoginListener, mShibbolethPrepare);
//				view.setVisibility(View.GONE);
			}
			view.loadUrl(url);
			return true;
		}
	};


	private LoginDataModel.ShibbolethLoginDadaModelListener mShibbolethLoginListener = new LoginDataModel.ShibbolethLoginDadaModelListener() {

		@Override
		public void shibbolethPreparedSuccessfully(ShibbolethPrepare prepare) {
			mShibbolethPrepare = prepare;
			String url = mLoginModel.getShibbolethLoginUrl(prepare.getToken());
			System.out.println("---URL WEBVIEW= " + url);
			webView.loadUrl(url);
		}

		@Override
		public void shibbolethPrepareFailed() {
			Toast.makeText(getActivity(), "Erreur...", Toast.LENGTH_SHORT).show();
		}
	};
}

