package com.kinesis.flutter_amazon_adm;

import android.content.Intent;
import android.util.Log;

import com.amazon.device.messaging.ADMMessageReceiver;

public class AdmMessageReceiver extends ADMMessageReceiver {
    private static final String TAG = "AdmMessageReceiver";

    public AdmMessageReceiver() {
        super(AdmMessageHandler.class);
        registerJobServiceClass(AdmMessageHandler.class, 434534);
    }
}
