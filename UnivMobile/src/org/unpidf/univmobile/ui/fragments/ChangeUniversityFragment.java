package org.unpidf.univmobile.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.PostStatisticsOperation;
import org.unpidf.univmobile.data.operations.ReadUniversitiesOperation;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.adapters.UniversitiesAdapter;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.LibraryListView;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-23.
 */
public class ChangeUniversityFragment extends AbsFragment {


	private ReadUniversitiesOperation mReadUniversitiesOperation;

	public static ChangeUniversityFragment newInstance() {
		ChangeUniversityFragment fragment = new ChangeUniversityFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public ChangeUniversityFragment() {
		// Required empty public constructor
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		mReadUniversitiesOperation.clear();
		mReadUniversitiesOperation = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_change_university, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		University univ = UniversitiesDataModel.getSavedUniversity(getActivity());
		TextView current = (TextView) view.findViewById(R.id.current_univ_value);
		current.setText(univ.getTitle());

		view.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});

//		mReadPoisOperation = new ReadPoisOperation(getActivity(), mReadPoisOperationListener, cat, -1, univID, null);
//		mReadPoisOperation.startOperation();
		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.current_univ_title), FontHelper.FONT.EXO_ITALIC);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.choose_univ), FontHelper.FONT.EXO_ITALIC);
		helper.loadFont(current, FontHelper.FONT.EXO_REGULAR);

		mReadUniversitiesOperation = new ReadUniversitiesOperation(getActivity(), mReadUniversitiesOperationListemer, -1);
		mReadUniversitiesOperation.startOperation();
	}


	private org.unpidf.univmobile.data.operations.OperationListener<List<University>> mReadUniversitiesOperationListemer = new OperationListener<List<University>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, final List<University> result) {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
			if (error != null) {
				handleError(error);
			}
			if (result != null && result.size() > 0) {

				final UniversitiesAdapter adapter = new UniversitiesAdapter(getActivity(), result);

				ListView list = (ListView) getView().findViewById(R.id.list);
				list.setVisibility(View.VISIBLE);
				list.setAdapter(adapter);
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						PostStatisticsOperation op = new PostStatisticsOperation(getActivity(), UniversitiesDataModel.getSavedUniversity(getActivity()).getSelf());
						op.startOperation();
						UniversitiesDataModel model = new UniversitiesDataModel(getActivity());
						model.saveUniversity(adapter.getItem(position));

						((HomeActivity) getActivity()).restart();
					}
				});

			}
		}

		@Override
		public void onPageDownloaded(List<University> result) {

		}
	};

}
