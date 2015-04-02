package org.unpidf.univmobile.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import org.unpidf.univmobile.R;

/**
 * Created by rviewniverse on 2015-02-23.
 */
public class SimpleDialogFragment extends DialogFragment {

	public static SimpleDialogFragment newInstance(String title) {
		SimpleDialogFragment frag = new SimpleDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");

		return new AlertDialog.Builder(getActivity()).setMessage(title).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dismiss();
			}
		}).create();
	}
}
