package fr.tvbarthel.apps.simpleweatherforcast.receivers;


import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import fr.tvbarthel.apps.simpleweatherforcast.MainActivity;
import fr.tvbarthel.apps.simpleweatherforcast.R;
import fr.tvbarthel.apps.simpleweatherforcast.services.AppWidgetService;

public class WeatherWidgetReceiver extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Update each of the widgets with the remote adapter
        for (int appWidgetId : appWidgetIds) {
            RemoteViews layout = buildLayout(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, layout);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private RemoteViews buildLayout(Context context, int appWidgetId) {
        // Specify the service to provide data for the collection widget.  Note that we need to
        // embed the appWidgetId via the data otherwise it will be ignored.
        final Intent intent = new Intent(context, AppWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        final RemoteViews remoteViews;
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        remoteViews.setRemoteAdapter(appWidgetId, R.id.app_widget_list_view, intent);
        remoteViews.setEmptyView(R.id.app_widget_list_view, R.id.app_widget_empty_view);

        // Bind a click listener template for the contents of the weather list.
        final Intent onClickIntent = new Intent(context, MainActivity.class);
        final PendingIntent onClickPendingIntent = PendingIntent.getActivity(context, 0, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.app_widget_list_view, onClickPendingIntent);

        return remoteViews;
    }
}
