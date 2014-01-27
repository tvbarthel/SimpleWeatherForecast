package fr.tvbarthel.apps.simpleweatherforcast.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import fr.tvbarthel.apps.simpleweatherforcast.R;

public class MoreAppsDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final LayoutInflater inflater = getActivity().getLayoutInflater();

		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		final View dialogView = inflater.inflate(R.layout.dialog_more_apps, null);

		dialogBuilder.setView(dialogView).setCancelable(true).setPositiveButton("OK", null);

		return dialogBuilder.create();
	}
}
