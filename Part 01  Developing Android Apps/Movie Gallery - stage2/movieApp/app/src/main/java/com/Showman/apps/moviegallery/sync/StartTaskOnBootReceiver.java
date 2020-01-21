package com.Showman.apps.moviegallery.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class StartTaskOnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "Services on boot complete started", Toast.LENGTH_LONG).show();
            MoviesSyncUtils.scheduleFirebaseJobDispatcher(context);
        }
    }
}
