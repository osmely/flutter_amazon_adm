package com.kinesis.flutter_amazon_adm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.amazon.device.messaging.ADMMessageHandlerBase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class AdmMessageHandler extends ADMMessageHandlerBase {
    private static final String TAG = "AdmMessageHandler";
    private static final String NOTIFICATION_CHANNEL_ID = "adm_notifications";
    private static final int NOTIFICATION_ID = 1;
    private static MethodChannel channel;

    public AdmMessageHandler() {
        super("AdmMessageHandler");
    }

    public static void setChannel(MethodChannel methodChannel) {
        channel = methodChannel;
    }

    @Override
    protected void onRegistered(final String registrationId) {
        Log.d(TAG, "Device registered: " + registrationId);
        if (channel != null) {
            channel.invokeMethod("onRegistrationId", registrationId);
        }
    }

    @Override
    protected void onUnregistered(final String registrationId) {
        Log.d(TAG, "Device unregistered");
        if (channel != null) {
            channel.invokeMethod("onUnregistered", null);
        }
    }

    @Override
    protected void onRegistrationError(final String string)
    {
        Log.e(TAG, "PluginADMMessageHandler:onRegistrationError " + string);
    }

    @Override
    protected void onMessage(final Intent intent) {
        Log.d(TAG, "Message received");
        
        if (intent.getExtras() != null) {
            Map<String, Object> message = new HashMap<>();
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                if (value != null) {
                    message.put(key, value);
                }
            }
            
            // If app is in foreground, send message directly
            if (isAppInForeground()) {
                if (channel != null) {
                    channel.invokeMethod("onMessage", message);
                }
            } else {
                // If app is in background, show notification
                showNotification(FlutterAmazonAdmPlugin.context, message);
            }
        }
    }

    private boolean isAppInForeground() {
        Context context = FlutterAmazonAdmPlugin.context;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) return false;

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND 
                && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    private void showNotification(Context context, Map<String, Object> message) {
        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "ADM Notifications",
                NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Create intent for NotificationActivity
        Intent intent = NotificationActivity.createIntent(
            context,
            new JSONObject(message).toString()
        );

        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(message.containsKey("title") ? message.get("title").toString() : "New Message")
            .setContentText(message.containsKey("message") ? message.get("message").toString() : "You have a new message")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
