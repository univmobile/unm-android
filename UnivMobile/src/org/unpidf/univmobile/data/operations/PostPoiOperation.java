package org.unpidf.univmobile.data.operations;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.entities.University;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class PostPoiOperation extends AbsOperation<Boolean> {


	private static final String POIS = "pois/";

	private static final String POI_ACTIVE = "active";
	private static final String POI_NAME = "name";
	private static final String POI_CATEGORY = "category";
	private static final String POI_UNIVERSITY = "university";
	private static final String POI_CREATEDON = "createdon";
	private static final String POI_UPDATEDON = "updatedon";
	private static final String POI_ADDRESS = "address";
	private static final String POI_CITY = "city";
	private static final String POI_PHONES = "phones";
	private static final String POI_EMAIL = "email";
	private static final String POI_DESCRIPTION = "description";
	private static final String POI_LATITUDE = "lat";
	private static final String POI_LONGITUED = "lng";
	private static final String POI_HASETHERNET = "hasEthernet";
	private static final String POI_HASWIFI = "hasWifi";
	private static final String POI_ICONRUEDESFACS = "iconRuedesfacs";




	private Category mCat;
	private University mUniv;
	private String mName;
	private String mDate;
	private String mAddress;
	private String mCity;
	private String mPhone;
	private String mMail;
	private String mDescription;
	private String mLat;
	private String mLng;

	public PostPoiOperation(Context c, OperationListener listener, Category cat, University univ, String name, String date, String address, String city, String phone, String mail, String description, String lat, String lng) {
		super(c, listener);

		this.mCat = cat;
		this.mUniv = univ;
		this.mName = name;
		this.mDate = date;
		this.mAddress = address;
		this.mCity = city;
		this.mPhone = phone;
		this.mMail = mail;
		this.mDescription = description;
		mLat = lat;
		mLng = lng;
	}

	@Override
	protected Boolean parse(JSONObject json) throws JSONException {
		return true;
	}

	@Override
	protected String getOperationUrl(int page) {
		return BASE_URL_API + POIS;
	}


	@Override
	protected REQUEST getRequestType() {
		return REQUEST.POST;
	}

	@Override
	protected String getBody() {


		try {
			JSONObject json = new JSONObject();
			json.put(POI_ACTIVE, true);
			json.put(POI_NAME, mName);
			json.put(POI_CATEGORY, mCat.getSelf());
			json.put(POI_UNIVERSITY, mUniv.getSelf());
			json.put(POI_CREATEDON, mDate);
			json.put(POI_ADDRESS, mAddress);
			json.put(POI_CITY, mCity);
			json.put(POI_PHONES, mPhone);
			json.put(POI_EMAIL, mMail);
			json.put(POI_DESCRIPTION, mDescription);

			json.put(POI_HASETHERNET, 1);
			json.put(POI_HASWIFI, 1);
			json.put(POI_ICONRUEDESFACS, 1);

			if (mLng != null && mLat != null) {
				json.put(POI_LATITUDE, mLat);
				json.put(POI_LONGITUED, mLng);
			}

			return json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	@Override
	protected void handleStatusLine(HttpResponse response) {
		if (response.getStatusLine().getStatusCode() != 201) {
			mError = new ErrorEntity(ErrorEntity.ERROR_TYPE.UNAUTHORIZED);
		}
	}
}
