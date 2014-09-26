package org.unpidf.univmobile.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.unpidf.univmobile.dao.User;
import org.unpidf.univmobile.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Singleton, managing users.
 */
public class UserManager {

	public static final String NOTI_CONNEXION_OK = "connexion-ok";
	public static final String NOTI_CONNEXION_ERR = "connexion-err";
	private static Context mContext;
	private static UserManager mInstance;
	private User user;

	public static UserManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new UserManager();
		}
		if (context != null) {
			mContext = context;
		}
		return mInstance;
	}

	public void connect(String user, String password) {
		Utils.execute(new GetUser(), user, password);
	}

	//Connexion d'un user
	private class GetUser extends AsyncTask<String, Object, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("apiKey", MappingManager.API_KEY));
	        nameValuePairs.add(new BasicNameValuePair("login", params[0]));
	        nameValuePairs.add(new BasicNameValuePair("password", params[1]));
			
			JSONObject json = ApiManager.callAPIPost(MappingManager.getUrlSession(mContext), nameValuePairs);
			System.out.println("---JSON = "+json);
			if(parseUser(json)){
				CacheManager.createCache(json, MappingManager.DIR_DATA, "User");
				return true;
			}
			return false;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTI_CONNEXION_OK));
			}else{
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTI_CONNEXION_ERR));
			}
			super.onPostExecute(result);
		}
	}

	public void checkConnection(User user) {
		Utils.execute(new CheckConnection(), user);
	}

	// Si User connecté, alors verification pour pour mettre à jour l'AppTokenId
	private class CheckConnection extends AsyncTask<User, Object, Boolean> {
		@Override
		protected Boolean doInBackground(User... user) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("apiKey", MappingManager.API_KEY));
	        nameValuePairs.add(new BasicNameValuePair("appTokenId", user[0].getId()));

	        JSONObject json = ApiManager.callAPIPost(MappingManager.getUrlSession(mContext), nameValuePairs);
			System.out.println("---JSON = "+json);
			if(parseUser(json)){
				CacheManager.createCache(json, MappingManager.DIR_DATA, "User");
			}
			return true;
		}
	}

	private boolean parseUser(JSONObject json) {
		if (json == null) {
			return false;
		}
		try {
			user = new User(json);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public User getUser() {
		if(user == null){
			JSONObject json = CacheManager.loadCache(MappingManager.DIR_DATA, "User");
			if(json != null){
				user = new User(json);
			}
		}
		return user;
	}

}
