package org.unpidf.univmobile.data.models;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.unpidf.univmobile.data.entities.Regions;

/**
 * Created by Rokas on 2015-01-07.
 * <p/>
 * This data model is responsible for receiving and storing information related with regions and universities list.
 * Also handles selected university.
 */
public class RegionsDataModel {

	private static final String REGIONS_URL = "http://vps111534.ovh.net/unm-backend/api/regions";

	private Context mContext;
	private RegionsDataModelObserver mObserver;

	public RegionsDataModel(Context c) {
		mContext = c;
	}

	public void addObserver(RegionsDataModelObserver observer) {
		mObserver = observer;
	}

	public void getRegions() {

		if (mObserver != null) {
			mObserver.onLoadStarted();
		}
		RequestQueue queue = Volley.newRequestQueue(mContext);
		String url = "http://vps111534.ovh.net/unm-backend/api/regions";

		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Regions regions = new Gson().fromJson(response, new TypeToken<Regions>() {}.getType());
				if (mObserver != null) {
					mObserver.onRegionsReceived(regions);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				if (mObserver != null) {
					mObserver.onLoadFailed(volleyError.getMessage());
				}
			}
		});
		queue.add(stringRequest);
	}

	public interface RegionsDataModelObserver {
		public void onLoadStarted();

		public void onLoadFailed(String message);

		public void onRegionsReceived(Regions regions);
	}
}
