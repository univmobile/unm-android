package org.unpidf.univmobile.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.dialog.DialogErrorLocation;
import org.unpidf.univmobile.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
/**
 * LocationManager for Google Play Services. 
 */
public class LocManager implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	private static LocManager instance;
	private static Activity mContext;
	private static LocationClient mLocationClient;
	private static LocationRequest mLocationRequest;
	private static List<LocListener> listLocationListener;
	private static Location location;
	private static boolean needAdresseTask = false;
	private static final String TAG = "LocManager";

	private static boolean logEnabled = true;
	private boolean locationUpdateRequest;
	private boolean addressRequest;
	private String localSearch;
	private String addressString;

	public interface LocListener{
		void onLocationNotChanged(String address);
		void onLocationChanged(Location loc);
		void onAddressChanged(String address, boolean success);
	}

	@SuppressLint("HandlerLeak")
	private Handler handlerRequestLocation = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			if(location == null){
				location = new Location("network");
				location.setLatitude(0);
				location.setLongitude(0);
			}
			notifyLocationChanged(location);
			notifyAddressChanged("Géolocalisation impossible pour le moment", true);
			if(logEnabled){
				Log.w(TAG, "Géolocalisation impossible pour le moment");
			}
			locationUpdateRequest = false;
			try{
				mLocationClient.removeLocationUpdates(LocManager.instance);
				mLocationClient.disconnect();
			}catch(IllegalStateException e){
				e.printStackTrace();
			}
		}
	};

	public static LocManager getInstance(Activity context){
		if(context != null){
			mContext = context;
		}
		if(instance == null){
			instance = new LocManager();
			listLocationListener = new ArrayList<LocManager.LocListener>();
			initRequest();
			location = new Location("network");
			location.setLatitude(0);
			location.setLongitude(0);
			mLocationClient = new LocationClient(context, LocManager.instance, LocManager.instance);
		}
		return instance;
	}

	private static void initRequest() {
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		mLocationRequest.setFastestInterval(1000);
	}

	public void addLocationListener(LocListener listener){
		if(!listLocationListener.contains(listener)){
			listLocationListener.add(listener);
		}
	}

	public void removeLocationListener(LocListener listener){
		if(listLocationListener.contains(listener)){
			listLocationListener.remove(listener);
		}
	}

	public void requestUpdate(){
		if(localSearch != null){
			localSearch = null;
			addressString = null;
		}
		if(!checkLocationAvailable(mContext, true)){
			if(location == null){
				location = new Location("network");
				location.setLatitude(0);
				location.setLongitude(0);
			}
			notifyLocationChanged(location);
			notifyAddressChanged("Géolocalisation non activée", true);
			return;
		}
		if(locationUpdateRequest){
			if(logEnabled){
				Log.w(TAG, "Request Already Asked...");
			}
			return;
		}
		if(mLocationClient.isConnected()){
			if(logEnabled){
				Log.i(TAG, "Request Location Update...");
			}
			locationUpdateRequest = true;
			mLocationClient.requestLocationUpdates(mLocationRequest, LocManager.instance);
			handlerRequestLocation.sendEmptyMessageDelayed(0, 15000);
		}else{
			mLocationClient.connect();
		}
	}

	public static Location getLocation(){
		return getLocation(false);
	}
	public static Location getLocation(boolean showPopup){
		if(showPopup && location != null && location.getLongitude() == 0 && location.getLatitude() == 0){
			try{
				Toast.makeText(mContext, "La localisation est désactivé", Toast.LENGTH_SHORT).show();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return location;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		if(mLocationClient.isConnected() && mLocationClient.getLastLocation() != null && locationUpdateRequest){
			if(hasLocationChanged(mLocationClient.getLastLocation())){
				if(logEnabled){
					Log.i(TAG, "Location has Changed !");
				}
				location = mLocationClient.getLastLocation();
				notifyLocationChanged(location);
				getAddressTask();
			}else{
				if(logEnabled){
					Log.i(TAG, "Location has Not Changed !");
				}
				notifyLocationhasNotChanged();
				if(addressString == null){
					getAddressTask();
				}
			}
			if(locationUpdateRequest){
				locationUpdateRequest = false;
				handlerRequestLocation.removeMessages(0);
				mLocationClient.removeLocationUpdates(LocManager.instance);
				mLocationClient.disconnect();
			}
		}
	}

	private boolean hasLocationChanged(Location newLoc) {
		if(location != null && location.distanceTo(newLoc) < 100){
			return false;
		}
		return true;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if(logEnabled){
			Log.i(TAG, "PlayServicesLocationClient Fail !!!!!!!!!!!!!");
		}
		handlerRequestLocation.sendEmptyMessage(0);
	}

	@Override
	public void onConnected(Bundle arg0) {
		if(mLocationClient.isConnected() ){
			if(logEnabled){
				Log.i(TAG, "PlayServicesLocationClient Connected");
			}
			if(mLocationClient.getLastLocation() != null && hasLocationChanged(mLocationClient.getLastLocation())){
				location = mLocationClient.getLastLocation();
				notifyLocationChanged(location);
			}
			requestUpdate();
		}
	}

	private void getAddressTask() {
		if(!addressRequest  && needAdresseTask){
			addressRequest = true;
			new GetAddressTask().execute();
		}
	}

	public void getAddress(){
		if(!checkLocationAvailable(mContext, false) && localSearch == null){
			location = new Location("network");
			location.setLatitude(0);
			location.setLongitude(0);
			notifyLocationChanged(location);
			notifyAddressChanged("Géolocalisation non activée", true);
			return;
		}else if(mLocationClient != null && !mLocationClient.isConnected() && localSearch == null){
			notifyAddressChanged("Géolocalisation impossible pour le moment", true);
			return;
		}
		if(addressString == null && location != null){
			getAddressTask();
		}else if(addressString != null){
			notifyAddressChanged(addressString, true);
		}
	}

	@Override
	public void onDisconnected() {
		if(logEnabled){
			Log.i(TAG, "PlayServicesLocationClient Disconnected");
		}
	}

	private void notifyLocationChanged(Location loc) {
		for (LocListener listener : listLocationListener) {
			listener.onLocationChanged(loc);
		}
	}

	private void notifyLocationhasNotChanged() {
		for (LocListener listener : listLocationListener) {
			listener.onLocationNotChanged(addressString);
		}
	}

	private void notifyAddressChanged(String address, boolean success) {
		for (LocListener listener : listLocationListener) {
			listener.onAddressChanged(address, success);
		}
	}

	private class GetAddressTask extends AsyncTask<String, Object, Boolean> {
		@Override
		protected Boolean doInBackground(String... url) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
			List <Address> addresses = null;
			try {
				if(logEnabled){
					Log.i(TAG, "Get Address with Loc Method 1...");
				}
				addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
				if (addresses != null && addresses.size() > 0) {
					String address = addresses.get(0).getAddressLine(0);
					String city = addresses.get(0).getAddressLine(1);
					addressString = "Près du "+address + ", <b>"+city+"</b>";
					if(logEnabled){
						Log.i(TAG, "Get Address with Loc Method 1 OK !");
					}
					return true;
				}else{
					addressString = null;
					return false;
				}
			} catch (Exception exception1) {
				if(logEnabled){
					Log.i(TAG, "Get Address with Loc Method 2...");
				}
				exception1.printStackTrace();
				List<Address> list = getFromLocation(location.getLatitude(), location.getLongitude());
				if(list != null && list.size() != 0){
					addressString = "Près du "+list.get(0).getAddressLine(0);
					if(logEnabled){
						Log.i(TAG, "Get Address with Loc Method 2 OK !");
					}
					return true;
				}else{
					addressString = null;
					return false;
				}
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			notifyAddressChanged(addressString, result);
			addressRequest = false;
		}
	}

	private class GetAddressNameTask extends AsyncTask<String, Object, Boolean> {
		private String search;
		@Override
		protected Boolean doInBackground(String... url) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
			search = url[0];
			try {
				if(logEnabled){
					Log.i(TAG, "Get Address with Name Method 1...");
				}
				List<Address> addresses = geocoder.getFromLocationName(search, 1);
				if(addresses != null && addresses.size() >= 0){
					location.setLatitude(addresses.get(0).getLatitude());
					location.setLongitude(addresses.get(0).getLongitude());
					if(logEnabled){
						Log.i(TAG, "Get Address with Name Method 1 OK !");
					}
					return true;
				}else{
					return false;
				}
			} catch (IOException e) {
				if(logEnabled){
					Log.i(TAG, "Get Address with Name Method 2...");
				}
				Address address = getLocationInfo(search);
				if(address != null){
					location.setLatitude(address.getLatitude());
					location.setLongitude(address.getLongitude());
					if(logEnabled){
						Log.i(TAG, "Get Address with Name Method 2 OK !");
					}
					return true;
				}
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				localSearch = search;
				notifyLocationChanged(location);
				new GetAddressTask().execute();
			}else{
				Toast.makeText(mContext, "Erreur. Réessayer ultérieurement.", Toast.LENGTH_LONG).show();
				handlerRequestLocation.sendEmptyMessage(0);
			}
		}
	}

	public static List<Address> getFromLocation(double lat, double lng){
		String address = String.format(Locale.ENGLISH,"http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="+Locale.getDefault().getCountry(), lat, lng);
		HttpGet httpGet = new HttpGet(address);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		List<Address> retList = null;

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject = new JSONObject(stringBuilder.toString());
			retList = new ArrayList<Address>();

			if("OK".equalsIgnoreCase(jsonObject.getString("status"))){
				JSONArray results = jsonObject.getJSONArray("results");
				for (int i=0;i<results.length();i++ ) {
					JSONObject result = results.getJSONObject(i);
					String indiStr = result.getString("formatted_address");
					Address addr = new Address(Locale.FRANCE);
					addr.setAddressLine(0, indiStr);
					retList.add(addr);
				}
			}
		} catch (ClientProtocolException e) {
			Log.e(LocManager.class.getName(), "Error calling Google geocode webservice.", e);
		} catch (IOException e) {
			Log.e(LocManager.class.getName(), "Error calling Google geocode webservice.", e);
		} catch (JSONException e) {
			Log.e(LocManager.class.getName(), "Error parsing Google geocode webservice response.", e);
		}
		return retList;
	}

	private Address getLocationInfo(String address) {
		String query = "http://maps.google.com/maps/api/geocode/json?address=" + address.replaceAll(" ","%20")+"&region=fr&sensor=true&language="+Locale.getDefault().getCountry()
				+ "&sensor=false";
		Address addr = null;
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(query);

		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				JSONObject jsonObject = null;
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
				}
				try {
					jsonObject = new JSONObject(stringBuilder.toString());
					addr = new Address(Locale.getDefault());
					JSONArray addrComp = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
							.getJSONArray("address_components");
					String locality = ((JSONArray)((JSONObject)addrComp.get(0)).get("types")).getString(0);
					if (locality.compareTo("locality") == 0) {
						locality = ((JSONObject)addrComp.get(0)).getString("long_name");
						addr.setLocality(locality);
					}
					String adminArea = ((JSONArray)((JSONObject)addrComp.get(2)).get("types")).getString(0);
					if (adminArea.compareTo("administrative_area_level_1") == 0) {
						adminArea = ((JSONObject)addrComp.get(2)).getString("long_name");
						addr.setAdminArea(adminArea);
					}
					String country = ((JSONArray)((JSONObject)addrComp.get(3)).get("types")).getString(0);
					if (country.compareTo("country") == 0) {
						country = ((JSONObject)addrComp.get(3)).getString("long_name");
						addr.setCountryName(country);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Double lon = Double.valueOf(0);
				Double lat = Double.valueOf(0);

				try {

					lon = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
							.getJSONObject("geometry").getJSONObject("location")
							.getDouble("lng");

					lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
							.getJSONObject("geometry").getJSONObject("location")
							.getDouble("lat");

				} catch (JSONException e) {
					e.printStackTrace();
				}
				addr.setLatitude(lat);
				addr.setLongitude(lon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addr;
	}

	public static boolean checkLocationAvailable(Activity context, boolean needPopUp) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		String provider = manager.getBestProvider(criteria, true);
		if(provider != null && !provider.equalsIgnoreCase("passive")){
			return true;
		}else if(!Utils.isPrefs("Geoloc", "fin", context.getApplicationContext()) && needPopUp){
			DialogFragment dialogExit = DialogErrorLocation.newInstance();
			if(dialogExit != null){
				dialogExit.show(context.getFragmentManager(), "dialogGeoloc"); 
			}
		}
		if(logEnabled){
			Log.w(TAG, "Géolocalisation non activé !");
		}
		return false;
	}

	public void getAddressName(String name) {
		if(!addressRequest){
			addressRequest = true;
			new GetAddressNameTask().execute(name);
		}
	}

	public static void setNeedAdresseTask(boolean needAdresseTask) {
		LocManager.needAdresseTask = needAdresseTask;
	}
}
