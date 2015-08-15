package org.manicmonkey.lightwidget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.manicmonkey.lightwidget.R;
import org.manicmonkey.lightwidget.SwitchIntentService;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link SwitchWidgetConfigureActivity SwitchWidgetConfigureActivity}
 */
public class SwitchWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            SwitchWidgetConfiguration switchWidgetConfiguration = new SwitchWidgetConfiguration(context, appWidgetId);
            switchWidgetConfiguration.delete(SwitchWidgetConfiguration.PREF_NAME);
            switchWidgetConfiguration.delete(SwitchWidgetConfiguration.PREF_SWITCH_ON);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        SwitchWidgetConfiguration switchWidgetConfiguration = new SwitchWidgetConfiguration(context, appWidgetId);
        String name = switchWidgetConfiguration.getString(SwitchWidgetConfiguration.PREF_NAME);
        boolean switchOn = switchWidgetConfiguration.getBoolean(SwitchWidgetConfiguration.PREF_SWITCH_ON);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.switch_widget);
        Intent intent = new Intent(context, SwitchIntentService.class);
        if (switchOn) {
            views.setImageViewResource(R.id.appwidget_button, R.drawable.pocket_lantern_orange_100);
            intent.setAction(SwitchIntentService.ACTION_SWITCH_ON);
        } else {
            views.setImageViewResource(R.id.appwidget_button, R.drawable.pocket_lantern_gray_100);
            intent.setAction(SwitchIntentService.ACTION_SWITCH_OFF);
        }
        intent.putExtra(SwitchIntentService.EXTRA_SWITCH, name);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

