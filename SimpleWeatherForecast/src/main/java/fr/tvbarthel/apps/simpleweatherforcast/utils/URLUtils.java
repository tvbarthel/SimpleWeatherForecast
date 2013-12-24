package fr.tvbarthel.apps.simpleweatherforcast.utils;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils {

	public static String getAsString(String urlString) throws MalformedURLException, IOException {
		final URL url = new URL(urlString);

		final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setUseCaches(false);

		//TODO handle the response code.
		Log.d("argonne", "code ->" + urlConnection.getResponseCode());

		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		final StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			line += "\n";
			stringBuilder.append(line);
		}
		return stringBuilder.toString();
	}

}
