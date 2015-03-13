package org.unpidf.univmobile.ui.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;

import com.flurry.android.FlurryAgent;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.models.ModelListener;

/**
 * Created by rviewniverse on 2015-02-11.
 */
public abstract class AbsFragment extends Fragment {


	public boolean onBackPressed() {
		return false;
	}

	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(getActivity(), getActivity().getString(R.string.flurry_key));
	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(getActivity());
	}


	protected void handleError(ErrorEntity mError) {
		if (mError.getmErrorType() == ErrorEntity.ERROR_TYPE.NETWORK_ERROR) {
			((UnivMobileApp) getActivity().getApplicationContext()).showErrorDialog(getFragmentManager());
		}
	}
}
