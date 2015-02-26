package org.unpidf.univmobile.data.models;

import org.unpidf.univmobile.data.operations.AbsOperation;

/**
 * Created by rviewniverse on 2015-02-13.
 */
public abstract class AbsDataModel {

	protected void clearOperation(AbsOperation operation) {
		if (operation != null) {
			operation.clear();
			operation.cancel(true);
		}
	}

	public abstract void clear();
}
