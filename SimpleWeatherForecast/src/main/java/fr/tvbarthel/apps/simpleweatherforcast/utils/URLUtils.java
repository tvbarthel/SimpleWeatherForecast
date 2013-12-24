package fr.tvbarthel.apps.simpleweatherforcast.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLUtils {

	public static String getAsString(String urlString) throws MalformedURLException, IOException {
		final URL url = new URL(urlString);
		final URLConnection connection = url.openConnection();
		connection.setUseCaches(false);
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(10000);

		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		final StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			line += "\n";
			stringBuilder.append(line);
		}
		return stringBuilder.toString();
	}
}
