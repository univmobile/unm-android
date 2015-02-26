package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Region;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.data.operations.AbsOperation;
import org.unpidf.univmobile.data.operations.ReadCategoriesOperation;
import org.unpidf.univmobile.data.operations.ReadUniversitiesOperation;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadRegionsOperation;
import org.unpidf.univmobile.data.repositories.SharedPreferencesRepo;

import java.util.List;

/**
 * Created by rviewniverse on 2015-01-07.
 * <p/>
 * This data model is responsible for receiving and storing information related with regions and universities list.
 * Also handles selected university.
 */
public class UniversitiesDataModel  extends AbsDataModel{

	public static final String FRANCE_REGION = "Île de France";
	private static final String PREF_UNIVERSITY_NAME = "pref_university_name";
	private static final String PREF_UNIVERSITY_ID = "pref_university_id";
	private static final String PREF_UNIVERSITY_SELF = "pref_university_self";
	private static final String PREF_REGION_NAME = "pref_region_name";

	private Context mContext;
	private UniversitiesDataModelListener mListener;
	private ReadRegionsOperation mReadRegionsOperation;
	private ReadUniversitiesOperation mReadUniversitiesOperation;



	private List<University> mUniversities;
	private List<Region> mRegions;

	public UniversitiesDataModel(Context c) {
		mContext = c;
	}

	@Override
	public void clear() {
		clearOperation(mReadRegionsOperation);
		mReadRegionsOperation = null;

		clearOperation(mReadUniversitiesOperation);
		mReadUniversitiesOperation = null;


		mContext = null;
		mListener = null;
		mUniversities = null;
		mRegions = null;
	}



	public void getRegions(UniversitiesDataModelListener listener) {
		clearOperation(mReadRegionsOperation);
		mReadRegionsOperation = null;

		mListener = listener;
		mReadRegionsOperation = new ReadRegionsOperation(mContext, mRegionsListener);
		mReadRegionsOperation.startOperation();
	}

	public void getUniversities(UniversitiesDataModelListener listener, Region r) {
		clearOperation(mReadRegionsOperation);
		mReadRegionsOperation = null;

		mListener = listener;
		mReadUniversitiesOperation = new ReadUniversitiesOperation(mContext, mUniversitiesListener, r.getUniversityUrl());
		mReadUniversitiesOperation.startOperation();
	}


	private OperationListener<List<Region>> mRegionsListener = new OperationListener<List<Region>>() {
		@Override
		public void onOperationStarted() {
			if (mListener != null) {
				mListener.showLoadingIndicator();
			}
		}


		@Override
		public void onOperationFinished(ErrorEntity error, List<Region> result) {
			mRegions = result;

			clearOperation(mReadRegionsOperation);
			mReadRegionsOperation = null;

			if (mListener != null) {
				if(error == null) {
					mListener.showRegions(result);
				} else {
					mListener.showErrorMessage(error);
				}
			}
		}

		@Override
		public void onPageDownloaded(List<Region> result) {

		}
	};

	private OperationListener<List<University>> mUniversitiesListener = new OperationListener<List<University>>() {
		@Override
		public void onOperationStarted() {
			if (mListener != null) {
				mListener.showLoadingIndicator();
			}
		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<University> result) {
			mUniversities = result;

			clearOperation(mReadUniversitiesOperation);
			mReadUniversitiesOperation = null;

			if (mListener != null) {
				if(error == null) {
					mListener.showUniversities(result);
				} else {
					mListener.showErrorMessage(error);
				}
			}
		}

		@Override
		public void onPageDownloaded(List<University> result) {

		}
	};

	public static boolean isUniversitySaved(Context c) {
		if (SharedPreferencesRepo.getInt(c, PREF_UNIVERSITY_ID) != -1) {
			return true;
		} else {
			return false;
		}
	}


	public static University getSavedUniversity(Context c) {
		String name = SharedPreferencesRepo.getString(c, PREF_UNIVERSITY_NAME);
		int id = SharedPreferencesRepo.getInt(c, PREF_UNIVERSITY_ID);
		String self = SharedPreferencesRepo.getString(c, PREF_UNIVERSITY_SELF);
		String regionName = SharedPreferencesRepo.getString(c, PREF_REGION_NAME);
		University univ = new University(id, name, self, regionName);
		return univ;
	}



	public void saveUniversity(University university) {
		SharedPreferencesRepo.saveInt(mContext, PREF_UNIVERSITY_ID, university.getId());
		SharedPreferencesRepo.saveString(mContext, PREF_UNIVERSITY_NAME, university.getTitle());
		SharedPreferencesRepo.saveString(mContext, PREF_UNIVERSITY_SELF, university.getSelf());
		SharedPreferencesRepo.saveString(mContext, PREF_REGION_NAME, university.getRegionName());
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

	public interface UniversitiesDataModelListener {
		public void showLoadingIndicator();

		public void showRegions(List<Region> regionsList);

		public void showUniversities(List<University> universityList);

		public void showErrorMessage(ErrorEntity error);
	}
}
