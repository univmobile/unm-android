package org.unpidf.univmobile.manager;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Network data Getter
 */
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

	public static boolean callAPIPost(String url, List<NameValuePair> param, boolean onlyCode) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost();
		try {
			post.setURI(new URI(url));
			HttpResponse response = null;
			post.setEntity(new UrlEncodedFormEntity(param));
			response = httpclient.execute(post);
			if( response == null ){
				return false;
			}
			if(response.getStatusLine().getStatusCode() == 200){
				return true;
			}
		} catch (URISyntaxException e1) {
			Log.e(TAG, "Exception " + e1.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "Failed : Bad request IOException " + e.getMessage() );
		} catch (OutOfMemoryError e) {
			Log.e(TAG,  "Failed : Bad request OutOfMemoryError "+ e.getMessage() );
		}
		return false;
	}
	
	public static JSONObject callAPIPost(String url, List<NameValuePair> param) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost();
		ByteArrayOutputStream os = null;
		JSONObject json = null;
		try {
			post.setURI(new URI(url));
			HttpResponse response = null;
			post.setEntity(new UrlEncodedFormEntity(param));
			response = httpclient.execute(post);
			if( response == null ){
				return null;
			}
			os = new ByteArrayOutputStream();
			response.getEntity().writeTo(os);
			json = new JSONObject( os.toString() );
		} catch (URISyntaxException e1) {
			Log.e(TAG, "Exception " + e1.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "Failed : Bad request IOException " + e.getMessage() );
		} catch (JSONException e) {
			Log.e(TAG, "Failed : Bad request JSONException "+ e.getMessage() );
		} catch (OutOfMemoryError e) {
			Log.e(TAG,  "Failed : Bad request OutOfMemoryError "+ e.getMessage() );
		} finally { 
			closeStream(os); 
		}
		return json;
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
