package org.unpidf.univmobile.dialog;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class DialogFragment extends android.support.v4.app.DialogFragment{

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
