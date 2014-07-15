package org.unpidf.univmobile.manager;

import org.unpidf.univmobile.dao.University;

public class DataManager {

	private static DataManager instance;
	private static University currentUniversity;
	
	public static DataManager getInstance(){
		if(instance == null){
			instance = new DataManager();
		}
		return instance;
	}
	
	public static University getCurrentUniversity() {
		return currentUniversity;
	}

}
