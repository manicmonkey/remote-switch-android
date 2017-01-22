package org.manicmonkey.lightwidget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import org.manicmonkey.lightwidget.R;
import org.manicmonkey.lightwidget.SwitchIntentService;

/**
 * @author James - 2017-01-22.
 */
public class SwitchWidgetMultiple extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            Log.d(this.getClass().getSimpleName(), "OnUpdate");
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.switch_widget_multiple);
            Intent intent = new Intent(context, SwitchWidgetMultipleService.class);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            remoteViews.setRemoteAdapter(R.id.list_switches, intent);

            final PendingIntent pendingIntent = PendingIntent.getService(context, 0, new Intent(context, SwitchIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.list_switches, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
