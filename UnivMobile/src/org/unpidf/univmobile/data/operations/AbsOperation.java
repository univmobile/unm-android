package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.unpidf.univmobile.data.entities.ErrorEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Rokas on 2015-01-31.
 */
public abstract class AbsOperation<T> extends AsyncTask<Void, Void, T> {

	public static final String TAG = "UnivMobile";

	public static final String BASE_URL = "http://vps111534.ovh.net:8082/";

	protected Context mContext;
	private OperationListener mListener;

	private ErrorEntity mError;

	public AbsOperation(Context c, OperationListener listener) {
		mContext = c;
		mListener = listener;
	}

	public void startOperation() {
		executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public void clear() {
		mContext = null;
		mListener = null;
	}

	@Override
	protected void onPreExecute() {
		Log.d(TAG, "Operation " + this.getClass().getSimpleName() + " started");
		if (mListener != null) {
			mListener.onOperationStarted();
		}
		super.onPreExecute();
	}

	@Override
	protected T doInBackground(Void... params) {
		InputStream in = null;
		T result = null;
		try {
			in = doRequest();
		} catch (IOException e) {
			e.printStackTrace();
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.NETWORK_ERROR);
			return null;
		}

		if (in == null) {
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.NETWORK_ERROR);
			return null;
		}

		try {
			result = parse(in);
		} catch (IOException e) {
			e.printStackTrace();
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.JSON_ERROR);
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.JSON_ERROR);
			return null;
		}

		if (result == null && mError == null) {
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.UNKNOWN_ERROR);
			return null;
		}

		return result;
	}

	protected void onPostExecute(T result) {
		if (mError == null) {
			Log.d(TAG, "Operation " + this.getClass().getSimpleName() + " finished");
		} else {
			Log.d(TAG, "Operation " + this.getClass().getSimpleName() + " failed. Error " + mError.getmErrorType().toString());
		}

		if (mListener != null) {
			if (mError == null) {
				mListener.onOperationFinished(result);
			} else {
				mListener.onOperationFailed(mError);
			}
		}
		if (mListener == null) {
			clear();
		}
		super.onPostExecute(result);
	}

	private InputStream doRequest() throws IOException {
		String url = getOperationUrl();
		Log.d(TAG, "Operations " + this.getClass().getSimpleName() + " request url: " + url);
		HttpUriRequest request = new HttpGet(url);
		HttpClient mHttpClient = new DefaultHttpClient();
		HttpResponse response = mHttpClient.execute(request);
		return response.getEntity().getContent();
	}

	abstract T parse(InputStream in) throws IOException, JSONException;

	protected abstract String getOperationUrl();

}
