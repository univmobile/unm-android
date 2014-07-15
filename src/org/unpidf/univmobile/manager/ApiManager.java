package org.unpidf.univmobile.manager;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;


public class ApiManager {

	public static final int TIMEOUT = 5000;
	private final static String TAG = "ApiManager";
	private static final int BUFFERSIZE = 1024;

	private static HttpParams createHttpParams(){
		HttpParams myhttpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myhttpParams, ApiManager.TIMEOUT);   
		HttpConnectionParams.setSoTimeout(myhttpParams, ApiManager.TIMEOUT);
		return myhttpParams;
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null && 
				cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	public static JSONObject callAPI(String urlApi, Context context){
		if(!isOnline(context)){
			return null;
		}
		return callAPI(urlApi);
	}
	public static JSONObject callAPI(String urlApi){

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HttpClient httpclient = new DefaultHttpClient( createHttpParams() );  
		HttpGet httpget = new HttpGet( urlApi );  
		httpget.setHeader("Accept-Encoding", "gzip"); 
		JSONObject oJsonResult = null;

		try {
			HttpResponse response = httpclient.execute(httpget);
			InputStream instream = response.getEntity().getContent();
			Header contentEncoding = response.getFirstHeader("Content-Encoding");

			if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				instream = new GZIPInputStream(instream);
			}

			int count = 0;
			byte data[] = new byte[BUFFERSIZE];
			while ((count = instream.read(data,0,BUFFERSIZE)) != -1){
				os.write(data,0,count);
			}
			oJsonResult = new JSONObject( os.toString() );
		} catch (IOException e) {
			Log.e(TAG, "Failed : Bad request IOException " + urlApi + " : " + e.getMessage() );
		} catch (JSONException e) {
			Log.e(TAG, "Failed : Bad request JSONException " + urlApi + " : " + e.getMessage() );
		} catch (OutOfMemoryError e) {
			Log.e(TAG, "Failed : Bad request OutOfMemoryError " + urlApi + " : " + e.getMessage() );
		} finally { 
			closeStream(os); 
		}
		return oJsonResult;
	}

	public static String callAPIString(String urlApi ){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HttpClient httpclient = new DefaultHttpClient( createHttpParams() );  
		HttpGet httpget = new HttpGet( urlApi );  
		httpget.setHeader("Accept-Encoding", "gzip");
		String oJsonResult = "";

		try {
			HttpResponse response = httpclient.execute(httpget);
			InputStream instream = response.getEntity().getContent();
			Header contentEncoding = response.getFirstHeader("Content-Encoding");

			if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				instream = new GZIPInputStream(instream);
			}

			int count = 0;
			byte data[] = new byte[BUFFERSIZE];
			while ((count = instream.read(data,0,BUFFERSIZE)) != -1){
				os.write(data,0,count);
			}

			oJsonResult = os.toString();
		} catch (IOException e) {
			Log.e(TAG, "Failed : Bad request IOException " + urlApi + " : " + e.getMessage() );
		} catch (OutOfMemoryError e) {
			Log.e(TAG, "Failed : Bad request OutOfMemoryError " + urlApi + " : " + e.getMessage() );
		} finally { 
			closeStream(os); 
		}
		return oJsonResult;
	}


	private static void closeStream(Closeable stream) { 
		if (stream != null) { 
			try { 
				stream.close(); 
			} catch (IOException e) { 
				Log.e(TAG, "IOException " + e.getMessage());
			} 
		} 
	}  

}








