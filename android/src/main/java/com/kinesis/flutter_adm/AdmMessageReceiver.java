package com.kinesis.flutter_adm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AdmMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "AdmMessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Reenviar el intent al AdmMessageHandler
        Intent serviceIntent = new Intent(intent);
        serviceIntent.setClass(context, AdmMessageHandler.class);

        // Iniciar el servicio con el intent
        context.startService(serviceIntent);
        
        Log.d(TAG, "ADM message received and forwarded to handler");
    }
}
