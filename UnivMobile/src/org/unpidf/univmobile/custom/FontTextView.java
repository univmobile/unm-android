package org.unpidf.univmobile.custom;

import java.util.HashMap;

import org.unpidf.univmobile.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextView extends TextView {

	private static HashMap<String, Typeface> listFont = new HashMap<String, Typeface>();

	public FontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			final TypedArray attributes = context.obtainStyledAttributes(attrs,	R.styleable.FontTextView);
			String ttfName = attributes.getString(R.styleable.FontTextView_ttfName);
			if(ttfName != null){
				setTypeface(getFont(ttfName, context));
			}
			attributes.recycle();
		} else {
			setTextSize(getTextSize() / 2);
		}
	}
	
	public static Typeface getFont(String fontName, Context context) {
		if (listFont.get(fontName) == null) {
			listFont.put(fontName, Typeface.createFromAsset(context.getAssets(), "fonts/"+ fontName));
		}
		return listFont.get(fontName);
	}

}