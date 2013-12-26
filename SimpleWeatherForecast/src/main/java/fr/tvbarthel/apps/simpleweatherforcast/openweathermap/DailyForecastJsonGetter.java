package fr.tvbarthel.apps.simpleweatherforcast.openweathermap;


import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;

import fr.tvbarthel.apps.simpleweatherforcast.utils.SharedPreferenceUtils;
import fr.tvbarthel.apps.simpleweatherforcast.utils.URLUtils;

public class DailyForecastJsonGetter extends AsyncTask<Location, Void, String> {

	private Context mContext;

	public DailyForecastJsonGetter(Context context) {
		super();
		mContext = context;
	}

	@Override
	protected String doInBackground(Location... params) {
		final Location lastKnownLocation = params[0];
		String json = getJsonAsString(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
		if (json != null) {
			SharedPreferenceUtils.storeWeather(mContext, json);
		}
		return json;
	}

	private String getJsonAsString(double latitude, double longitude) {
		String result = null;
		try {
			//TODO use real locale.
			final String lang = Locale.getDefault().getLanguage();
			result = URLUtils.getAsString("http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + String.valueOf(latitude) + "&lon=" + String.valueOf(longitude) + "&cnt=14&mode=json&lang=" + lang + "&units=metric&APPID=c756ce72a59777bd32a5762e12e74057");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
