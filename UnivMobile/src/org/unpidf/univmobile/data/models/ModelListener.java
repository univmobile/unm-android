package org.unpidf.univmobile.data.models;

import org.unpidf.univmobile.data.entities.ErrorEntity;

/**
 * Created by Rokas on 2015-03-13.
 */
public interface ModelListener {
	void onError(ErrorEntity mError);
}
