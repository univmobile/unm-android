package org.unpidf.univmobile.fragments;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.adapter.ListPoiAdapter;
import org.unpidf.univmobile.custom.AnimatedExpandableListView;
import org.unpidf.univmobile.manager.DataManager;
import org.unpidf.univmobile.view.GeocampusActivity;
import org.unpidf.univmobile.view.UniversityActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

/**
 * List regions and universities. Fragment for: {@link GeocampusActivity}
 * 
 * @author Michel
 */
public class ListPoiFragment extends Fragment {

    private ListPoiAdapter adapter;

    public static ListPoiFragment newInstance(String title) {
        final Bundle bundle = new Bundle();
        bundle.putString("title", title);
        final ListPoiFragment fragment = new ListPoiFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getView() == null || getActivity() == null) {
                return;
            }
            if (intent.getAction().equals(DataManager.NOTIF_POIS_OK)) {
                getView().findViewById(R.id.loader).setVisibility(View.GONE);
                refreshData();
            } else if (intent.getAction().equals(DataManager.NOTIF_POIS_ERR)) {
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = null;
        final IntentFilter filter = new IntentFilter();
        filter.addAction(DataManager.NOTIF_POIS_OK);
        filter.addAction(DataManager.NOTIF_POIS_ERR);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
        return inflater.inflate(R.layout.frag_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected void refreshData() {
        final AnimatedExpandableListView listView = (AnimatedExpandableListView) getView()
                .findViewById(R.id.listView);
        if (adapter != null) {
            adapter.setList(DataManager.getInstance(getActivity()).getListPois());
        } else {
            listView.setChildIndicator(null);
            listView.setGroupIndicator(null);
            adapter = new ListPoiAdapter(getActivity(), DataManager.getInstance(getActivity())
                    .getListPois());
            listView.setAdapter(adapter);
            listView.setOnGroupClickListener(new OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                        long id) {
                    if (listView.isGroupExpanded(groupPosition)) {
                        listView.collapseGroupWithAnimation(groupPosition);
                    } else {
                        listView.expandGroupWithAnimation(groupPosition);
                    }
                    return true;
                }
            });
            listView.setOnChildClickListener(new OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                        int childPosition, long id) {
                    startActivity(new Intent(getActivity(), UniversityActivity.class).putExtra(
                            "poi", adapter.getChild(groupPosition, childPosition)));
                    return false;
                }
            });
        }
        for (int i = 0; i < DataManager.getInstance(getActivity()).getListPois().size(); i++) {
            listView.expandGroup(i);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            try {
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
