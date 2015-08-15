package org.manicmonkey.lightwidget.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * @author James Baxter 2015-08-15.
 */
public class SwitchServiceFactory {

    /**
     * Build and return SwitchService - null if server address not set
     */
    public static SwitchService getSwitchService(Context context) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String serverAddress = sharedPreferences.getString("server_address", null);

        if (serverAddress == null)
            return null;

        final RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(serverAddress)
                .setRequestInterceptor(requestInterceptor)
                .build();

        return restAdapter.create(SwitchService.class);
    }
}
