package org.unpidf.univmobile.ui.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.models.ModelListener;
import org.unpidf.univmobile.ui.fragments.SimpleDialogFragment;

/**
 * Created by Rokas on 2015-03-13.
 */
public abstract class AbsActivity extends Activity {


	protected void handleError(ErrorEntity mError) {
		if (mError.getmErrorType() == ErrorEntity.ERROR_TYPE.NETWORK_ERROR) {
			((UnivMobileApp) getApplicationContext()).showErrorDialog(getFragmentManager());
		}
	}
}
