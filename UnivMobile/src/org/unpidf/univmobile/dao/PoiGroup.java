package org.unpidf.univmobile.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.unpidf.univmobile.dao.JSONEnabled.optString;

public class PoiGroup implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4380315275015749480L;

    private final String groupLabel;
    private final List<Poi> listPois;

    public PoiGroup(JSONObject json) throws JSONException {
        this.groupLabel = optString(json, "groupLabel");
        final JSONArray array = json.optJSONArray("pois");
        final List<Poi> listTemp = new ArrayList<Poi>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                listTemp.add(new Poi(array.getJSONObject(i)));
            }
        }
        listPois = listTemp;
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public List<Poi> getListPois() {
        if (listPois != null) {
            return listPois;
        }
        return new ArrayList<Poi>();
    }
}
