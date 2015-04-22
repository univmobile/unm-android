package org.unpidf.univmobile.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.ui.activities.HomeActivity;

/**
 * Created by RokasTS on 2015.04.01.
 */
public class ConfirmationDialogFragment extends DialogFragment {

    public static ConfirmationDialogFragment newInstance(String title, String message) {
        ConfirmationDialogFragment frag = new ConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, onPositiveClickListener)
                .setNegativeButton(R.string.no, onNegativeClickListener)
                .create();
    }

    private DialogInterface.OnClickListener onNegativeClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dismiss();
        }
    };

    private DialogInterface.OnClickListener onPositiveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dismiss();
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).onPositiveButtonClicked();
            }
        }
    };
}
