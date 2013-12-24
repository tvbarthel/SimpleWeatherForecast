package fr.tvbarthel.apps.simpleweatherforcast.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd/MM", Locale.getDefault());
			((TextView) v.findViewById(R.id.fragment_forecast_date))
					.setText(simpleDateFormat.format(new Date(dailyForecastModel.getDateTime() * 1000)));
			((TextView) v.findViewById(R.id.fragment_forecast_day_temperature))
					.setText(String.valueOf(dailyForecastModel.getTemperature()));
			((TextView) v.findViewById(R.id.fragment_forecast_humidity))
					.setText("Humidity " + String.valueOf(dailyForecastModel.getHumidity()) + "%");
			((TextView) v.findViewById(R.id.fragment_forecast_weather_description))
					.setText(dailyForecastModel.getDescription());
		}
		return v;
	}
}
