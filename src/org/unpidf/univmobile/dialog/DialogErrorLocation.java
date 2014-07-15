package org.unpidf.univmobile.dialog;

import org.unpidf.univmobile.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class DialogErrorLocation extends DialogFragment {

	private static boolean isShown = false;

	public static DialogErrorLocation newInstance() {
		if(!isShown){
			isShown = true;
			return new DialogErrorLocation();
		}
		return null;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(R.string.textAlertGeolocTitle);
		builder.setMessage(R.string.textAlertGeoloc);
		builder.setPositiveButton(R.string.textAlertDialogOui, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				getActivity().startActivity(intent);
				dismiss();
			}
		});
		builder.setNeutralButton(R.string.textAlertDialogNon, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dismiss();
			}
		});
		builder.setNegativeButton(R.string.textAlertDialogJamais , new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dismiss();
				//Utils.addPrefBool(getActivity(), "Geoloc", "fin", true);
			}
		});
		return builder.create();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		isShown = false;
		super.onDismiss(dialog);
	}
}
