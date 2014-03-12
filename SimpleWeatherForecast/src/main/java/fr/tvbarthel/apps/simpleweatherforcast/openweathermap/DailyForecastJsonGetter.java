package fr.tvbarthel.apps.simpleweatherforcast.openweathermap;


import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;

import fr.tvbarthel.apps.simpleweatherforcast.R;
import fr.tvbarthel.apps.simpleweatherforcast.utils.SharedPreferenceUtils;
import fr.tvbarthel.apps.simpleweatherforcast.utils.URLUtils;

/**
 * An {@link AsyncTask} used to retrieve the weather forecast as a Json from openweathermap.
 * <p/>
 * This class uses the openweathermap API.
 * http://openweathermap.org/
 */
public class DailyForecastJsonGetter extends AsyncTask<Location, Void, String> {

    private Context mContext;

    public DailyForecastJsonGetter(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected String doInBackground(Location... params) {
        final Location lastKnownLocation = params[0];
        final String json = getJsonAsString(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        if (json != null) {
            SharedPreferenceUtils.storeWeather(mContext, json);
        }
        return json;
    }

    private String getJsonAsString(double latitude, double longitude) {
        String result = null;
        try {
            //Retrieve the language.
            final String lang = Locale.getDefault().getLanguage();
            //Forge the url for the open weather map API.
            final String url = mContext.getString(R.string.url_open_weather_map_api, lang, latitude, longitude);
            //retrieve the response as a string.
            result = URLUtils.getAsString(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
