package fr.tvbarthel.apps.simpleweatherforcast.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.tvbarthel.apps.simpleweatherforcast.R;

/**
 * A simple {@link android.support.v4.app.Fragment} that displays the weather forecast of one day.
 */
public class ForecastFragment extends Fragment {

	public static String ARGUMENT_POSITION = "Position";

	public static ForecastFragment newInstance(int position) {
		final ForecastFragment instance = new ForecastFragment();
		final Bundle arguments = new Bundle();
		arguments.putInt(ARGUMENT_POSITION, position);
		instance.setArguments(arguments);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_forecast, container, false);
		((TextView)v.findViewById(R.id.forecast_fragment_position)).setText(String.valueOf(getArguments().getInt(ARGUMENT_POSITION)));
		return v;
	}
}
