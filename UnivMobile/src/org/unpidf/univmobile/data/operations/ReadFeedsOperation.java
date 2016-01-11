package org.unpidf.univmobile.data.operations;

import android.content.Context;
import android.text.Html;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unpidf.univmobile.data.entities.NewsFeed;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-09.
 */
public class ReadFeedsOperation extends AbsOperation<List<NewsFeed>> {

	private static final String NEWS_SEARCH_UNIV = "feeds/search/findAllActiveRssFeedsForUniversity?universityId=%d";

	private int mUniversityID;

	public ReadFeedsOperation(Context c, OperationListener listener, int universityId) {
		super(c, listener);
		mUniversityID = universityId;
	}

	@Override
	protected List<NewsFeed> parse(JSONObject json) throws JSONException {
		JSONObject _embedded = json.getJSONObject("_embedded");
		JSONArray newsJson = _embedded.getJSONArray("feeds");

		List<NewsFeed> news = new ArrayList<NewsFeed>();

		for (int i = 0; i < newsJson.length(); i++) {
			JSONObject oneNJson = newsJson.getJSONObject(i);
			NewsFeed oneNew = new Gson().fromJson(oneNJson.toString(), NewsFeed.class);
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
		/*for (int i = 0; i<mFeedsList.size(); i++) {
			url += "&feedIds=" + mFeedsList.get(i);
		}*/
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
