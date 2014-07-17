package org.unpidf.univmobile.dialog;

import android.app.FragmentManager;
import android.app.FragmentTransaction;


public class DialogFragment extends android.app.DialogFragment{

	public void show(FragmentManager manager, String tag) {
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(this, tag);
		ft.commitAllowingStateLoss();
	}

	@Override
	public void dismissAllowingStateLoss() {
		try{
			super.dismissAllowingStateLoss();
		}catch(Exception e){
		}
	}
}
