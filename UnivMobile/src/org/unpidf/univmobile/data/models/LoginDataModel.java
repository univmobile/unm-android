package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.entities.ShibbolethPrepare;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ShibbolethPrepareOperation;
import org.unpidf.univmobile.data.operations.ShibbolethRetrieveOperation;
import org.unpidf.univmobile.data.operations.StandardLoginOperation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by rviewniverse on 2015-01-19.
 */
public class LoginDataModel extends AbsDataModel {

	public static final String API_KEY = "toto";

	public static final String TEST_URL_SHIBBOLETH = "https://univmobile-dev.univ-paris1.fr/";
	public static String TEST_URL_SHIBBOLETH_SUCCESS = TEST_URL_SHIBBOLETH  + "testSP/success";
	private Context mContext;

	private StandardLoginDataModelListener mLoginDataModelListener;
	private StandardLoginOperation mStandardLoginOperation;

	private ShibbolethLoginDadaModelListener mShibbolethLoginDadaModelListener;
	private ShibbolethPrepareOperation mShibbolethPrepareOperation;
	private ShibbolethRetrieveOperation mShibbolethRetrieveOperation;

	public LoginDataModel(Context context) {
		mContext = context;
	}

	@Override
	public void clear() {
		clearOperation(mStandardLoginOperation);
		mStandardLoginOperation = null;

		clearOperation(mShibbolethPrepareOperation);
		mShibbolethPrepareOperation = null;

		clearOperation(mShibbolethRetrieveOperation);
		mShibbolethRetrieveOperation = null;

		mContext = null;
	}

	public void standardLogin(StandardLoginDataModelListener listener, String name, String password) {
		clearOperation(mStandardLoginOperation);
		mStandardLoginOperation = null;

		mLoginDataModelListener = listener;


		mStandardLoginOperation = new StandardLoginOperation(mContext, mStandardLoginOperationListener, name, password);
		mStandardLoginOperation.startOperation();
	}


	public void prepareShibboleth(ShibbolethLoginDadaModelListener listener) {
		clearOperation(mShibbolethPrepareOperation);
		mShibbolethPrepareOperation = null;

		mShibbolethLoginDadaModelListener = listener;
		mShibbolethPrepareOperation = new ShibbolethPrepareOperation(mContext, mShibbolethPrepareOperationListener);
		mShibbolethPrepareOperation.startOperation();

	}

	public void retrieveShibboleth(ShibbolethLoginDadaModelListener listener, ShibbolethPrepare prepare) {
		clearOperation(mShibbolethRetrieveOperation);
		mShibbolethRetrieveOperation = null;

		mShibbolethLoginDadaModelListener = listener;

		mShibbolethRetrieveOperation = new ShibbolethRetrieveOperation(mContext, mShibbolethRetrieveOperationListener, prepare);
		mShibbolethRetrieveOperation.startOperation();
	}

	public String getShibbolethLoginUrl(String token) {
		try {
			University u = UniversitiesDataModel.getSavedUniversity(mContext);


			String target = TEST_URL_SHIBBOLETH + "testSP/?loginToken=" + token + "&callback=" + URLEncoder.encode(TEST_URL_SHIBBOLETH_SUCCESS, "UTF-8") + ".sso";

			//String provider = "https://idp-test.univ-paris1.fr";
			return TEST_URL_SHIBBOLETH + "Shibboleth.sso/Login?target=" + URLEncoder.encode(target, "UTF-8") + "&entityID=" + URLEncoder.encode(u.getMobileShibbolethUrl(), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private OperationListener<Login> mShibbolethRetrieveOperationListener = new OperationListener<Login>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, Login result) {
			clearOperation(mShibbolethRetrieveOperation);
			mShibbolethRetrieveOperation = null;

			if (mShibbolethLoginDadaModelListener != null) {
				if (result != null) {

					((UnivMobileApp) mContext.getApplicationContext()).setLogin(result);
					mShibbolethLoginDadaModelListener.shibbolethLoginSuccessfully(result);
				} else {
					mShibbolethLoginDadaModelListener.shibbolethLoginFailed();
				}
			}
		}

		@Override
		public void onPageDownloaded(Login result) {

		}
	};
	private OperationListener<ShibbolethPrepare> mShibbolethPrepareOperationListener = new OperationListener<ShibbolethPrepare>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, ShibbolethPrepare result) {
			clearOperation(mShibbolethPrepareOperation);
			mShibbolethPrepareOperation = null;

			if (mShibbolethLoginDadaModelListener != null) {
				if (result != null) {

					mShibbolethLoginDadaModelListener.shibbolethPreparedSuccessfully(result);
				} else {
					mShibbolethLoginDadaModelListener.shibbolethPrepareFailed();
				}
			}
		}

		@Override
		public void onPageDownloaded(ShibbolethPrepare result) {

		}
	};


	private OperationListener<Login> mStandardLoginOperationListener = new OperationListener<Login>() {
		@Override
		public void onOperationStarted() {
			if (mLoginDataModelListener != null) {
				mLoginDataModelListener.showLoadingIndicator();
			}
		}

		@Override
		public void onOperationFinished(ErrorEntity error, Login result) {
			clearOperation(mStandardLoginOperation);
			mStandardLoginOperation = null;

			if (mLoginDataModelListener != null) {
				if (result != null) {
					((UnivMobileApp) mContext.getApplicationContext()).setLogin(result);
					mLoginDataModelListener.loginSuccessful(result);
				} else {
					mLoginDataModelListener.loginFailed(error);
				}
			}
		}

		@Override
		public void onPageDownloaded(Login result) {

		}
	};

	public interface StandardLoginDataModelListener {
		public void showLoadingIndicator();

		public void loginSuccessful(Login login);

		public void loginFailed(ErrorEntity error);
	}

	public interface ShibbolethLoginDadaModelListener {
		public void shibbolethPreparedSuccessfully(ShibbolethPrepare prepare);

		public void shibbolethPrepareFailed();

		public void shibbolethLoginSuccessfully(Login login);

		public void shibbolethLoginFailed();
	}

}
