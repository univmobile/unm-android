package org.unpidf.univmobile.fragments;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.adapter.RegionUnivAdapter;
import org.unpidf.univmobile.dao.Region;
import org.unpidf.univmobile.dao.University;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
/**
 * Display universities for a specific region. Fragment for: {@link SelectUniversityActivity}
 * @author Michel
 *
 */
public class SelectUniversityFragment extends Fragment{

	private Region region;
	private RegionUnivAdapter adapter;

	private OnItemClickListener OnUniversityClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			DataManager.getInstance(getActivity()).setCurrentUniversity(((University)parent.getItemAtPosition(position)));
			getActivity().finish();
		}
	};

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(getView() == null || getActivity() == null){
				return;
			}
			if(intent.getAction().contains(DataManager.NOTIF_REGION_UNIV_OK)){
				getView().findViewById(R.id.loader).setVisibility(View.GONE);
				refreshData();
			}else if(intent.getAction().contains(DataManager.NOTIF_REGION_UNIV_ERR)){
				Toast.makeText(getActivity(), "Erreur réseau...", Toast.LENGTH_SHORT).show();
				if(adapter == null){
					((SelectUniversityActivity)getActivity()).onBackPressed();
				}
			}
		}
	};

	public static Fragment newInstance(Region region) {
		SelectUniversityFragment frag = new SelectUniversityFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("region", region);
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		region = (Region) getArguments().getSerializable("region");
		adapter = null;
		IntentFilter filter = new IntentFilter(DataManager.NOTIF_REGION_UNIV_OK + region.getId());
		filter.addAction(DataManager.NOTIF_REGION_UNIV_ERR + region.getId());
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
		return inflater.inflate(R.layout.frag_list_generic, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getView().findViewById(R.id.loader).setVisibility(View.VISIBLE);
		DataManager.getInstance(getActivity()).launchRegionUniversityGetting(region);
	}

	private void refreshData() {
		if(adapter == null){
			ListView listView = ((ListView)getView().findViewById(R.id.listView));
			adapter = new RegionUnivAdapter(getActivity(), region.getListUniversities());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(OnUniversityClick);
		}else{
			adapter.setList(region.getListUniversities());
		}
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
