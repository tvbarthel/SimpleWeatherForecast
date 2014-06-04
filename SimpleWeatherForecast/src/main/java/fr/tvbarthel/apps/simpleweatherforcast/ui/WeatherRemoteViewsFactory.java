package fr.tvbarthel.apps.simpleweatherforcast.ui;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import fr.tvbarthel.apps.simpleweatherforcast.R;
import fr.tvbarthel.apps.simpleweatherforcast.openweathermap.DailyForecastModel;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WeatherRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mAppWidgetId;
    private List<DailyForecastModel> mDailyForecasts;

    public WeatherRemoteViewsFactory(Context context, Intent intent, List<DailyForecastModel> dailyForecasts) {
        mContext = context;
        mDailyForecasts = dailyForecasts;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mDailyForecasts.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final DailyForecastModel dailyForecast = mDailyForecasts.get(position);
        final RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.row_app_widget);
        remoteViews.setTextViewText(R.id.row_app_widget_temperature, "test temperature");
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
