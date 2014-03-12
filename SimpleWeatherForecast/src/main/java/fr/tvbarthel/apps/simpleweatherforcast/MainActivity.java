package fr.tvbarthel.apps.simpleweatherforcast;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import fr.tvbarthel.apps.simpleweatherforcast.fragments.AboutDialogFragment;
import fr.tvbarthel.apps.simpleweatherforcast.fragments.ForecastFragment;
import fr.tvbarthel.apps.simpleweatherforcast.fragments.LicenseDialogFragment;
import fr.tvbarthel.apps.simpleweatherforcast.fragments.MoreAppsDialogFragment;
import fr.tvbarthel.apps.simpleweatherforcast.fragments.TemperatureUnitPickerDialogFragment;
import fr.tvbarthel.apps.simpleweatherforcast.openweathermap.DailyForecastJsonGetter;
import fr.tvbarthel.apps.simpleweatherforcast.openweathermap.DailyForecastJsonParser;
import fr.tvbarthel.apps.simpleweatherforcast.openweathermap.DailyForecastModel;
import fr.tvbarthel.apps.simpleweatherforcast.ui.AlphaForegroundColorSpan;
import fr.tvbarthel.apps.simpleweatherforcast.utils.ColorUtils;
import fr.tvbarthel.apps.simpleweatherforcast.utils.ConnectivityUtils;
import fr.tvbarthel.apps.simpleweatherforcast.utils.LocationUtils;
import fr.tvbarthel.apps.simpleweatherforcast.utils.SharedPreferenceUtils;

