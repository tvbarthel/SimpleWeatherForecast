package fr.tvbarthel.apps.simpleweatherforcast.utils;


import android.app.Service;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class LocationUtils {

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
