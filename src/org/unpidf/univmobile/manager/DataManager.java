package org.unpidf.univmobile.manager;

public class DataManager {

	private static DataManager instance;

	public static DataManager getInstance(){
		if(instance == null){
			instance = new DataManager();
		}
		return instance;
	}

}
