package org.unpidf.univmobile.ui.activities;

import android.app.Activity;
import android.os.Bundle;

import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;

/**
 * Created by Rokas on 2015-03-13.
 */
public abstract class AbsActivity extends Activity {

    protected boolean mDestroyed;
    protected boolean mInstanceSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInstanceSaved = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        mInstanceSaved = false;
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mInstanceSaved = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDestroyed = true;
    }

    protected void handleError(ErrorEntity mError) {
        if (!mDestroyed && !mInstanceSaved) {
            if (mError.getmErrorType() == ErrorEntity.ERROR_TYPE.NETWORK_ERROR) {
                ((UnivMobileApp) getApplicationContext()).showErrorDialog(getFragmentManager());
            }
        }
    }
}
