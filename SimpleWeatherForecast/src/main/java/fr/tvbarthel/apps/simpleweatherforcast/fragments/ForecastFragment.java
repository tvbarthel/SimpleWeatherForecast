package fr.tvbarthel.apps.simpleweatherforcast.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.tvbarthel.apps.simpleweatherforcast.R;

/**
 * A simple {@link android.support.v4.app.Fragment} that displays the weather forecast of one day.
 */
public class ForecastFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_forecast, container, false);
		return v;
	}
}
