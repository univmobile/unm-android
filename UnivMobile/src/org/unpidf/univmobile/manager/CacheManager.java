package org.unpidf.univmobile.manager;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class CacheManager {

	private static final String STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static final String TAG = "CacheManager";
	
	public static JSONObject loadCache( String directory, String filename ) {
		JSONObject jsonObject = null;
		InputStream fIn = null;
		byte[] buffer = new byte[1024];
		try {
			fIn = new FileInputStream(STORAGE_DIRECTORY + "/" + directory + "/" + filename);
			StringBuilder x = new StringBuilder();

			int numRead = 0;
			while ((numRead = fIn.read(buffer)) >= 0) {
			    x.append(new String(buffer, 0, numRead));
			}
			jsonObject = new JSONObject(x.toString());
	    	fIn.close();
		} catch (FileNotFoundException e) {
			Log.e( TAG, "FileNotFoundException : CacheManager : " + e.getMessage());
		} catch (IOException e) {
			Log.e( TAG, "IOException : CacheManager : " + e.getMessage());
		} catch (JSONException e) {
			Log.e( TAG, "JSONException : CacheManager : " + e.getMessage());
		} finally { 
			closeStream(fIn); 
		}
		return jsonObject;
	}	
	
	public static void createCache(JSONObject jsonObject, String directory, String filename ){
		FileOutputStream out = null;
		try {
			createDirectoryIfNeeded(STORAGE_DIRECTORY + "/" + directory);
			File cacheMediaFile = new File( STORAGE_DIRECTORY + "/" + directory, filename );
			out = new FileOutputStream(cacheMediaFile);
			out.write(jsonObject.toString().getBytes());
		} catch (FileNotFoundException e) {
			Log.e( TAG, "FileNotFoundException : saveJson : " + e.getMessage());
		} catch (IOException e) {
			Log.e( TAG, "IOException : saveJson : " + e.getMessage());
		} catch (OutOfMemoryError e) {
			Log.e( TAG, "OutOfMemoryError : saveJson : " + e.getMessage());
		} finally { 
			closeStream(out); 
		}
	}

	private static void createDirectoryIfNeeded(String directory){
		File root = Environment.getExternalStorageDirectory();
		if (!root.canWrite()){
			Log.e( TAG, "ERROR : SDCARD IS NOT WRITEABLE");
		}

		File path = new File( directory );
		if(!path.isDirectory()) {
			path.mkdirs();
		} 
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
