package org.unpidf.univmobile;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.repositories.SharedPreferencesRepo;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import com.crashlytics.android.Crashlytics;

/**
 * Created by rviewniverse on 2015-01-31.
 */
public class UnivMobileApp extends Application {
	private FontHelper mFontHelper;
	private Login mLogin;

	@Override
	public void onCreate() {
		super.onCreate();
		Crashlytics.start(this);
		initLogin();

		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		Tracker t = analytics.newTracker("UA-60498468-1");

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
		if(token != null) {
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
}
