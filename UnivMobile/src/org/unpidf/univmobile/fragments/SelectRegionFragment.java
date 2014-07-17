package org.unpidf.univmobile.fragments;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.adapter.RegionUnivAdapter;
import org.unpidf.univmobile.dao.Region;
import org.unpidf.univmobile.manager.DataManager;
import org.unpidf.univmobile.view.SelectUniversityActivity;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectRegionFragment extends Fragment{
	
	public static SelectRegionFragment newInstance(){
		return new SelectRegionFragment();
	}
	
	private OnItemClickListener OnRegionClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			Region region = (Region) parent.getItemAtPosition(position);
			((SelectUniversityActivity)getActivity()).swichToUniversity(region);
		}
	};
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(DataManager.NOTIF_REGION_OK)){
				((ListView)getView().findViewById(R.id.listView)).setAdapter(new RegionUnivAdapter(getActivity(), DataManager.getInstance(getActivity()).getListRegion()));
				((ListView)getView().findViewById(R.id.listView)).setOnItemClickListener(OnRegionClick);
			}else if(intent.getAction().equals(DataManager.NOTIF_REGION_ERR)){
				Toast.makeText(getActivity(), "Erreur lors du chargement.", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		IntentFilter filter = new IntentFilter(DataManager.NOTIF_REGION_OK);
		filter.addAction(DataManager.NOTIF_REGION_ERR);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
		return inflater.inflate(R.layout.frag_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		DataManager.getInstance(getActivity()).launchRegionGetting();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(getActivity() != null){
			try{
				LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}
		}
	}
}
