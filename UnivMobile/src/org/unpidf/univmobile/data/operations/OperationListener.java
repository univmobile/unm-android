package org.unpidf.univmobile.data.operations;

import org.unpidf.univmobile.data.entities.ErrorEntity;

/**
 * Created by Rokas on 2015-02-05.
 */
public interface OperationListener<T> {
	public void onOperationStarted();

	public void onOperationFailed(ErrorEntity error);

	public void onOperationFinished(T result);
}
