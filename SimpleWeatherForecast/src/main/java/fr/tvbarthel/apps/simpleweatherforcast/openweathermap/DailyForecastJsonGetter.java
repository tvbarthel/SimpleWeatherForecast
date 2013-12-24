package fr.tvbarthel.apps.simpleweatherforcast.openweathermap;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;

import fr.tvbarthel.apps.simpleweatherforcast.utils.SharedPreferenceUtils;
import fr.tvbarthel.apps.simpleweatherforcast.utils.URLUtils;

public class DailyForecastJsonGetter extends AsyncTask<Void, Void, String> {

	private Context mContext;

	public DailyForecastJsonGetter(Context context) {
		super();
		mContext = context;
	}

	@Override
	protected String doInBackground(Void... params) {
		String json = getJsonAsString();
		if(json != null) {
			SharedPreferenceUtils.storeWeather(mContext, json);
		}
		return json;
	}

	private String getJsonAsString() {
		String result = null;
		try {
			//TODO use real location.
			//TODO use real locale.
			result = URLUtils.getAsString("http://api.openweathermap.org/data/2.5/forecast/daily?lat=45.833619&lon=1.261105&cnt=14&mode=json&lang=fr&units=metric&APPID=c756ce72a59777bd32a5762e12e74057");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
