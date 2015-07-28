package org.unpidf.univmobile;

import android.app.Application;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;

import io.fabric.sdk.android.Fabric;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.repositories.SharedPreferencesRepo;
import org.unpidf.univmobile.ui.fragments.SimpleDialogFragment;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.Objects;

/**
 * Created by rviewniverse on 2015-01-31.
 */
public class UnivMobileApp extends Application {
	private FontHelper mFontHelper;
	private Login mLogin;

	private static Handler mHandler = new Handler();
	private SimpleDialogFragment mDialogFragment;
	private boolean mPreparingErrorDialog;
	private Object mLock1 = new Object();

	@Override
	public void onCreate() {
		super.onCreate();
		FlurryAgent.init(this, getString(R.string.flurry_key));
		Fabric.with(this, new Crashlytics());
		initLogin();
	}


	public FontHelper getFontHelper() {
		if (mFontHelper == null) {
			mFontHelper = new FontHelper(this);
		}
		return mFontHelper;
	}

	public void logout() {
		mLogin = null;
		SharedPreferencesRepo.saveString(this, "login_name", null);
		SharedPreferencesRepo.saveString(this, "login_token", null);
		SharedPreferencesRepo.saveString(this, "login_id", null);
	}

	public void initLogin() {
		String token = SharedPreferencesRepo.getString(this, "login_token");
		if (token != null) {
			String name = SharedPreferencesRepo.getString(this, "login_name");
			String id = SharedPreferencesRepo.getString(this, "login_id");
			mLogin = new Login();
			mLogin.setToken(token);
			mLogin.setName(name);
			mLogin.setId(id);
		}
	}

	public Login getLogin() {
		return mLogin;
	}

	public void setLogin(Login mLogin) {
		SharedPreferencesRepo.saveString(this, "login_name", mLogin.getName());
		SharedPreferencesRepo.saveString(this, "login_token", mLogin.getToken());
		SharedPreferencesRepo.saveString(this, "login_id", mLogin.getId());
		this.mLogin = mLogin;
	}

	public void showErrorDialog(FragmentManager manager) {
		synchronized (mLock1) {
			if (!mPreparingErrorDialog) {
				mPreparingErrorDialog = true;
				if (mDialogFragment == null || !mDialogFragment.isVisible()) {
					FragmentTransaction ft = manager.beginTransaction();
					mDialogFragment = SimpleDialogFragment.newInstance(getString(R.string.no_internet));
					mDialogFragment.show(ft, "dialog");
				}

				mHandler.postDelayed(mRemovePreparedRunnable, 1000);
			}

		}
	}

	public Runnable mRemovePreparedRunnable = new Runnable() {
		@Override
		public void run() {
			synchronized (mLock1) {
				mPreparingErrorDialog = false;
			}
		}
	};
}
