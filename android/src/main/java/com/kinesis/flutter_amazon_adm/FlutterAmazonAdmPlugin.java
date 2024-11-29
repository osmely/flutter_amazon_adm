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
    static Context context;
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
        FlutterAmazonAdmPlugin.context = flutterPluginBinding.getApplicationContext();
        adm = new ADM(FlutterAmazonAdmPlugin.context);
        AdmMessageHandler.setChannel(channel);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "initialize":
                initialize(result);
                break;
            case "isSupported":
                result.success(isSupported());
                break;
            case "getAdmRegistrationId":
                result.success(getAdmRegistrationId());
                break;
            case "startUnregister":
                startUnregister(result);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void initialize(Result result) {
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
    }

    private boolean isSupported() {
        try {
            return adm.isSupported();
        } catch (Exception e) {
            return false;
        }
    }

    private String getAdmRegistrationId() {
        try {
            return adm.getRegistrationId();
        } catch (Exception e) {
            return null;
        }
    }

    private void startUnregister(Result result) {
        try {
            adm.startUnregister();
            result.success(null);
        } catch (Exception e) {
            result.error("ERROR", "Error al desregistrar ADM", e.getMessage());
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
