package org.unpidf.univmobile.data.operations;

import org.unpidf.univmobile.data.entities.ErrorEntity;

/**
 * Created by rviewniverse on 2015-02-05.
 */
public interface OperationListener<T> {
	public void onOperationStarted();

	public void onOperationFinished(ErrorEntity error, T result);

	public void onPageDownloaded(T result);
}
