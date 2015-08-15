package org.manicmonkey.lightwidget.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.manicmonkey.lightwidget.R;
import org.manicmonkey.lightwidget.backend.Switch;
import org.manicmonkey.lightwidget.backend.SwitchService;
import org.manicmonkey.lightwidget.backend.SwitchServiceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The configuration screen for the {@link SwitchWidget SwitchWidget} AppWidget.
 */
public class SwitchWidgetConfigureActivity extends Activity {

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Spinner nameSpinner;
    private Spinner onOffSpinner;

    public SwitchWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.switch_widget_configure);
        setupNameSpinner();
        setupOnOffSpinner();

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    private List<String> getSwitchNames() {
        SwitchService switchService = SwitchServiceFactory.getSwitchService(this);
        List<String> switchNames = new ArrayList<>();
        if (switchService != null) {
            for (Switch aSwitch : switchService.get()) {
                switchNames.add(aSwitch.getName());
            }
        }
        return switchNames;
    }

    private void setupNameSpinner() {
        nameSpinner = (Spinner) findViewById(R.id.switch_name_spinner);
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        //todo if not names, tell user why and quit
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                return getSwitchNames();
            }

            @Override
            protected void onPostExecute(List<String> names) {
                adapter.addAll(names);
            }
        }.execute();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nameSpinner.setAdapter(adapter);
    }

    private void setupOnOffSpinner() {
        onOffSpinner = (Spinner) findViewById(R.id.switch_on_off_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.switch_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        onOffSpinner.setAdapter(adapter);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = SwitchWidgetConfigureActivity.this;

            final SwitchWidgetConfiguration switchWidgetConfiguration = new SwitchWidgetConfiguration(context, mAppWidgetId);

            // When the button is clicked, store the configuration
            String name = nameSpinner.getSelectedItem().toString();
            Log.d(getClass().getSimpleName(), "Got name: " + name);
            switchWidgetConfiguration.save(SwitchWidgetConfiguration.PREF_NAME, name);

            String onOff = onOffSpinner.getSelectedItem().toString();
            Log.d(getClass().getSimpleName(), "Got onOff: " + onOff);
            switchWidgetConfiguration.save(SwitchWidgetConfiguration.PREF_SWITCH_ON, onOff.equals("On"));

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            SwitchWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };
}

