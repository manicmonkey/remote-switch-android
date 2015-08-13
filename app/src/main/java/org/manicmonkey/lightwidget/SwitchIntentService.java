package org.manicmonkey.lightwidget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.manicmonkey.lightwidget.backend.Switch;
import org.manicmonkey.lightwidget.backend.SwitchAction;
import org.manicmonkey.lightwidget.backend.SwitchService;

import retrofit.RestAdapter;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class SwitchIntentService extends IntentService {

    public static final String ACTION_SWITCH_ON = "org.manicmonkey.lightwidget.action.ON";
    public static final String ACTION_SWITCH_OFF = "org.manicmonkey.lightwidget.action.OFF";

    public static final String EXTRA_GROUP = "org.manicmonkey.lightwidget.extra.GROUP";
    public static final String EXTRA_SWITCH = "org.manicmonkey.lightwidget.extra.SWITCH";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionSwitchOn(Context context, String group, String switchNumber) {
        Intent intent = new Intent(context, SwitchIntentService.class);
        intent.setAction(ACTION_SWITCH_ON);
        intent.putExtra(EXTRA_GROUP, group);
        intent.putExtra(EXTRA_SWITCH, switchNumber);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionSwitchOff(Context context, String group, String switchNumber) {
        Intent intent = new Intent(context, SwitchIntentService.class);
        intent.setAction(ACTION_SWITCH_OFF);
        intent.putExtra(EXTRA_GROUP, group);
        intent.putExtra(EXTRA_SWITCH, switchNumber);
        context.startService(intent);
    }

    public SwitchIntentService() {
        super("SwitchIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SWITCH_ON.equals(action)) {
                final String group = intent.getStringExtra(EXTRA_GROUP);
                final String switchNumber = intent.getStringExtra(EXTRA_SWITCH);
                handleActionSwitchOn(group, switchNumber);
            } else if (ACTION_SWITCH_OFF.equals(action)) {
                final String group = intent.getStringExtra(EXTRA_GROUP);
                final String switchNumber = intent.getStringExtra(EXTRA_SWITCH);
                handleActionSwitchOff(group, switchNumber);
            }
        }
    }

    /**
     * Handle action SwitchOn in the provided background thread with the provided parameters.
     */
    private void handleActionSwitchOn(String group, String switchNumber) {
        Log.d(getClass().getSimpleName(), "Turn on group[" + group + "] switch[" + switchNumber + "]");
        performSwitchRequest(group, switchNumber, true);
        int resId = R.string.switched_on;
        showToast(resId);
    }

    /**
     * Handle action SwitchOff in the provided background thread with the provided parameters.
     */
    private void handleActionSwitchOff(String group, String switchNumber) {
        Log.d(getClass().getSimpleName(), "Turn off group[" + group + "] switch[" + switchNumber + "]");
        performSwitchRequest(group, switchNumber, false);
        showToast(R.string.switched_off);
    }

    private void performSwitchRequest(String group, String switchNumber, boolean switchOn) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String serverAddress = sharedPreferences.getString("server_address", null);

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(serverAddress)
                .build();

        final SwitchService switchService = restAdapter.create(SwitchService.class);
        for (Switch aSwitch : switchService.get()) {
            System.out.println("aSwitch = " + aSwitch);
            switchService.execute(aSwitch.getName(), new SwitchAction(switchOn));
        }
    }

    private void showToast(final int resId) {
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SwitchIntentService.this, resId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
