package fr.tvbarthel.apps.simpleweatherforcast.services;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import fr.tvbarthel.apps.simpleweatherforcast.openweathermap.DailyForecastJsonParser;
import fr.tvbarthel.apps.simpleweatherforcast.openweathermap.DailyForecastModel;
import fr.tvbarthel.apps.simpleweatherforcast.ui.WeatherRemoteViewsFactory;
import fr.tvbarthel.apps.simpleweatherforcast.utils.SharedPreferenceUtils;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        final Context context = getApplicationContext();
        final String lastKnownWeather = SharedPreferenceUtils.getLastKnownWeather(context);
        final ArrayList<DailyForecastModel> dailyForecasts = DailyForecastJsonParser.parse(lastKnownWeather);
        return new WeatherRemoteViewsFactory(getApplicationContext(), intent, dailyForecasts);
    }
}
