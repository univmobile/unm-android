package org.unpidf.univmobile;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.unpidf.univmobile.data.entities.Login;
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

	public Login getmLogin() {
		return mLogin;
	}

	public void setmLogin(Login mLogin) {
		this.mLogin = mLogin;
	}
}
