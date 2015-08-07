package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.ImageMap;
import org.unpidf.univmobile.data.entities.Poi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class ReadImageMapOperation extends AbsOperation<ImageMap> {

	private static final String IMAGE_MAP_DETAILS = "imageMaps/%d";
	private static final String IMAGE_MAP_CONTENT = BASE_URL_ASSETS + "imagemaps/";


	private int mID;

	public ReadImageMapOperation(Context c, OperationListener listener, int id) {
		super(c, listener);
		mID = id;
	}

	@Override
	protected ImageMap parse(JSONObject json) throws JSONException {

		ImageMap map = new Gson().fromJson(json.toString(), ImageMap.class);
		map.setImage(getBitmapFromURL(IMAGE_MAP_CONTENT + map.getUrl()));
		JSONObject links = json.getJSONObject("_links");
		JSONObject comments = links.getJSONObject("pois");
		map.setPoisUrl(comments.getString("href"));
		return map;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url = BASE_URL_API + String.format(IMAGE_MAP_DETAILS, mID);

		return url;
	}


	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			// Log exception
			return null;
		}
	}
}
