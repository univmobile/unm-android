package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.ssl.SslHackUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by rviewniverse on 2015-01-31.
 */
public abstract class AbsOperation<T> extends AsyncTask<Void, T, T> {

	public static final String TAG = "UnivMobile";

	protected static final String BASE_URL = "https://univmobile-dev.univ-paris1.fr/admin/";
	protected static final String BASE_URL_API = BASE_URL + "api/";

	protected enum REQUEST {POST, GET, DELETE}


	protected Context mContext;
	private OperationListener mListener;

	protected ErrorEntity mError;

	private int mCurrentPage = 0;
	private int mTotalPages = 0;

	public AbsOperation(Context c, OperationListener listener) {
		mContext = c;
		mListener = listener;
		SslHackUtils.trustAllHosts();
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
		return getOnePage(0, null);
	}

	protected void onPostExecute(T result) {
		if (mError == null) {
			Log.d(TAG, "Operation " + this.getClass().getSimpleName() + " finished");
		} else {
			Log.d(TAG, "Operation " + this.getClass().getSimpleName() + " failed. Error " + mError.getmErrorType().toString());
		}

		if (mListener != null) {
			mListener.onOperationFinished(mError, result);
		}
		if (mListener == null) {
			clear();
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(T... values) {
		if (mListener != null) {
			mListener.onPageDownloaded(values[0]);
		}

		super.onProgressUpdate(values);
	}

	private T getOnePage(int page, T oldData) {

		InputStream in = null;
		T result = null;
		try {
			in = doRequest(page);
		} catch (IOException e) {
			e.printStackTrace();
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.NETWORK_ERROR);
			return null;
		}

		if (in == null) {
			return null;
		}

		try {
			result = parse(getJsonObject(in));

			if (mCurrentPage + 1 < mTotalPages) {
				publishProgress(result);
			}

			if (oldData != null) {
				result = combine(result, oldData);
			}

			if (hasNextPage() && shouldBePaged()) {
				result = getOnePage(mCurrentPage + 1, result);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.JSON_ERROR);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.JSON_ERROR);
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.JSON_ERROR);
			return result;
		}

		return result;
	}

	protected InputStream doRequest(int page) throws IOException {
		String url = getOperationUrl(page);
		url = url.replaceAll("\\s", "");
		Log.d(TAG, "Operations " + this.getClass().getSimpleName() + " request url: " + url);
		HttpUriRequest request = null;

		Login login = ((UnivMobileApp) mContext.getApplicationContext()).getLogin();
		switch (getRequestType()) {
			case GET:
				request = new HttpGet(url);
				((HttpGet) request).setHeader("Content-type", "application/json");
				if (login != null) {
					((HttpGet) request).setHeader("Authentication-Token", login.getToken());
				}
				break;
			case POST:
				request = new HttpPost(url);
				String body = getBody();
				if (body != null) {
					((HttpPost) request).setEntity(new StringEntity(body));
					((HttpPost) request).setHeader("Content-type", "application/json");
					if (login != null) {
						((HttpPost) request).setHeader("Authentication-Token", login.getToken());
					}
				}
				break;
			case DELETE:
				request = new HttpDelete(url);
				((HttpDelete) request).setHeader("Content-type", "application/json");
				if (login != null) {
					((HttpDelete) request).setHeader("Authentication-Token", login.getToken());
				}

				break;
		}
		HttpClient mHttpClient = new DefaultHttpClient();
		HttpResponse response = mHttpClient.execute(request);
		handleStatusLine(response);
		if (response != null && response.getEntity() != null) {

			return response.getEntity().getContent();
		}
		return null;
	}

	protected JSONObject getJsonObject(InputStream in) throws IOException, JSONException, IllegalArgumentException {
		try {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();

			String inputStr;
			while ((inputStr = streamReader.readLine()) != null) {
				responseStrBuilder.append(inputStr);
			}
			Log.d(TAG, "Operations " + this.getClass().getSimpleName() + " request result: " + responseStrBuilder);
			if (responseStrBuilder != null && responseStrBuilder.length() > 0) {
				JSONObject json = new JSONObject(responseStrBuilder.toString());
				parsePage(json);

				return json;
			} else {
				return new JSONObject("{}");
			}

		} finally {
			in.close();
		}
	}

	private void parsePage(JSONObject json) {
		try {
			JSONObject page = json.getJSONObject("page");
			mTotalPages = page.getInt("totalPages");
			mCurrentPage = page.getInt("number");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public boolean hasNextPage() {
		if (mCurrentPage + 1 < mTotalPages) {
			return true;
		} else {
			return false;
		}
	}

	public int getNextPage() {
		return mCurrentPage + 1;
	}

	protected void handleStatusLine(HttpResponse response) {

	}

	protected T combine(T newData, T oldData) {
		return oldData;
	}

	protected boolean shouldBePaged() {
		return false;
	}

	protected REQUEST getRequestType() {
		return REQUEST.GET;
	}

	protected String getBody() {
		return null;
	}

	protected abstract T parse(JSONObject json) throws JSONException;

	protected abstract String getOperationUrl(int page);


}
