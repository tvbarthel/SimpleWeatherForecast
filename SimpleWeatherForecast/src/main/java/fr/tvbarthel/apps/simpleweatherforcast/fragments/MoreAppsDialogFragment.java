package fr.tvbarthel.apps.simpleweatherforcast.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import fr.tvbarthel.apps.simpleweatherforcast.R;

public class MoreAppsDialogFragment extends DialogFragment {

	private static final String URI_ROOT_MARKET = "market://details?id=";
	private static final String URI_ROOT_PLAY_STORE = "http://play.google.com/store/apps/details?id=";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final LayoutInflater inflater = getActivity().getLayoutInflater();

		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		final View dialogView = inflater.inflate(R.layout.dialog_more_apps, null);

		if (dialogView != null) {

			dialogView.findViewById(R.id.dialog_more_apps_chase_whisply).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					launchPlayStoreDetails(getString(R.string.dialog_more_apps_chase_whisply_package_name));
				}
			});

			dialogView.findViewById(R.id.dialog_more_apps_simplethermometer).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					launchPlayStoreDetails(getString(R.string.dialog_more_apps_simplethermometer_package_name));
				}
			});

		}

		dialogBuilder.setView(dialogView).setCancelable(true).setPositiveButton(R.string.dialog_ok, null);

		return dialogBuilder.create();
	}

	private void launchPlayStoreDetails(String appPackageName) {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URI_ROOT_MARKET + appPackageName)));
		} catch (android.content.ActivityNotFoundException activityNotFoundException) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URI_ROOT_PLAY_STORE + appPackageName)));
		}
	}
}
