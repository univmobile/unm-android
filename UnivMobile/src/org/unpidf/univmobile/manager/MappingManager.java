package org.unpidf.univmobile.manager;

import static android.content.pm.PackageManager.GET_META_DATA;

import java.net.URLEncoder;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.Bundle;

public class MappingManager {

    public static final String DIR_DATA = "/Android/data/org.unpidf.univmobile";
    public static final String API_KEY = "toto";
    private static String URL = null;
    private static String APP_VERSION = null;

    public static String getUrlApiJson(Context context) {
        if (URL != null) {
            return URL + "json/";
        } else {
            URL = getMetadata(context, "jsonURL");
            return URL + "json/";
        }
    }
    
    public static String getUrlApi(Context context) {
        if (URL != null) {
            return URL;
        } else {
            URL = getMetadata(context, "jsonURL");
            return URL;
        }
    }

    public static String getUrlRegions(Context context) {
        return getUrlApiJson(context) + "regions";
    }

    public static String getUrlPois(Context context) {
        return getUrlApiJson(context) + "pois";
    }
    
    public static String getUrlSession(Context context) {
        return getUrlApiJson(context) + "session";
    }
    
    public static String getUrlPois(Context context, Location loc) {
        return getUrlApiJson(context) + "pois" + "?lat="+loc.getLatitude()+"&lng="+loc.getLongitude();
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

    public static String getUrlShibbo(Context context, String target, String id) {
        return getUrlApi(context) + "Shibboleth.sso/Login?target=" + URLEncoder.encode(target) + "&entityID=" + URLEncoder.encode(id);
    }
    
    public static String getUrlShibboTarget(Context context, String token) {
        return getUrlApi(context) + "testSP/?loginToken=" + token + "&callback=" + URLEncoder.encode(getUrlShibboSuccess(context)) + ".sso";
    }
    
    public static String getUrlShibboSuccess(Context context) {
        return getUrlApi(context) + "testSP/success";
    }
    }
