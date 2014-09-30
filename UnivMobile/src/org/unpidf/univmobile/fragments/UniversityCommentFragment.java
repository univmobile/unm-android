package org.unpidf.univmobile.fragments;

import java.util.List;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.adapter.CommentPoiAdapter;
import org.unpidf.univmobile.dao.Comment;
import org.unpidf.univmobile.dao.Poi;
import org.unpidf.univmobile.manager.DataManager;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * See all comments of a specific university. Fragment for: {@link UniversityActivity}
 * @author Michel
 *
 */
public class UniversityCommentFragment extends Fragment{

	private Poi poi;
	private CommentPoiAdapter adapter;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(getView() == null || getActivity() == null){
				return;
			}
			if(intent.getAction().contains(DataManager.NOTIF_COMMENT_OK)){
				getView().findViewById(R.id.loader).setVisibility(View.GONE);
				refreshData();
			}else{
				Toast.makeText(getActivity(), "Erreur lors de la récupération...", Toast.LENGTH_SHORT).show();
			}
		}
	};

	public static UniversityCommentFragment newInstance(Poi poi, String title) {
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putSerializable("poi", poi);
		UniversityCommentFragment frag = new UniversityCommentFragment();
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		poi = (Poi) getArguments().getSerializable("poi");
		IntentFilter filter = new IntentFilter();
		filter.addAction(DataManager.NOTIF_COMMENT_OK + "-" + poi.getId());
		filter.addAction(DataManager.NOTIF_COMMENT_ERR + "-" + poi.getId());
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
		return inflater.inflate(R.layout.frag_list_generic, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		((TextView)getView().findViewById(R.id.noData)).setText("Aucun commentaire");
		DataManager.getInstance(getActivity()).launchPoisCommentGetting(poi);
	}

	protected void refreshData() {
		List<Comment> list = DataManager.getInstance(getActivity()).getListComments();
		final ListView listView = (ListView) getView().findViewById(R.id.listView);
		if(adapter != null){
			((CommentPoiAdapter)listView.getAdapter()).setList(list);
		}else{
			adapter = new CommentPoiAdapter(getActivity(), list);
			listView.setAdapter(adapter);
		}
		getView().findViewById(R.id.noData).setVisibility((list.size() == 0) ? View.VISIBLE : View.GONE);
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
