package fr.tvbarthel.apps.simpleweatherforcast.openweathermap;


import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DailyForecastJsonParser extends AsyncTask<String, Void, ArrayList<DailyForecastModel>> {

	private static String TAG_WEATHER_LIST = "list";
	private static String TAG_HUMIDITY = "humidity";
	private static String TAG_WEATHER = "weather";
	private static String TAG_WEATHER_DESCRIPTION = "description";
	private static String TAG_TEMPERATURE = "temp";
	private static String TAG_TEMPERATURE_DAY = "day";
	private static String TAG_DATE_TIME = "dt";

	@Override
	protected ArrayList<DailyForecastModel> doInBackground(String... params) {
		return parse(params[0]);
	}

	private ArrayList<DailyForecastModel> parse(final String json) {
		final ArrayList<DailyForecastModel> result = new ArrayList<DailyForecastModel>();
		try {
			JSONObject root = new JSONObject(json);
			JSONArray weatherList = root.getJSONArray(TAG_WEATHER_LIST);
			for (int i = 0; i < weatherList.length(); i++) {
				result.add(parseDailyForecast(weatherList.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	private DailyForecastModel parseDailyForecast(JSONObject jsonDailyForecast) {
		final DailyForecastModel model = new DailyForecastModel();
		model.setDateTime(parseDateTime(jsonDailyForecast));
		model.setHumidity(parseHumidity(jsonDailyForecast));
		model.setDescription(parseWeatherDescription(jsonDailyForecast));
		model.setTemperature(parseDayTemperature(jsonDailyForecast));
		return model;
	}

	private Double parseDayTemperature(JSONObject dailyForecast) {
		Double dayTemperature = 0d;
		try {
			dayTemperature = dailyForecast.getJSONObject(TAG_TEMPERATURE).getDouble(TAG_TEMPERATURE_DAY);
		} catch (JSONException exception) {
			exception.printStackTrace();
		}
		return dayTemperature;
	}

	private String parseWeatherDescription(JSONObject dailyForecast) {
		String weatherDescription = "";
		try {
			weatherDescription = dailyForecast.getJSONArray(TAG_WEATHER).getJSONObject(0).getString(TAG_WEATHER_DESCRIPTION);
		} catch (JSONException exception) {
			exception.printStackTrace();
		}
		return weatherDescription;
	}

	private int parseHumidity(JSONObject dailyForecast) {
		int humidity = 0;
		try {
			humidity = dailyForecast.getInt(TAG_HUMIDITY);
		} catch (JSONException exception) {
			exception.printStackTrace();
		}
		return humidity;
	}

	private long parseDateTime(JSONObject dailyForecast) {
		long dateTime = 0;
		try {
			dateTime = dailyForecast.getLong(TAG_DATE_TIME);
		} catch (JSONException exception) {
			exception.printStackTrace();
		}
		return dateTime;
	}

}
