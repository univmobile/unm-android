package org.unpidf.univmobile.data.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.Comment;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.ImageMap;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.entities.RestoMenu;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.PostBookmarkOperation;
import org.unpidf.univmobile.data.operations.PostCommentOperation;
import org.unpidf.univmobile.data.operations.PostPoiOperation;
import org.unpidf.univmobile.data.operations.ReadCategoriesOperation;
import org.unpidf.univmobile.data.operations.ReadCommentsOperation;
import org.unpidf.univmobile.data.operations.ReadImageMapOperation;
import org.unpidf.univmobile.data.operations.ReadPoiOperation;
import org.unpidf.univmobile.data.operations.ReadPoisOperation;
import org.unpidf.univmobile.data.operations.ReadRestMenuOperation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class GeoDataModel extends AbsDataModel {

	public static final int ROOT_CAT_1 = 1;
	public static final int ROOT_CAT_2 = 5;
	public static final int ROOT_CAT_3 = 2;
	private Context mContext;
	private GeoModelListener mListener;

	//all categories
	private ReadCategoriesOperation mReadCategoriesOperationAll;
	private List<Category> mCategoriesAll;
	private boolean mFinishedCatAll = false;

	//categories tab 1
	private ReadCategoriesOperation mReadCategoriesOperation1;
	private List<Category> mCategories1;
	private boolean mFinishedCat1 = false;

	//categories tab 2
	private ReadCategoriesOperation mReadCategoriesOperation2;
	private List<Category> mCategories2;
	private boolean mFinishedCat2 = false;

	//categories tab 3
	private ReadCategoriesOperation mReadCategoriesOperation3;
	private List<Category> mCategories3;
	private boolean mFinishedCat3 = false;


	//pois downloading
	private ReadPoisOperation mPoisOperation;
	private int mUniversityId;

	//Single poi download
	private ReadPoiOperation mReadPoiOperation;

	// image map download
	private ReadImageMapOperation mReadImageMap;
	private ReadPoisOperation mImageMapPoisOperation;
	private ImageMap mImageMap;

	//comments downlaoding
	private ReadCommentsOperation mCommentsOperation;
	private List<Comment> mComments;
	private int mCommentsPoiId;

	//Resto menu
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	private ReadRestMenuOperation mReadRestMenuOperation;

	//Post comment
	private PostCommentOperation mPostCommentOperation;

	//Post poi
	private PostPoiOperation mPostPoiOperation;
	private SimpleDateFormat mPoiDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	//Post bookmark
	private PostBookmarkOperation mPostBookmarkOperation;


	private int mPoiToBeShownId = -1;
	private int mImageMapToBeShownId = -1;
	private final Object lock = new Object();


	public GeoDataModel(Context c, GeoModelListener listener) {
		mContext = c;
		mListener = listener;
	}

	@Override
	public void clear() {
		mContext = null;
		mListener = null;

		clearOperation(mReadCategoriesOperationAll);
		clearOperation(mReadCategoriesOperation1);
		clearOperation(mReadCategoriesOperation2);
		clearOperation(mReadCategoriesOperation3);

		mReadCategoriesOperationAll = null;
		mReadCategoriesOperation1 = null;
		mReadCategoriesOperation2 = null;
		mReadCategoriesOperation3 = null;

		clearOperation(mPoisOperation);
		mPoisOperation = null;

		clearOperation(mCommentsOperation);
		mCommentsOperation = null;

		clearOperation(mReadRestMenuOperation);
		mReadRestMenuOperation = null;

		clearOperation(mReadImageMap);
		mReadImageMap = null;

		clearOperation(mImageMapPoisOperation);
		mImageMapPoisOperation = null;

		clearOperation(mReadPoiOperation);
		mReadPoiOperation = null;

		clearOperation(mPostCommentOperation);
		mPostCommentOperation = null;

		clearOperation(mPostPoiOperation);
		mPostPoiOperation = null;

		clearOperation(mPostBookmarkOperation);
		mPostBookmarkOperation = null;

	}

	public void setPoiToBeShownId(int id) {
		mPoiToBeShownId = id;
	}

	public void setImageMapToBeShownId(int id) {
		mImageMapToBeShownId = id;
	}
	public Category getCategoryById(int id) {
		for (Category c : mCategoriesAll) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}

	public void postBookmark(Poi poi) {
		clearOperation(mPostBookmarkOperation);
		mPostBookmarkOperation = null;

		Login login = ((UnivMobileApp) mContext.getApplicationContext()).getmLogin();
		if (login == null) {
			mListener.showAuthorizationError();
		} else {
			mPostBookmarkOperation = new PostBookmarkOperation(mContext, mPostBookmarkOperationListener, login.getId(), poi.getId());
			mPostBookmarkOperation.startOperation();
		}
	}

	public void postPoi(Category cat, String name, String address, String phone, String mail, String description) {
		clearOperation(mPostPoiOperation);
		mPostPoiOperation = null;
		try {
			Geocoder geocoder = new Geocoder(mContext);
			List<Address> addresses;

			addresses = geocoder.getFromLocationName(address, 1);

			String lat = null;
			String lng = null;
			if (addresses.size() > 0) {
				lat = String.valueOf(addresses.get(0).getLatitude());
				lng = String.valueOf(addresses.get(0).getLongitude());
			}

			University univ = UniversitiesDataModel.getSavedUniversity(mContext);
			String date = mPoiDateFormat.format(new Date());
			mPostPoiOperation = new PostPoiOperation(mContext, mPostPoiOperationListener, cat, univ, name, date, address, phone, mail, description, lat, lng);
			mPostPoiOperation.startOperation();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void postComment(String comment, Poi poi) {
		clearOperation(mPostCommentOperation);
		mPostCommentOperation = null;

		mPostCommentOperation = new PostCommentOperation(mContext, mPostCommentOperationListener, comment, poi);
		mPostCommentOperation.startOperation();
	}

	public void getPoi(int id) {
		clearOperation(mReadPoiOperation);
		mReadPoiOperation = null;
		mReadPoiOperation = new ReadPoiOperation(mContext, mReadPoiOperationListener, id, null);
		mReadPoiOperation.startOperation();
	}

	public void getImageMap(int id) {
		clearOperation(mReadImageMap);
		mReadImageMap = null;

		mReadImageMap = new ReadImageMapOperation(mContext, mReadImageMapOperationListener, id);
		mReadImageMap.startOperation();
	}

	public void getImageMapPois(String url) {
		mPoisOperation = new ReadPoisOperation(mContext, mImageMapPoisListener, null, -1, -1, url);
		mPoisOperation.startOperation();
	}

	public void getCategories() {
		clearOperation(mReadCategoriesOperationAll);
		mReadCategoriesOperationAll = null;

		clearOperation(mReadCategoriesOperation1);
		mReadCategoriesOperation1 = null;

		clearOperation(mReadCategoriesOperation2);
		mReadCategoriesOperation2 = null;

		clearOperation(mReadCategoriesOperation3);
		mReadCategoriesOperation3 = null;


		mReadCategoriesOperationAll = new ReadCategoriesOperation(mContext, mCategoriesAllListener, -1);
		mReadCategoriesOperation1 = new ReadCategoriesOperation(mContext, mCategories1Listener, ROOT_CAT_1);
		mReadCategoriesOperation2 = new ReadCategoriesOperation(mContext, mCategories2Listener, ROOT_CAT_2);
		mReadCategoriesOperation3 = new ReadCategoriesOperation(mContext, mCategories3Listener, ROOT_CAT_3);
		mReadCategoriesOperationAll.startOperation();
		mReadCategoriesOperation1.startOperation();
		mReadCategoriesOperation2.startOperation();
		mReadCategoriesOperation3.startOperation();
	}

	public void getPois(List<Category> selectedCategories, int root) {
		if (root != ROOT_CAT_2 && root != ROOT_CAT_3) {
			mUniversityId = UniversitiesDataModel.getSavedUniversity(mContext).getId();
		} else {
			mUniversityId = -1;
		}
		clearOperation(mPoisOperation);
		mPoisOperation = null;

		mPoisOperation = new ReadPoisOperation(mContext, mPoisListener, selectedCategories, root, mUniversityId, null);
		mPoisOperation.startOperation();

	}

	public void getComments(Poi poi, boolean refresh) {

		mUniversityId = UniversitiesDataModel.getSavedUniversity(mContext).getId();

		if (mComments != null && mCommentsPoiId == poi.getId() && !refresh) {
			if (mListener != null) {
				mListener.populateComments(mComments);
			}
		} else {
			clearOperation(mCommentsOperation);
			mCommentsOperation = null;

			if (mComments != null) {
				mComments.clear();
				mComments = null;
			}
			mCommentsPoiId = poi.getId();

			mCommentsOperation = new ReadCommentsOperation(mContext, mCommentsListener, poi.getCommentsUrl());
			//mCommentsOperation = new ReadCommentsOperation(mContext, mCommentsListener, "http://vps111534.ovh.net/unm-backend/api/comments/search/findByPoiOrderByCreatedOnDesc?poiId=15");
			mCommentsOperation.startOperation();
		}
	}

	public void getRestoMenu(Poi poi) {
		mReadRestMenuOperation = new ReadRestMenuOperation(mContext, mRestoMenuListener, poi.getId());
		mReadRestMenuOperation.startOperation();
	}

	private OperationListener<Boolean> mPostBookmarkOperationListener = new OperationListener<Boolean>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, Boolean result) {
			if (mListener != null) {
				if (error == null || error.getmErrorType() == ErrorEntity.ERROR_TYPE.JSON_ERROR) {
					mListener.bookmarkPosted();
				} else if (error != null && error.getmErrorType() == ErrorEntity.ERROR_TYPE.UNAUTHORIZED) {
					mListener.showAuthorizationError();
				}
			}
		}

		@Override
		public void onPageDownloaded(Boolean result) {

		}
	};

	private OperationListener<Boolean> mPostPoiOperationListener = new OperationListener<Boolean>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, Boolean result) {
			if (mListener != null) {
				if (error == null || error.getmErrorType() == ErrorEntity.ERROR_TYPE.JSON_ERROR) {
					mListener.poiPosted();
				} else if (error != null && error.getmErrorType() == ErrorEntity.ERROR_TYPE.UNAUTHORIZED) {
					mListener.showAuthorizationError();
				}
			}
		}

		@Override
		public void onPageDownloaded(Boolean result) {

		}
	};

	private OperationListener<Poi> mPostCommentOperationListener = new OperationListener<Poi>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, Poi result) {
			if (mListener != null) {
				if (error == null || error.getmErrorType() == ErrorEntity.ERROR_TYPE.JSON_ERROR) {
					mListener.commentPosted(result);
				} else if (error != null && error.getmErrorType() == ErrorEntity.ERROR_TYPE.UNAUTHORIZED) {
					mListener.showAuthorizationError();
				}
			}
		}

		@Override
		public void onPageDownloaded(Poi result) {

		}
	};

	private OperationListener<Poi> mReadPoiOperationListener = new OperationListener<Poi>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, Poi result) {
			if (result != null) {
				if (mListener != null) {
					mListener.showPoi(result);
				}
			}
		}

		@Override
		public void onPageDownloaded(Poi result) {

		}
	};
	private OperationListener<ImageMap> mReadImageMapOperationListener = new OperationListener<ImageMap>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, ImageMap result) {
			if (result != null && result.getPoisUrl() != null && result.getPoisUrl().length() > 0) {

				mImageMap = result;
				getImageMapPois(mImageMap.getPoisUrl());
			}

		}

		@Override
		public void onPageDownloaded(ImageMap result) {
		}
	};

	private OperationListener<List<Poi>> mImageMapPoisListener = new OperationListener<List<Poi>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Poi> result) {
			if (result != null) {
				mImageMap.setPois(result);
				if (mListener != null) {
					mListener.showImageMapPois(mImageMap);
				}
			}

		}

		@Override
		public void onPageDownloaded(List<Poi> result) {
		}
	};


	private OperationListener<List<RestoMenu>> mRestoMenuListener = new OperationListener<List<RestoMenu>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<RestoMenu> result) {
			clearOperation(mReadRestMenuOperation);
			mReadRestMenuOperation = null;

			if (mListener != null) {
				//if (result != null) {
				mListener.populateCurrentMenu(result);
				//} else {
				//mListener.showErrorMessage(error);
				//}
			}
		}

		@Override
		public void onPageDownloaded(List<RestoMenu> result) {
		}
	};
	private OperationListener<List<Comment>> mCommentsListener = new OperationListener<List<Comment>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Comment> result) {
			//if(result != null) {
			mComments = result;
			if (mListener != null) {
				mListener.populateComments(result);
			}
			//}
		}

		@Override
		public void onPageDownloaded(List<Comment> result) {
		}
	};


	private OperationListener<List<Poi>> mPoisListener = new OperationListener<List<Poi>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Poi> result) {
			if (result != null) {
				if (mListener != null) {
					mListener.populatePois(result);
				}
			}
		}

		@Override
		public void onPageDownloaded(List<Poi> result) {
			if (mListener != null) {
				mListener.populatePois(result);
			}
		}
	};

	private OperationListener<List<Category>> mCategoriesAllListener = new OperationListener<List<Category>>() {
		@Override
		public void onOperationStarted() {
		}


		@Override
		public void onOperationFinished(ErrorEntity error, List<Category> result) {
			synchronized (lock) {
				mReadCategoriesOperationAll.clear();
				mReadCategoriesOperationAll = null;
				mFinishedCatAll = true;
				mCategoriesAll = result;
				notifyListenerIfNeeded();
				if(mImageMapToBeShownId != -1) {
					getImageMap(mImageMapToBeShownId);
				}
				if(mPoiToBeShownId != -1) {
					getPoi(mPoiToBeShownId);
				}
			}
		}

		@Override
		public void onPageDownloaded(List<Category> result) {

		}
	};

	private OperationListener<List<Category>> mCategories1Listener = new OperationListener<List<Category>>() {
		@Override
		public void onOperationStarted() {
		}


		@Override
		public void onOperationFinished(ErrorEntity error, List<Category> result) {
			synchronized (lock) {
				mReadCategoriesOperation1.clear();
				mReadCategoriesOperation1 = null;
				mFinishedCat1 = true;
				mCategories1 = result;
				notifyListenerIfNeeded();
			}
		}

		@Override
		public void onPageDownloaded(List<Category> result) {

		}
	};

	private OperationListener<List<Category>> mCategories2Listener = new OperationListener<List<Category>>() {
		@Override
		public void onOperationStarted() {
		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Category> result) {
			synchronized (lock) {
				mReadCategoriesOperation2.clear();
				mReadCategoriesOperation2 = null;
				mFinishedCat2 = true;
				mCategories2 = result;
				notifyListenerIfNeeded();
			}
		}

		@Override
		public void onPageDownloaded(List<Category> result) {

		}
	};

	private OperationListener<List<Category>> mCategories3Listener = new OperationListener<List<Category>>() {
		@Override
		public void onOperationStarted() {
		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Category> result) {
			synchronized (lock) {
				mReadCategoriesOperation3.clear();
				mReadCategoriesOperation3 = null;
				mFinishedCat3 = true;
				mCategories3 = result;
				notifyListenerIfNeeded();
			}
		}

		@Override
		public void onPageDownloaded(List<Category> result) {

		}
	};

	private RestoMenu getTodayMenu(List<RestoMenu> menus) {
		long time = System.currentTimeMillis();
		Calendar today = Calendar.getInstance();
		today.setTime(new Date(time));
		RestoMenu menu = null;
		for (RestoMenu m : menus) {
			if (menu == null) {
				menu = m;
			} else {
				try {
					Date date = mDateFormat.parse(m.getEffectiveDate());
					Calendar menuCalendar = Calendar.getInstance();
					menuCalendar.setTime(date);

					if (today.get(Calendar.YEAR) == menuCalendar.get(Calendar.YEAR) &&
							today.get(Calendar.MONTH) == menuCalendar.get(Calendar.MONTH) &&
							today.get(Calendar.DAY_OF_MONTH) == menuCalendar.get(Calendar.DAY_OF_MONTH)) {
						return m;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		return menu;
	}

	private void notifyListenerIfNeeded() {
		if (mFinishedCat1 && mFinishedCat2 && mFinishedCat3 && mFinishedCatAll) {
			if (mListener != null) {
				mListener.populateCategories();
			}
		}
	}

	public List<Category> getCategories3() {
		return mCategories3;
	}

	public List<Category> getCategories1() {
		return mCategories1;
	}

	public List<Category> getCategories2() {
		return mCategories2;
	}

	public interface GeoModelListener {
		public void showErrorMessage(ErrorEntity error);

		public void populateCategories();

		public void populatePois(List<Poi> pois);

		public void populateComments(List<Comment> comments);

		public void populateCurrentMenu(List<RestoMenu> news);

		public void showImageMapPois(ImageMap map);

		public void showPoi(Poi poi);

		public void commentPosted(Poi poi);

		public void poiPosted();

		public void bookmarkPosted();

		public void showAuthorizationError();

	}
}
