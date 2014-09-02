package org.unpidf.univmobile.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import static android.content.pm.PackageManager.GET_META_DATA;

import android.os.Bundle;

public class MappingManager {

    public static final String DIR_DATA = "/Android/data/org.unpidf.univmobile";
    private static String URL = null;
    private static String APP_VERSION = null;

    public static String getUrlApi(Context context) {
        if (URL != null) {
            return URL;
        } else {
            URL = getMetadata(context, "jsonURL");
            return URL;
        }
    }

    public static String getUrlRegions(Context context) {
        return getUrlApi(context) + "regions";
    }

    public static String getUrlPois(Context context) {
        return getUrlApi(context) + "pois";
    }

    public static String getAppVersion(Context context) {
        if (APP_VERSION != null) {
            return APP_VERSION;
        } else {
            APP_VERSION = getMetadata(context, "appVersion");
            return APP_VERSION;
        }
    }

    private static String getMetadata(final Context context, final String name) {

        final PackageManager pm = context.getPackageManager();

        final ApplicationInfo ai;

        try {

            ai = pm.getApplicationInfo(context.getPackageName(), GET_META_DATA);

        } catch (final NameNotFoundException e) {

            e.printStackTrace();

            return null;
        }

        final Bundle bundle = ai.metaData;

        return bundle.getString(name);
    }
}
