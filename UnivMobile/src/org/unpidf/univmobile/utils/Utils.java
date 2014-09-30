package org.unpidf.univmobile.utils;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class Utils {

	public static int convertDpToPixel(float dp, Resources resources) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
	}

	public static int convertPixelToDp(float px, Resources resources) {
		DisplayMetrics metrics = resources.getDisplayMetrics();
		int dp = (int) (px / (metrics.densityDpi / 160f));
		return dp;
	}

	@SuppressLint("NewApi")
	public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task, P... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}

	public static boolean isIntentAvailable(String uri, Context context) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public static int getStatusBarHeight(Context context) { 
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		} 
		return result;
	}

	public static boolean isConnected(Context c) {
		ConnectivityManager cm =(ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static boolean isPrefs(String pref, String prefName, Context context) {
		if(context == null){
			return false;
		}
		SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return prefs.getBoolean(pref, false);
	}

	public static void setPrefs(String pref, String prefName, boolean value, Context context) {
		if(context == null){
			return;
		}
		SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(pref, value);
		editor.commit();
	}
	
	public static double roundParam(double d, int round){
		return (double)Math.round(d * round) / round;
	}


}
