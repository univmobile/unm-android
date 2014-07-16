package org.unpidf.univmobile.manager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.dao.Region;
import org.unpidf.univmobile.dao.University;
import org.unpidf.univmobile.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

public class DataManager {

	public static final String NOTIF_REGION_OK = "notif-region-ok";
	public static final String NOTIF_REGION_ERR = "notif-region-err";
	public static final String NOTIF_REGION_UNIV_OK = "notif-region-univ-ok";
	public static final String NOTIF_REGION_UNIV_ERR = "notif-region-univ-err";
	
	private static Context mContext;
	private static DataManager mInstance;

	private University currentUniversity;
	private List<Region> listRegion;

	public static DataManager getInstance(Context context){
		if(mInstance == null){
			mInstance = new DataManager();
		}
		if(context != null){
			mContext = context;
		}
		return mInstance;
	}

	public University getCurrentUniversity() {
		return currentUniversity;
	}
	
	public List<Region> getListRegion() {
		return listRegion;
	}

	public void launchRegionGetting() {
		Utils.execute(new GetListRegionTask());
	}

	private class GetListRegionTask extends AsyncTask<Void, Object, Boolean>{
		@Override
		protected Boolean doInBackground(Void... params) {
			JSONObject jsonObject = ApiManager.callAPI(MappingManager.URL_REGIONS);
			if(jsonObject == null){
				return false;
			}
			try{
				JSONArray array = jsonObject.getJSONArray("region");
				List<Region> listRegiontemp = new ArrayList<Region>();
				for (int i = 0; i < array.length(); i++) {
					listRegiontemp.add(new Region(array.getJSONObject(i)));
				}
				listRegion = listRegiontemp;
				return true;
			}catch(JSONException e){
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_REGION_OK));
			}else{
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_REGION_ERR));
			}
		}
	}
	
	public void launchRegionUniversityGetting(Region region) {
		Utils.execute(new GetListRegionUnivTask(), region);
	}

	private class GetListRegionUnivTask extends AsyncTask<Region, Object, Boolean>{
		private Region region;
		@Override
		protected Boolean doInBackground(Region... params) {
			region = params[0];
			JSONObject jsonObject = ApiManager.callAPI(region.getUrl());
			if(jsonObject == null){
				return false;
			}
			try{
				JSONArray array = jsonObject.getJSONArray("universities");
				List<University> listUniversitytemp = new ArrayList<University>();
				for (int i = 0; i < array.length(); i++) {
					listUniversitytemp.add(new University(array.getJSONObject(i)));
				}
				region.setListUniversity(listUniversitytemp);
				return true;
			}catch(JSONException e){
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_REGION_UNIV_OK + region.getId()));
			}else{
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_REGION_UNIV_ERR + region.getId()));
			}
		}
	}

	public void setCurrentUniversity(University university) {
		currentUniversity = university;
	}
}
