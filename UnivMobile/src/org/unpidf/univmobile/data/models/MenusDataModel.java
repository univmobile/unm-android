package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.NavigationMenu;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadMenusOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-12.
 */
public class MenusDataModel extends AbsDataModel {

	private Context mContext;
	private MenusModelListener mListener;

	private ReadMenusOperation mReadMenusOperation;


	public MenusDataModel(Context c) {
		mContext = c;
	}

	@Override
	public void clear() {
		clearOperation(mReadMenusOperation);
		mReadMenusOperation = null;

		mContext = null;
		mListener = null;
	}

	public void getMenus(MenusModelListener listener) {
		clearOperation(mReadMenusOperation);
		mReadMenusOperation = null;

		mListener = listener;

		int univID = UniversitiesDataModel.getSavedUniversity(mContext).getId();
		mReadMenusOperation = new ReadMenusOperation(mContext, mReadMenusOperationListener, univID);
		mReadMenusOperation.startOperation();
	}

	private List<NavigationMenu> getMenuWithGrouping(List<NavigationMenu> menus, String grouping) {
		List<NavigationMenu> groupMenu = new ArrayList<NavigationMenu>();
		for (NavigationMenu m : menus) {
			if (m.getGrouping().equals(grouping)) {
				groupMenu.add(m);
			}
		}
		Collections.sort(groupMenu);
		return groupMenu;
	}

	private OperationListener<List<NavigationMenu>> mReadMenusOperationListener = new OperationListener<List<NavigationMenu>>() {
		@Override
		public void onOperationStarted() {
		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<NavigationMenu> result) {
			clearOperation(mReadMenusOperation);
			mReadMenusOperation = null;

			if (mListener != null) {
				if (result != null) {

					List<NavigationMenu> msMenu = getMenuWithGrouping(result, "MS");
					List<NavigationMenu> muMenu = getMenuWithGrouping(result, "MU");
					List<NavigationMenu> tttMenu = getMenuWithGrouping(result, "TT");
					mListener.menusReceived(msMenu, muMenu, tttMenu);
				}
			}
		}

		@Override
		public void onPageDownloaded(List<NavigationMenu> result) {
		}
	};


	public interface MenusModelListener {
		void menusReceived(List<NavigationMenu> msMenus, List<NavigationMenu> muMenus, List<NavigationMenu> ttMenus);
	}
}
