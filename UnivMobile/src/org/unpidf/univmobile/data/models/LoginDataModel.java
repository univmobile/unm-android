package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.data.operations.ShibbolethPrepareOperation;
import org.unpidf.univmobile.data.operations.ShibbolethRetrieveOperation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Rokas on 2015-01-19.
 */
public class LoginDataModel {

	public static final String API_KEY = "toto";

	public static final String TEST_URL_SHIBBOLETH = "https://univmobile-dev.univ-paris1.fr/";
	public static final String TEST_URL_SESSION = TEST_URL_SHIBBOLETH + "json/session";
	public static final String TEST_URL_SUCCESS = TEST_URL_SHIBBOLETH + "testSP/success";

	private Context mContext;

	public LoginDataModel(Context context) {
		mContext = context;
	}

	public void prepareShibboleth(ShibbolethLoginObserver observer) {
		ShibbolethPrepareOperation op = new ShibbolethPrepareOperation(mContext, observer);
		op.prepareShibboleth();
	}

	public void retrieveShibboleth(String loginToken, String key, ShibbolethLoginObserver observer) {
		ShibbolethRetrieveOperation op = new ShibbolethRetrieveOperation(mContext, observer);
		op.retrieveShibboleth(loginToken, key);

	}

	public String getShibbolethLoginUrl(String token) {
		try {

			String target = TEST_URL_SHIBBOLETH + "testSP/?loginToken=" + token + "&callback=" + URLEncoder.encode(TEST_URL_SUCCESS, "UTF-8") + ".sso";
			String url = TEST_URL_SHIBBOLETH + "Shibboleth.sso/Login?target=" + URLEncoder.encode(target, "UTF-8");// + "&entityID=" + URLEncoder.encode(id, "UTF-8");
			return url;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public interface ShibbolethLoginObserver {
		void onShibbolethConnectedSuccessful(String json);

		void onShibbolethConnectedFailed();

		void onShibbolethPrepareSuccessful(String token, String key);

		void onShibbolethPrepareFailed();
	}

	public interface StandardLoginObserver {
		void onLoginSuccessful();

		void onLoginFailed();
	}

}
