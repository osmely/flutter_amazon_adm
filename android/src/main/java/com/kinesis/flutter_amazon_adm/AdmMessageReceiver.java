package com.kinesis.flutter_amazon_adm;

import android.content.Intent;
import android.util.Log;

import com.amazon.device.messaging.ADMMessageReceiver;

public class AdmMessageReceiver extends ADMMessageReceiver {
    private static final String TAG = "AdmMessageReceiver";

    public AdmMessageReceiver() {
        super(AdmMessageHandler.class);

        Log.d("AdmMessageReceiver", ":::::::::::::::");

        registerJobServiceClass(AdmMessageHandlerJob.class, 434534);
    }

}


