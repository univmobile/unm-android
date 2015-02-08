package org.unpidf.univmobile.ui.uiutils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Rokas on 2015-01-31.
 */
public class FontHelper {

	public enum FONT {
		EXO_BOLD, EXO_REGULAR, EXO_SEMI_BOLD, EXO_ITALIC, EXO_MEDIUM, EXO2_REGULAR, EXO2_LIGHT
	}

	private Context mContext;

	private Typeface mExoBold;
	private Typeface mExoRegular;
	private Typeface mExoSemiBold;
	private Typeface mExoItalic;
	private Typeface mExoMedium;
	private Typeface mExo2Regular;
	private Typeface mExo2Light;

	public FontHelper(Context context) {
		mContext = context;
	}

	public void loadFont(TextView textView, FONT font) {
		Typeface typeface = getTypeFace(font);
		if (typeface != null) {
			textView.setTypeface(typeface);
		}
	}

	public Typeface getTypeFace(FONT font) {
		Typeface typeface = null;
		switch (font) {
			case EXO_BOLD:
				if (mExoBold == null) {
					mExoBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Exo-Bold.otf");
				}
				typeface = mExoBold;
				break;
			case EXO_REGULAR:
				if (mExoRegular == null) {
					mExoRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Exo-Regular.otf");
				}
				typeface = mExoRegular;
				break;
			case EXO_SEMI_BOLD:
				if (mExoSemiBold == null) {
					mExoSemiBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Exo-SemiBold.otf");
				}
				typeface = mExoSemiBold;
				break;
			case EXO_ITALIC:
				if (mExoItalic == null) {
					mExoItalic = Typeface.createFromAsset(mContext.getAssets(), "fonts/Exo-Italic.otf");
				}
				typeface = mExoItalic;
				break;
			case EXO_MEDIUM:
				if (mExoMedium == null) {
					mExoMedium = Typeface.createFromAsset(mContext.getAssets(), "fonts/Exo-Medium.otf");
				}
				typeface = mExoMedium;
				break;
			case EXO2_REGULAR:
				if (mExo2Regular == null) {
					mExo2Regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Exo2-Regular.otf");
				}
				typeface = mExo2Regular;
				break;
			case EXO2_LIGHT:
				if (mExo2Light == null) {
					mExo2Light = Typeface.createFromAsset(mContext.getAssets(), "fonts/Exo2_Light.otf");
				}
				typeface = mExo2Light;
				break;
		}
		return typeface;
	}
}
