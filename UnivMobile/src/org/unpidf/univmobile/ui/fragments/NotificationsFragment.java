package org.unpidf.univmobile.ui.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.NotificationEntity;
import org.unpidf.univmobile.data.models.NotificationsDataModel;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.adapters.NotificationsAdapter;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.List;


public class NotificationsFragment extends AbsFragment {


	private NotificationsDataModel mNotificationsDataModel;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private NotificationsAdapter mAdapter;
	private boolean mShowLoading = true;

	public static NotificationsFragment newInstance() {
		NotificationsFragment fragment = new NotificationsFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public NotificationsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_notifications, container, false);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mNotificationsDataModel != null) {
			mNotificationsDataModel.clear();
			mNotificationsDataModel = null;
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.title), FontHelper.FONT.EXO_SEMI_BOLD);

		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.geo_purple_light);

		initNotifications();
	}


	private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			if (mAdapter != null) {
				mAdapter.clear();
			}
			mShowLoading = false;
			initNotifications();
		}
	};


	private void initNotifications() {
		if (mNotificationsDataModel == null) {
			mNotificationsDataModel = new NotificationsDataModel(getActivity());
		}
		mNotificationsDataModel.getNotifications(mNotificationsListener);
	}

	private NotificationsDataModel.NotificationsModelListener mNotificationsListener = new NotificationsDataModel.NotificationsModelListener() {

		@Override
		public void showLoadingIndicator() {
			if (mShowLoading) {
				getView().findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
			}
			getView().findViewById(R.id.listView).setVisibility(View.GONE);
		}

		@Override
		public void notificationsReceived(int newNotificationsCount, List<NotificationEntity> notifications) {

			((HomeActivity) getActivity()).resetNewNotificationsCount();
			mNotificationsDataModel.saveNotificationsReadDate();

			mSwipeRefreshLayout.setRefreshing(false);
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);


			if (notifications != null && notifications.size() > 0) {

				ListView list = (ListView) getView().findViewById(R.id.listView);
				list.setVisibility(View.VISIBLE);

				NotificationsAdapter adapter = new NotificationsAdapter(getActivity(), notifications);
				list.setAdapter(adapter);
			} else {
				getView().findViewById(R.id.no_data).setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onError(ErrorEntity mError) {

			mSwipeRefreshLayout.setRefreshing(false);
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
			handleError(mError);
		}
	};
}
