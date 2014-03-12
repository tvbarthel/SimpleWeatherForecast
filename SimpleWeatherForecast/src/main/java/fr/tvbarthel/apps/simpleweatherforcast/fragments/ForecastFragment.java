package fr.tvbarthel.apps.simpleweatherforcast.fragments;

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
    public static String ARGUMENT_TEMPERATURE_UNIT = "ForecastFragment.TemperatureUnit";

    public static ForecastFragment newInstance(DailyForecastModel model, String temperatureUnit) {
        final ForecastFragment instance = new ForecastFragment();
        final Bundle arguments = new Bundle();
        arguments.putParcelable(ARGUMENT_MODEL, model);
        arguments.putString(ARGUMENT_TEMPERATURE_UNIT, temperatureUnit);
        instance.setArguments(arguments);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_forecast, container, false);
        final Bundle arguments = getArguments();
        final DailyForecastModel dailyForecastModel = arguments.getParcelable(ARGUMENT_MODEL);
        final String temperatureUnit = arguments.getString(ARGUMENT_TEMPERATURE_UNIT);
        if (dailyForecastModel != null) {
            ((TextView) v.findViewById(R.id.fragment_forecast_day_temperature))
                    .setText(String.valueOf(convertTemperature(dailyForecastModel.getTemperature(), temperatureUnit)) + temperatureUnit);

			/* Not sure if people really want to see the humidity ~
            final int humidity = dailyForecastModel.getHumidity();
			if(humidity != 0) {
				((TextView) v.findViewById(R.id.fragment_forecast_humidity))
						.setText("Humidity " + String.valueOf(humidity) + "%");
			} else {
				(v.findViewById(R.id.fragment_forecast_humidity))
						.setVisibility(View.GONE);
			}*/

            final long minTemperature = convertTemperature(dailyForecastModel.getMinTemperature(), temperatureUnit);
            final long maxTemperature = convertTemperature(dailyForecastModel.getMaxTemperature(), temperatureUnit);

            ((TextView) v.findViewById(R.id.fragment_forecast_min_max)).setText(getString(R.string.forecast_fragment_min_max_temperature, minTemperature, maxTemperature));

            ((TextView) v.findViewById(R.id.fragment_forecast_weather_description))
                    .setText(dailyForecastModel.getDescription());
        }
        return v;
    }

    private long convertTemperature(double temperatureInCelsius, String temperatureUnit) {
        double temperatureConverted = temperatureInCelsius;
        if (temperatureUnit.equals(getString(R.string.temperature_unit_fahrenheit_symbol))) {
            temperatureConverted = temperatureInCelsius * 1.8f + 32f;
        } else if (temperatureUnit.equals(getString(R.string.temperature_unit_kelvin_symbol))) {
            temperatureConverted = temperatureInCelsius + 273.15f;
        }
        return Math.round(temperatureConverted);
    }
}
