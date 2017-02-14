package org.manicmonkey.lightwidget.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.crashlytics.android.Crashlytics;

import org.manicmonkey.lightwidget.R;
import org.manicmonkey.lightwidget.SwitchIntentService;
import org.manicmonkey.lightwidget.backend.Switch;
import org.manicmonkey.lightwidget.backend.SwitchService;
import org.manicmonkey.lightwidget.backend.SwitchServiceFactory;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * @author James - 2017-01-22.
 */
public class SwitchWidgetMultipleRowsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private List<String> switchNames;

    public SwitchWidgetMultipleRowsFactory(Context context, Intent intent) {
        this.context = context;
        Fabric.with(context, new Crashlytics());
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        final SwitchService switchService = SwitchServiceFactory.getSwitchService(context);
        List<String> switchNames = new ArrayList<>();
        for (Switch aSwitch : switchService.get()) {
            switchNames.add(aSwitch.getName());
        }
        this.switchNames = switchNames;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        Log.d(this.getClass().getSimpleName(), "Got count: " + switchNames.size());
        return switchNames.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final String switchName = switchNames.get(position);

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.switch_widget_multiple_row);
        remoteViews.setTextViewText(R.id.lbl_switch_name, switchName);

        setupSwitchButton(remoteViews, R.id.btn_switch_on, switchName, SwitchIntentService.ACTION_SWITCH_ON);
        setupSwitchButton(remoteViews, R.id.btn_switch_off, switchName, SwitchIntentService.ACTION_SWITCH_OFF);

        Log.d(getClass().getSimpleName(), "Configured remote view for switch: " + switchName);
        return remoteViews;
    }

    private void setupSwitchButton(RemoteViews remoteViews, int btnResId, String switchName, String action) {
        Intent fillInIntent = new Intent(context, SwitchIntentService.class);
        fillInIntent.setAction(action);
        fillInIntent.putExtra(SwitchIntentService.EXTRA_SWITCH, switchName);
        remoteViews.setOnClickFillInIntent(btnResId, fillInIntent);
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
