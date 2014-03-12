package fr.tvbarthel.apps.simpleweatherforcast.utils;


import android.app.Service;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class LocationUtils {

	/**
	 * Get the last known {@link android.location.Location} with a coarse accuracy.
	 *
	 * @param context the {@link android.content.Context} used to retrieve the {@link android.location.LocationManager}.
	 * @return the last known {@link android.location.Location} or null.
	 */
	public static Location getLastKnownLocation(Context context) {
		Location lastKnownLocation = null;
		final LocationManager locationManager = (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);
		final Criteria locationCriteria = new Criteria();
		locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
		final String providerName = locationManager.getBestProvider(locationCriteria, true);
		if (providerName != null) {
			lastKnownLocation = locationManager.getLastKnownLocation(providerName);
		}
		return lastKnownLocation;
	}
}
