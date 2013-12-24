package fr.tvbarthel.apps.simpleweatherforcast.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferenceUtils {
	private static String KEY_LAST_UPDATE = "SharedPreferenceUtils.Key.LastUpdate";
	private static String KEY_LAST_KNOWN_JSON_WEATHER = "SharedPreferenceUtils.Key.LastKnownJsonWeather";

	private static SharedPreferences getDefaultSharedPreferences(final Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static String getLastKnownWeather(final Context context) {
		return getDefaultSharedPreferences(context).getString(KEY_LAST_KNOWN_JSON_WEATHER, null);
	}

	public static long getLastUpdate(final Context context) {
		return getDefaultSharedPreferences(context).getLong(KEY_LAST_UPDATE, 0);
	}

	public static void storeWeather(final Context context, final String jsonWeather) {
		final Editor editor = getDefaultSharedPreferences(context).edit();
		editor.putString(KEY_LAST_KNOWN_JSON_WEATHER, jsonWeather);
		editor.putLong(KEY_LAST_UPDATE, System.currentTimeMillis());
		editor.apply();
	}

}
