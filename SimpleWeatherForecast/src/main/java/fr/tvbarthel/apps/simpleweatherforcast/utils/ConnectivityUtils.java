package fr.tvbarthel.apps.simpleweatherforcast.utils;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityUtils {

	public static boolean isConnected(Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager)
				context.getSystemService(Service.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}
}
