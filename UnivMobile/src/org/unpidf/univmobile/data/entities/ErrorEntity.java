package org.unpidf.univmobile.data.entities;

/**
 * Created by rviewniverse on 2015-01-31.
 */
public class ErrorEntity {

	public static enum ERROR_TYPE {
		NETWORK_ERROR, JSON_ERROR, UNAUTHORIZED, UNKNOWN_ERROR
	}

	private ERROR_TYPE mErrorType;

	public ErrorEntity(ERROR_TYPE type) {
		mErrorType = type;
	}

	public ERROR_TYPE getmErrorType() {
		return mErrorType;
	}
}
