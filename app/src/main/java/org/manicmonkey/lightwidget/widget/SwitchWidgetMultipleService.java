package org.manicmonkey.lightwidget.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * @author James - 2017-01-22.
 */
public class SwitchWidgetMultipleService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(this.getClass().getSimpleName(), "OnGetViewFactory");
        return new SwitchWidgetMultipleRowsFactory(this.getApplicationContext(), intent);
    }
}
