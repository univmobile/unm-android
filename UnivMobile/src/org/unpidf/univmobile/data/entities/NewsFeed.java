package org.unpidf.univmobile.data.entities;

/**
 * Created by PC on 12/8/2015.
 */
public class NewsFeed {
    private String name = "Vienas1";
    private boolean isChecked;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