public class MainActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final long REFRESH_TIME_AUTO = 1000 * 60 * 60 * 2; // 2 hours in millis.
	private static final long REFRESH_TIME_MANUAL = 1000 * 60 * 10; // 10 minutes.

	private ForecastPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private Toast mTextToast;
	private String mTemperatureUnit;
	private SpannableString mActionBarSpannableTitle;
	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private TypefaceSpan mTypefaceSpanLight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Get the temperature unit symbol
		mTemperatureUnit = SharedPreferenceUtils.getTemperatureUnitSymbol(this);

		final ActionBar actionBar = getSupportActionBar();
		// Add some transparency to the action bar background
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(130, 0, 0, 0)));
		// Hide the app icon in the actionBar
		actionBar.setDisplayShowHomeEnabled(false);

		mSectionsPagerAdapter = new ForecastPagerAdapter(getSupportFragmentManager());
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(Color.WHITE);
		mTypefaceSpanLight = new TypefaceSpan("sans-serif-light");

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setPageTransformer(true, new ForecastPageTransformer());
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int currentPosition, float positionOffset, int i2) {
				setGradientBackgroundColor(currentPosition, positionOffset);
				updateActionBarTitle(currentPosition, positionOffset);
			}

			@Override
			public void onPageSelected(int i) {
			}

			@Override
			public void onPageScrollStateChanged(int i) {
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		final String lastKnownWeather = SharedPreferenceUtils.getLastKnownWeather(getApplicationContext());

		SharedPreferenceUtils.registerOnSharedPreferenceChangeListener(this, this);

		//Load the last known weather
		loadDailyForecast(lastKnownWeather);

		//Check if the last known weather is out dated.
		if (isWeatherOutdated(REFRESH_TIME_AUTO) || lastKnownWeather == null) {
			updateDailyForecast();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferenceUtils.unregisterOnSharedPreferenceChangeListener(this, this);
		hideToast();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_item_manual_refresh) {
			if (isWeatherOutdated(REFRESH_TIME_MANUAL)) {
				updateDailyForecast();
			} else {
				makeTextToast(R.string.toast_already_up_to_date);
			}
			return true;
		} else if (id == R.id.menu_item_license) {
			(new LicenseDialogFragment()).show(getSupportFragmentManager(), "dialog_license");
		} else if (id == R.id.menu_item_about) {
			(new AboutDialogFragment()).show(getSupportFragmentManager(), "dialog_about");
		} else if (id == R.id.menu_item_more_apps) {
			(new MoreAppsDialogFragment()).show(getSupportFragmentManager(), "dialog_more_apps");
		} else if (id == R.id.menu_item_unit_picker) {
			final String[] temperatureUnitNames = getResources().getStringArray(R.array.temperature_unit_names);
			final String[] temperatureUnitSymbols = getResources().getStringArray(R.array.temperature_unit_symbols);
			(TemperatureUnitPickerDialogFragment.newInstance(temperatureUnitNames, temperatureUnitSymbols))
					.show(getSupportFragmentManager(), "dialog_unit_picker");
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(SharedPreferenceUtils.KEY_TEMPERATURE_UNIT_SYMBOL)) {
			mTemperatureUnit = SharedPreferenceUtils.getTemperatureUnitSymbol(this);
			mSectionsPagerAdapter.notifyDataSetChanged();
			invalidatePageTransformer();
		}
	}

	private boolean isWeatherOutdated(long refreshTimeInMillis) {
		final long lastUpdate = SharedPreferenceUtils.getLastUpdate(getApplicationContext());
		return System.currentTimeMillis() - lastUpdate > refreshTimeInMillis;
	}

	private void updateDailyForecast() {
		if (ConnectivityUtils.isConnected(getApplicationContext())) {
			final Location lastKnownLocation = LocationUtils.getLastKnownLocation(getApplicationContext());
			if (lastKnownLocation != null) {
				new DailyForecastJsonGetter(getApplicationContext()) {
					@Override
					protected void onPostExecute(String newJsondailyForecast) {
						super.onPostExecute(newJsondailyForecast);
						loadDailyForecast(newJsondailyForecast);
					}
				}.execute(lastKnownLocation);
			} else {
				makeTextToast(R.string.toast_not_allowed_to_access_location);
			}
		} else {
			makeTextToast(R.string.toast_no_connection_available);
		}
	}

	private void makeTextToast(int stringResourceId) {
		hideToast();
		mTextToast = Toast.makeText(this, stringResourceId, Toast.LENGTH_LONG);
		mTextToast.show();
	}

	private void hideToast() {
		if (mTextToast != null) {
			mTextToast.cancel();
			mTextToast = null;
		}
	}

	private void loadDailyForecast(String jsonDailyForecast) {
		if (jsonDailyForecast != null) {
			new DailyForecastJsonParser() {
				@Override
				protected void onPostExecute(ArrayList<DailyForecastModel> dailyForecastModels) {
					super.onPostExecute(dailyForecastModels);
					mSectionsPagerAdapter.updateModels(dailyForecastModels);
					invalidatePageTransformer();
				}
			}.execute(jsonDailyForecast);
		}
	}

	/**
	 * Trick to notify the pageTransformer of a data set change.
	 */
	private void invalidatePageTransformer() {
		if (mViewPager.getAdapter().getCount() > 0) {
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					if (mViewPager.beginFakeDrag()) {
						mViewPager.fakeDragBy(0f);
						mViewPager.endFakeDrag();
					}
				}
			});
		}
	}

	private void setGradientBackgroundColor(int currentPosition, float positionOffset) {
		final GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
				new int[]{getColor(currentPosition, positionOffset), getColor(currentPosition, (float) Math.pow(positionOffset, 0.40))});
		getWindow().setBackgroundDrawable(g);
	}

	private void setActionBarAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mActionBarSpannableTitle.setSpan(mAlphaForegroundColorSpan, 0, mActionBarSpannableTitle.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(mActionBarSpannableTitle);
	}

	private void updateActionBarTitle(int currentPosition, float positionOffset) {
		float alpha = 1 - positionOffset * 2;
		if (positionOffset >= 0.5) {
			currentPosition++;
			alpha = (positionOffset - 0.5f) * 2;
		}
		setActionBarTitle(currentPosition);
		setActionBarAlpha(alpha);
	}

	private void setActionBarTitle(int position) {
		String newTitle = getActionBarTitle(position);
		if (mActionBarSpannableTitle == null || !mActionBarSpannableTitle.toString().equals(newTitle)) {
			mActionBarSpannableTitle = new SpannableString(newTitle);
			mActionBarSpannableTitle.setSpan(mTypefaceSpanLight, 0, mActionBarSpannableTitle.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
	}

	private String getActionBarTitle(int currentPosition) {
		final DailyForecastModel currentModel = mSectionsPagerAdapter.getModel(currentPosition);
		final Date dateFromUnixTimeStamp = new Date(currentModel.getDateTime() * 1000);
		return new SimpleDateFormat("EEEE dd MMMM", Locale.getDefault()).format(dateFromUnixTimeStamp);
	}


	private int getColor(int currentPosition, float positionOffset) {
		//retrieve current color and next color relative to the current position.
		final int colorSize = ColorUtils.COLORS.length;
		final int[] currentColor = ColorUtils.COLORS[(currentPosition) % colorSize];
		final int[] nextColor = ColorUtils.COLORS[(currentPosition + 1) % colorSize];

		//Compute the deltas relative to the current position offset.
		final int deltaR = (int) ((nextColor[0] - currentColor[0]) * positionOffset);
		final int deltaG = (int) ((nextColor[1] - currentColor[1]) * positionOffset);
		final int deltaB = (int) ((nextColor[2] - currentColor[2]) * positionOffset);

		return Color.argb(255, currentColor[0] + deltaR, currentColor[1] + deltaG, currentColor[2] + deltaB);
	}


	/**
	 * A {@link FragmentStatePagerAdapter} used for {@link DailyForecastModel}
	 */
	public class ForecastPagerAdapter extends FragmentStatePagerAdapter {

		private final ArrayList<DailyForecastModel> mDailyForecastModels;

		public ForecastPagerAdapter(FragmentManager fm) {
			super(fm);
			mDailyForecastModels = new ArrayList<DailyForecastModel>();
		}

		public void updateModels(ArrayList<DailyForecastModel> newModels) {
			mDailyForecastModels.clear();
			mDailyForecastModels.addAll(newModels);
			notifyDataSetChanged();
		}

		public DailyForecastModel getModel(int position) {
			if (position >= mDailyForecastModels.size()) {
				return new DailyForecastModel();
			}
			return mDailyForecastModels.get(position);
		}

		@Override
		public Fragment getItem(int position) {
			return ForecastFragment.newInstance(mDailyForecastModels.get(position), mTemperatureUnit);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return mDailyForecastModels.size();
		}
	}

	/**
	 * A {@link PageTransformer} used to scale the fragments.
	 */
	public class ForecastPageTransformer implements PageTransformer {

		public void transformPage(View view, float position) {
			final int pageWidth = view.getWidth();

			if (position < -2.0) { // [-Infinity,-2.0)
				// This page is way off-screen to the left.
				ViewHelper.setAlpha(view, 0);

			} else if (position <= 2.0) { // [-2.0,2.0]
				final float normalizedPosition = Math.abs(position / 2);
				final float scaleFactor = 1f - normalizedPosition;
				final float horizontalMargin = pageWidth * normalizedPosition;

				//Translate back the page.
				if (position < 0) {
					//left
					ViewHelper.setTranslationX(view, horizontalMargin);
				} else {
					//right
					ViewHelper.setTranslationX(view, -horizontalMargin);
				}

				// Scale the page down relative to its size.
				ViewHelper.setScaleX(view, (float) Math.pow(scaleFactor, 0.80));
				ViewHelper.setScaleY(view, (float) Math.pow(scaleFactor, 0.80));

				// Fade the page relative to its size.
				ViewHelper.setAlpha(view, 1f * scaleFactor - (scaleFactor * 0.25f));

			} else { // (2.0,+Infinity]
				// This page is way off-screen to the right.
				ViewHelper.setAlpha(view, 0);
			}
		}
	}

}
