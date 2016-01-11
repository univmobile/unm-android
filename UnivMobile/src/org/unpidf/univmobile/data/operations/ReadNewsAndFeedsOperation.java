package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.text.Html;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class ReadNewsAndFeedsOperation extends AbsOperation<List<News>> {

	private static final String NEWS_SEARCH_UNIV = "news/search/findNewsForUniversityAndFeeds?universityId=%d";

	private int mUniversityID;
	private int mPage;
	private int mTotalSize;
	private List<Integer> mFeedsList;

	public ReadNewsAndFeedsOperation(Context c, OperationListener listener, int universityId, int page, int totalSize, List<Integer> feedsList) {
		super(c, listener);
		mUniversityID = universityId;
		mPage = page;
		mTotalSize = totalSize;
		mFeedsList = feedsList;
	}

	@Override
	protected List<News> parse(JSONObject json) throws JSONException {
		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray newsJson = _embedded.getJSONArray("news");

		List<News> news = new ArrayList<News>();

		for (int i = 0; i < newsJson.length(); i++) {
			JSONObject oneNJson = newsJson.getJSONObject(i);
			News oneNew = new Gson().fromJson(oneNJson.toString(), News.class);
			oneNew.setDescription(removeTags(oneNew.getDescription()));
			news.add(oneNew);
		}
		return news;
	}


	public String removeTags(String in) {
		String text = Html.fromHtml(in).toString();
		int index = 0;
		int index2 = 0;
		while (index != -1) {
			index = text.indexOf("<");
			index2 = text.indexOf(">", index);
			if (index != -1 && index2 != -1) {
				text = text.substring(0, index).concat(text.substring(index2 + 1, text.length()));
			}
		}
		return text;
	}

	@Override
	protected String getOperationUrl(int page) {
		String url = BASE_URL_API + String.format(NEWS_SEARCH_UNIV, mUniversityID);
		if (mTotalSize != 0) {
			url += "&size=" + mTotalSize;
		}
		if (mPage != 0) {
			url += "&page=" + mPage;
		}
		if(mFeedsList!=null){
		for (int i = 0; i<mFeedsList.size(); i++) {
			url += "&feedIds=" + mFeedsList.get(i);
		}}
		return url;
	}
////
////	@Override
////	protected List<News> combine(List<News> newData, List<News> oldData) {
////		oldData.addAll(newData);
////		return oldData;
////	}
//
//	@Override
//	protected boolean shouldBePaged() {
//		return false;
//	}
}
