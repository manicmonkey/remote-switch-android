package org.manicmonkey.lightwidget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.manicmonkey.lightwidget.backend.SwitchAction;
import org.manicmonkey.lightwidget.backend.SwitchService;
import org.manicmonkey.lightwidget.backend.SwitchServiceFactory;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class SwitchIntentService extends IntentService {

    public static final String ACTION_SWITCH_ON = "org.manicmonkey.lightwidget.action.ON";
    public static final String ACTION_SWITCH_OFF = "org.manicmonkey.lightwidget.action.OFF";

    public static final String EXTRA_SWITCH = "org.manicmonkey.lightwidget.extra.SWITCH";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionSwitchOn(Context context, String switchName) {
        Intent intent = new Intent(context, SwitchIntentService.class);
        intent.setAction(ACTION_SWITCH_ON);
        intent.putExtra(EXTRA_SWITCH, switchName);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionSwitchOff(Context context, String switchName) {
        Intent intent = new Intent(context, SwitchIntentService.class);
        intent.setAction(ACTION_SWITCH_OFF);
        intent.putExtra(EXTRA_SWITCH, switchName);
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
                handleActionSwitchOn(intent.getStringExtra(EXTRA_SWITCH));
            } else if (ACTION_SWITCH_OFF.equals(action)) {
                handleActionSwitchOff(intent.getStringExtra(EXTRA_SWITCH));
            }
        }
    }

    /**
     * Handle action SwitchOn in the provided background thread with the provided parameters.
     *
     */
    private void handleActionSwitchOn(String name) {
        Log.d(getClass().getSimpleName(), "Turn on switch[" + name + "]");
        performSwitchRequest(name, true);
    }

    /**
     * Handle action SwitchOff in the provided background thread with the provided parameters.
     */
    private void handleActionSwitchOff(String name) {
        Log.d(getClass().getSimpleName(), "Turn off switch[" + name + "]");
        performSwitchRequest(name, false);
    }

    private void performSwitchRequest(String switchName, boolean switchOn) {
        final SwitchService switchService = SwitchServiceFactory.getSwitchService(this);

        if (switchService == null) {
            showToast(R.string.no_server_address);
            return;
        }

        switchService.execute(switchName, new SwitchAction(switchOn));

        if (switchOn) {
            showToast(R.string.switched_on);
        } else {
            showToast(R.string.switched_off);
        }
    }

    // Need to post toast on main thread
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
