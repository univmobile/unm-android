package org.unpidf.univmobile;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.repositories.SharedPreferencesRepo;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

/**
 * Created by rviewniverse on 2015-01-31.
 */
public class UnivMobileApp extends Application {
	private FontHelper mFontHelper;
	private Login mLogin;

	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader();

		initLogin();
	}


	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY + 2).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
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

	public Login getmLogin() {
		return mLogin;
	}

	public void setmLogin(Login mLogin) {
		SharedPreferencesRepo.saveString(this, "login_name", mLogin.getName());
		SharedPreferencesRepo.saveString(this, "login_token", mLogin.getToken());
		SharedPreferencesRepo.saveString(this, "login_id", mLogin.getId());
		this.mLogin = mLogin;
	}
}
