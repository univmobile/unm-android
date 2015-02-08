package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Region;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.data.operations.ReadUniversitiesOperation;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadRegionsOperation;
import org.unpidf.univmobile.data.repositories.SharedPreferencesRepo;

import java.util.List;

/**
 * Created by Rokas on 2015-01-07.
 * <p/>
 * This data model is responsible for receiving and storing information related with regions and universities list.
 * Also handles selected university.
 */
public class UniversitiesDataModel {

	private static final String PREF_UNIVERSITY = "pref_university";

	private Context mContext;
	private UniversitiesDataModelObserver mObserver;
	private ReadRegionsOperation mRegionsOperation;
	private ReadUniversitiesOperation mReadUniversitiesOperation;

	private List<University> mUniversities;
	private List<Region> mRegions;

	public UniversitiesDataModel(Context c) {
		mContext = c;
	}

	public void clear() {
		mObserver = null;
		mRegionsOperation = null;
		mReadUniversitiesOperation = null;
		mUniversities = null;
		mRegions = null;
	}

	public void getRegions(UniversitiesDataModelObserver observer) {
		mObserver = observer;
		mRegionsOperation = new ReadRegionsOperation(mContext, mRegionsListener);
		mRegionsOperation.startOperation();
	}

	public void getUniversities(UniversitiesDataModelObserver observer, Region r) {
		mObserver = observer;
		mReadUniversitiesOperation = new ReadUniversitiesOperation(mContext, mUniversitiesListener, r.getUniversityUrl());
		mReadUniversitiesOperation.startOperation();
	}


	private OperationListener<List<Region>> mRegionsListener = new OperationListener<List<Region>>() {
		@Override
		public void onOperationStarted() {
			if (mObserver != null) {
				mObserver.showLoadingIndicator();
			}
		}

		@Override
		public void onOperationFailed(ErrorEntity error) {
			mRegionsOperation.clear();
			mRegionsOperation = null;
			if (mObserver != null) {
				mObserver.showErrorMessage(error);
			}
		}

		@Override
		public void onOperationFinished(List<Region> result) {
			mRegions = result;
			mRegionsOperation.clear();
			mRegionsOperation = null;
			if (mObserver != null) {
				mObserver.showRegions(result);
			}
		}
	};

	private OperationListener<List<University>> mUniversitiesListener = new OperationListener<List<University>>() {
		@Override
		public void onOperationStarted() {
			if (mObserver != null) {
				mObserver.showLoadingIndicator();
			}
		}

		@Override
		public void onOperationFailed(ErrorEntity error) {
			mReadUniversitiesOperation.clear();
			mReadUniversitiesOperation = null;
			if (mObserver != null) {
				mObserver.showErrorMessage(error);
			}
		}

		@Override
		public void onOperationFinished(List<University> result) {
			mUniversities = result;
			mReadUniversitiesOperation.clear();
			mReadUniversitiesOperation = null;
			if (mObserver != null) {
				mObserver.showUniversities(result);
			}
		}
	};

	public boolean isUniversitySaved() {
		if (SharedPreferencesRepo.getInt(mContext, PREF_UNIVERSITY) != -1) {
			return true;
		} else {
			return false;
		}
	}

	public void saveUniversity(University university) {
		SharedPreferencesRepo.saveInt(mContext, PREF_UNIVERSITY, university.getId());
	}

	public int getSavedUniversity() {
		return SharedPreferencesRepo.getInt(mContext, PREF_UNIVERSITY);
	}

	public List<Region> regions() {
		return mRegions;
	}

	public List<University> universities() {
		return mUniversities;
	}

	public void setUniversities(List<University> mUniversities) {
		this.mUniversities = mUniversities;
	}

	public interface UniversitiesDataModelObserver {
		public void showLoadingIndicator();

		public void showRegions(List<Region> regionsList);

		public void showUniversities(List<University> universityList);

		public void showErrorMessage(ErrorEntity error);
	}
}
