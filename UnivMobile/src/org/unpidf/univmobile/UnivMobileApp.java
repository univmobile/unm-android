package org.unpidf.univmobile;

import android.app.Application;

import org.unpidf.univmobile.ui.uiutils.FontHelper;

/**
 * Created by Rokas on 2015-01-31.
 */
public class UnivMobileApp extends Application {
	private FontHelper mFontHelper;

	public FontHelper getFontHelper() {
		if (mFontHelper == null) {
			mFontHelper = new FontHelper(this);
		}
		return mFontHelper;
	}

}
