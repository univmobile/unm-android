package org.unpidf.univmobile.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;

import org.unpidf.univmobile.R;


public class MyWebViewFragment extends WebViewFragment {

	private static final String ARG_URL = "arg_url";

	private String mUrl;


	public static MyWebViewFragment newInstance(String url) {
		MyWebViewFragment fragment = new MyWebViewFragment();
		Bundle args = new Bundle();
		args.putString(ARG_URL, url);
		fragment.setArguments(args);
		return fragment;
	}

	public MyWebViewFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mUrl = getArguments().getString(ARG_URL);
		}
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		WebView webView = getWebView();
		webView.loadUrl(mUrl);
		webView.setWebViewClient(mWebViewClient);
	}

	private WebViewClient mWebViewClient = new WebViewClient() {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}


	};
}
