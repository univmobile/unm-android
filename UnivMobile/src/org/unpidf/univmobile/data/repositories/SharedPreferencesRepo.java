package org.unpidf.univmobile.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;

/**
 * Created by rviewniverse on 2015-01-10.
 */
public class SharedPreferencesRepo {

	private static final String SHARED_PREF_NAME = "univmobile";

	private static SharedPreferences getPrefs(Context c) {
		return c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
	}

	private static synchronized Editor getEditor(Context c) {
		return getPrefs(c).edit();
	}

	public static void saveInt(Context c, String key, int value) {
		Editor edit = getEditor(c);
		edit.putInt(key, value);
		edit.commit();
	}

	public static void saveString(Context c, String key, String value) {
		Editor edit = getEditor(c);
		edit.putString(key, value);
		edit.commit();
	}

	public static void saveLong(Context c, String key, long value) {
		Editor edit = getEditor(c);
		edit.putLong(key, value);
		edit.commit();
	}

	public static int getInt(Context c, String key) {
		return getPrefs(c).getInt(key, -1);
	}

	public static String getString(Context c, String key) {
		return getPrefs(c).getString(key, null);
	}

	public static long getLong(Context c, String key) {
		return getPrefs(c).getLong(key, -1);
	}
}
