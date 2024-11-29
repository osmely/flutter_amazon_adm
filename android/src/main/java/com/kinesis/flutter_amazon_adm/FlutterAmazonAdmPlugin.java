package com.kinesis.flutter_amazon_adm;

import android.content.Context;
import androidx.annotation.NonNull;
import com.amazon.device.messaging.ADM;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class FlutterAmazonAdmPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    private static MethodChannel methodChannel;
    private static String pendingNotificationMessage;

    private MethodChannel channel;
    private Context context;
    private ADM adm;

    public static void handleNotificationOpen(String message) {
        pendingNotificationMessage = message;
        if (methodChannel != null) {
            try {
                JSONObject jsonMessage = new JSONObject(message);
                methodChannel.invokeMethod("onMessageOpenedApp", jsonToMap(jsonMessage));
                pendingNotificationMessage = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_amazon_adm");
        methodChannel = channel;
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
        adm = new ADM(context);
        AdmMessageHandler.setChannel(channel);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "initialize":
                try {
                    if (!adm.isSupported()) {
                        result.error("ADM_NOT_SUPPORTED", "Amazon Device Messaging is not supported on this device", null);
                        return;
                    }

                    // Check for pending notification message
                    if (pendingNotificationMessage != null) {
                        JSONObject jsonMessage = new JSONObject(pendingNotificationMessage);
                        channel.invokeMethod("onMessageOpenedApp", jsonToMap(jsonMessage));
                        pendingNotificationMessage = null;
                    }

                    result.success(null);
                } catch (Exception e) {
                    result.error("INIT_ERROR", "Error initializing ADM: " + e.getMessage(), null);
                }
                break;

            case "isSupported":
                try {
                    result.success(adm.isSupported());
                } catch (Exception e) {
                    result.error("SUPPORT_CHECK_ERROR", "Error checking ADM support: " + e.getMessage(), null);
                }
                break;

            case "getAdmRegistrationId":
                try {
                    String registrationId = adm.getRegistrationId();
                    result.success(registrationId);
                } catch (Exception e) {
                    result.error("REG_ID_ERROR", "Error getting registration ID: " + e.getMessage(), null);
                }
                break;

            default:
                result.notImplemented();
                break;
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        methodChannel = null;
        AdmMessageHandler.setChannel(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        // Not needed as we use NotificationActivity
    }

    @Override
    public void onDetachedFromActivity() {
        // Not needed
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        // Not needed
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        // Not needed
    }

    private static Map<String, Object> jsonToMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            map.put(key, value);
        }
        return map;
    }
}
