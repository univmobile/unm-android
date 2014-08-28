package org.unpidf.univmobile.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class MappingManager {

	public static final String DIR_DATA = "/Android/data/org.unpidf.univmobile";
	private static String URL = null;

	public static String getUrlApi(Context context){
		if(URL != null){
			return URL;
		}else{
			try {
				ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
				Bundle bundle = ai.metaData;
				URL = bundle.getString("jsonURL");
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			return URL;
		}
	}
	
	public static String getUrlRegions(Context context){
		return getUrlApi(context) + "regions";
	}
	
	public static String getUrlPois(Context context){
		return getUrlApi(context) + "pois";
	}
	
}
