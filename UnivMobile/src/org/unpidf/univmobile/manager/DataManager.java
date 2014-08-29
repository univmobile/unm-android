package org.unpidf.univmobile.manager;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.dao.Comment;
import org.unpidf.univmobile.dao.Poi;
import org.unpidf.univmobile.dao.PoiGroup;
import org.unpidf.univmobile.dao.Region;
import org.unpidf.univmobile.dao.University;
import org.unpidf.univmobile.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
/**
 * Singleton, launching request and hosting data.
 */
public class DataManager {

	public static final String NOTIF_REGION_OK = "notif-region-ok";
	public static final String NOTIF_REGION_ERR = "notif-region-err";
	public static final String NOTIF_REGION_UNIV_OK = "notif-region-univ-ok";
	public static final String NOTIF_REGION_UNIV_ERR = "notif-region-univ-err";
	public static final String NOTIF_POIS_OK = "notif-pois-ok";
	public static final String NOTIF_POIS_ERR = "notif-pois-err";
	public static final String NOTIF_COMMENT_OK = "notif-comment-ok";
	public static final String NOTIF_COMMENT_ERR = "notif-comment-err";

	private static Context mContext;
	private static DataManager mInstance;

	private University currentUniversity;
	private List<Region> listRegion;
	private List<PoiGroup> listPois;
	private List<Comment> listComments;

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
		if(listRegion != null && listRegion.size() > 0){
			//From Memory
			LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_REGION_OK));
		}else{
			JSONObject jsonCache = CacheManager.loadCache( MappingManager.DIR_DATA, "ListRegions");
			if( jsonCache != null ){
				//From cache
				boolean etat = parseRegions(jsonCache);
				if(etat){
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_REGION_OK));
				}
			}else{
				//From app
				int ressourceId =  mContext.getResources().getIdentifier("listregions", "raw", mContext.getPackageName());
				if(ressourceId != 0){
					InputStream is = mContext.getResources().openRawResource(ressourceId);
					try {
						byte [] buffer = new byte[is.available()];
						int bytesRead;
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						while ((bytesRead = is.read(buffer)) != -1) {
							output.write(buffer, 0, bytesRead);
						}
						byte[] bytes = output.toByteArray();
						JSONObject jsonRaw = new JSONObject(new String(bytes));
						boolean etat = parseRegions(jsonRaw);
						if(etat){
							LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_REGION_OK));
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						closeStream(is);
					}
				}
			}
		}
		Utils.execute(new GetListRegionTask());
	}

	private class GetListRegionTask extends AsyncTask<Object, Object, Boolean>{
		@Override
		protected Boolean doInBackground(Object... params) {
			JSONObject jsonObject = ApiManager.callAPI(MappingManager.getUrlRegions(mContext));
			boolean etat = parseRegions(jsonObject);
			if(etat){
				CacheManager.createCache(jsonObject, MappingManager.DIR_DATA, "ListRegions");
			}
			return etat;
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

	private boolean parseRegions(JSONObject json) {
		if(json == null){
			return false;
		}
		try{
			JSONArray array = json.getJSONArray("regions");
			List<Region> listRegiontemp = new ArrayList<Region>();
			for (int i = 0; i < array.length(); i++) {
				listRegiontemp.add(new Region(array.getJSONObject(i)));
			}
			listRegion = listRegiontemp;
			return true;
		}catch(JSONException e){
			e.printStackTrace();
			return false;
		}
	}

	public void launchRegionUniversityGetting(Region region) {
		if(region.getListUniversities().size() != 0){
			//From Memory
			LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_REGION_UNIV_OK + region.getId()));
		}else{
			JSONObject jsonCache = CacheManager.loadCache( MappingManager.DIR_DATA, "ListUniversities-"+region.getId() );
			if( jsonCache != null ){
				//From cache
				boolean etat = parseListRegions(region, jsonCache);
				if(etat){
					LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_REGION_UNIV_OK + region.getId()));
				}
			}else{
				//From app
				int ressourceId =  mContext.getResources().getIdentifier("listuniversities_"+region.getId(), "raw", mContext.getPackageName());
				if(ressourceId != 0){
					InputStream is = mContext.getResources().openRawResource(ressourceId);
					try {
						byte [] buffer = new byte[is.available()];
						int bytesRead;
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						while ((bytesRead = is.read(buffer)) != -1) {
							output.write(buffer, 0, bytesRead);
						}
						byte[] bytes = output.toByteArray();
						JSONObject jsonraw = new JSONObject(new String(bytes));
						boolean etat = parseListRegions(region, jsonraw);
						if(etat){
							LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_REGION_UNIV_OK + region.getId()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						closeStream(is);
					}
				}
			}
		}
		Utils.execute(new GetListRegionUnivTask(), region);
	}

	private static void closeStream(Closeable stream) { 
		if (stream != null) { 
			try { 
				stream.close(); 
			} catch (IOException e) { 
				Log.e("DATAMANAGER", "Message : IOException " + e.getMessage());
			} 
		} 
	}

	private class GetListRegionUnivTask extends AsyncTask<Region, Object, Boolean>{
		private Region region;
		@Override
		protected Boolean doInBackground(Region... params) {
			region = params[0];
			JSONObject jsonObject = ApiManager.callAPI(region.getUrl());
			boolean etat = parseListRegions(region, jsonObject);
			if(etat){
				CacheManager.createCache(jsonObject, MappingManager.DIR_DATA, "ListUniversities-"+region.getId());
			}
			return etat;
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

	private boolean parseListRegions(Region region, JSONObject json) {
		if(json == null){
			return false;
		}
		try{
			JSONArray array = json.getJSONArray("universities");
			List<University> ListUniversitiestemp = new ArrayList<University>();
			for (int i = 0; i < array.length(); i++) {
				ListUniversitiestemp.add(new University(array.getJSONObject(i)));
			}
			region.setListUniversities(ListUniversitiestemp);
			return true;
		}catch(JSONException e){
			e.printStackTrace();
			return false;
		}
	}

	public void setCurrentUniversity(University university) {
		currentUniversity = university;
	}

	public void launchPoisGetting() {
		JSONObject jsonCache = CacheManager.loadCache( MappingManager.DIR_DATA, "ListPois");
		if( jsonCache != null ){
			//From cache
			boolean etat = parseListPois(jsonCache);
			if(etat){
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_POIS_OK));
			}
		}
		Utils.execute(new GetPoiTask());
	}

	private class GetPoiTask extends AsyncTask<Object, Object, Boolean>{
		@Override
		protected Boolean doInBackground(Object... params) {
			JSONObject jsonObject = ApiManager.callAPI(MappingManager.getUrlPois(mContext));
			boolean etat = parseListPois(jsonObject);
			if(etat){
				CacheManager.createCache(jsonObject, MappingManager.DIR_DATA, "ListPois");
			}
			return etat;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_POIS_OK));
			}else{
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_POIS_ERR));
			}
		}
	}

	private boolean parseListPois(JSONObject json) {
		if(json == null){
			return false;
		}
		try{
			JSONArray array = json.getJSONArray("groups");
			List<PoiGroup> listPoiGroupTemp = new ArrayList<PoiGroup>();
			for (int i = 0; i < array.length(); i++) {
				listPoiGroupTemp.add(new PoiGroup(array.getJSONObject(i)));
			}
			listPois = listPoiGroupTemp;
			return true;
		}catch(JSONException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public List<PoiGroup> getListPois() {
		if(listPois != null){
			return listPois;
		}
		return new ArrayList<PoiGroup>();
	}

	public void launchPoisCommentGetting(Poi poi) {
		Utils.execute(new GetCommentTask(), poi);
	}

	private class GetCommentTask extends AsyncTask<Poi, Object, Boolean>{
		private Poi poi;
		@Override
		protected Boolean doInBackground(Poi... params) {
			poi = params[0];
			// System.out.println("---URL = "+poi.getCommentsUrl());
			JSONObject jsonObject = ApiManager.callAPI(poi.getCommentsUrl());
			// System.out.println("---JSON = "+jsonObject);
			boolean etat = parseListComments(jsonObject);
			return etat;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_COMMENT_OK + "-" + poi.getId()));
			}else{
				LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(NOTIF_COMMENT_ERR + "-" + poi.getId()));
			}
		}
	}

	private boolean parseListComments(JSONObject json) {
		if(json == null){
			return false;
		}
		try{
			JSONArray array = json.getJSONArray("comments");
			List<Comment> listCommentTemp = new ArrayList<Comment>();
			for (int i = 0; i < array.length(); i++) {
				listCommentTemp.add(new Comment(array.getJSONObject(i)));
			}
			listComments = listCommentTemp;
			return true;
		}catch(JSONException e){
			e.printStackTrace();
			return false;
		}
	}

	public List<Comment> getListComments() {
		if(listComments != null){
			return listComments;
		}
		return new ArrayList<Comment>();
	}
}
