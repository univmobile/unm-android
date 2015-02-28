package org.unpidf.univmobile.data.operations;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Poi;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Rokas on 2015-02-28.
 */
public class RemoveBookamrkOperation extends AbsOperation<Poi> {

	private static final String BOOKMARK = "bookmarks/%d";
	private Poi mPoi;
	private int mBokmarkID;

	public RemoveBookamrkOperation(Context c, OperationListener listener, Poi poi, int bookmarkID) {
		super(c, listener);
		mPoi = poi;
		mBokmarkID = bookmarkID;
	}

	@Override
	protected Poi parse(JSONObject json) throws JSONException {
		return mPoi;
	}

	@Override
	protected JSONObject getJsonObject(InputStream in) throws IOException, JSONException, IllegalArgumentException {
		return null;
	}

	@Override
	protected String getOperationUrl(int page) {
		return BASE_URL_API + String.format(BOOKMARK, mBokmarkID);
	}

	protected REQUEST getRequestType() {
		return REQUEST.DELETE;
	}


}
