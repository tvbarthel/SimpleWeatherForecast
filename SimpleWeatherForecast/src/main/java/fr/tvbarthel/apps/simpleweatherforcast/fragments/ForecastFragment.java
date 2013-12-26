package fr.tvbarthel.apps.simpleweatherforcast.fragments;

import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.tvbarthel.apps.simpleweatherforcast.R;
import fr.tvbarthel.apps.simpleweatherforcast.openweathermap.DailyForecastModel;

/**
 * A simple {@link android.support.v4.app.Fragment} that displays the weather forecast of one day.
 */
public class ForecastFragment extends Fragment {

	public static String ARGUMENT_MODEL = "ForecastFragment.DailyForecastModel";

	public static ForecastFragment newInstance(DailyForecastModel model) {
		final ForecastFragment instance = new ForecastFragment();
		final Bundle arguments = new Bundle();
		arguments.putParcelable(ARGUMENT_MODEL, model);
		instance.setArguments(arguments);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_forecast, container, false);
		final DailyForecastModel dailyForecastModel = getArguments().getParcelable(ARGUMENT_MODEL);
		if (dailyForecastModel != null) {
			((TextView) v.findViewById(R.id.fragment_forecast_day_temperature))
					.setText(String.valueOf(Math.round(dailyForecastModel.getTemperature())) + "°C");

			/* Not sure if people really want to see the humidity ~
			final int humidity = dailyForecastModel.getHumidity();
			if(humidity != 0) {
				((TextView) v.findViewById(R.id.fragment_forecast_humidity))
						.setText("Humidity " + String.valueOf(humidity) + "%");
			} else {
				(v.findViewById(R.id.fragment_forecast_humidity))
						.setVisibility(View.GONE);
			}*/

			final String minTemperature = String.valueOf(Math.round(dailyForecastModel.getMinTemperature()));
			final String maxTemperature = String.valueOf(Math.round(dailyForecastModel.getMaxTemperature()));

			((TextView) v.findViewById(R.id.fragment_forecast_min_max)).setText(minTemperature + " ~ " + maxTemperature);

			((TextView) v.findViewById(R.id.fragment_forecast_weather_description))
					.setText(dailyForecastModel.getDescription());
		}
		return v;
	}
}
