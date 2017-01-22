package org.manicmonkey.lightwidget.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author James Baxter 2015-08-15.
 */
public class SwitchWidgetSingleConfiguration {

    public static final String PREF_NAME = "NAME";
    public static final String PREF_SWITCH_ON = "SWITCH_ON";

    private static final String PREFS_NAME = "org.manicmonkey.lightwidget.widget.SwitchWidgetSingle";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    private final Context context;
    private final int appWidgetId;

    public SwitchWidgetSingleConfiguration(Context context, int appWidgetId) {
        this.context = context;
        this.appWidgetId = appWidgetId;
    }

    // Write the prefix to the SharedPreferences object for this widget
    public void save(String name, boolean value) {
        SharedPreferences.Editor prefs = getSharedPreferences().edit();
        prefs.putBoolean(getKey(name), value);
        prefs.apply();
    }

    // Write the prefix to the SharedPreferences object for this widget
    public void save(String name, String value) {
        SharedPreferences.Editor prefs = getSharedPreferences().edit();
        prefs.putString(getKey(name), value);
        Log.d(getClass().getSimpleName(), "Saved setting [" + name + "] = [" + value + "]");
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public boolean getBoolean(String name) {
        SharedPreferences prefs = getSharedPreferences();
        boolean value = prefs.getBoolean(getKey(name), false);
        Log.d(getClass().getSimpleName(), "Got setting [" + name + "] = [" + value + "]");
        return value;
    }

    public String getString(String name) {
        SharedPreferences prefs = getSharedPreferences();
        String value = prefs.getString(getKey(name), null);
        Log.d(getClass().getSimpleName(), "Got setting [" + name + "] = [" + value + "]");
        return value;
    }

    public void delete(String name) {
        SharedPreferences.Editor prefs = getSharedPreferences().edit();
        prefs.remove(getKey(name));
        prefs.apply();
    }

    @NonNull
    private String getKey(String name) {
        return PREF_PREFIX_KEY + appWidgetId + "_" + name;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFS_NAME, 0);
    }
}
