package com.kinesis.flutter_adm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.flutter.embedding.android.FlutterActivity;

public class NotificationActivity extends FlutterActivity {
    private static final String EXTRA_NOTIFICATION_MESSAGE = "notification_message";

    public static Intent createIntent(Context context, String message) {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRA_NOTIFICATION_MESSAGE, message);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get notification message
        String message = getIntent().getStringExtra(EXTRA_NOTIFICATION_MESSAGE);
        
        // Pass message to plugin
        if (message != null) {
            FlutterAmazonAdmPlugin.handleNotificationOpen(message);
        }
        
        // Finish this activity and return to main activity
        finish();
    }
}
