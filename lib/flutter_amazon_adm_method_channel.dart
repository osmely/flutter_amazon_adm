import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'flutter_amazon_adm_platform_interface.dart';

/// An implementation of [FlutterAmazonAdmPlatform] that uses method channels.
class MethodChannelFlutterAmazonAdm extends FlutterAmazonAdmPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_amazon_adm');

  @override
  Future<String?> getAdmRegistrationId() async {
    final String? registrationId = await methodChannel.invokeMethod<String>('getAdmRegistrationId');
    return registrationId;
  }

  @override
  Future<bool> isSupported() async {
    final bool supported = await methodChannel.invokeMethod<bool>('isSupported') ?? false;
    return supported;
  }

  @override
  Future<void> initialize({
    required Function(Map<String, dynamic>) onMessage,
    required Function(Map<String, dynamic>) onMessageOpenedApp,
    required Function(String) onRegistrationId,
  }) async {
    methodChannel.setMethodCallHandler((MethodCall call) async {
      switch (call.method) {
        case 'onMessage':
          final Map<String, dynamic> message = Map<String, dynamic>.from(call.arguments);
          onMessage(message);
          break;
        case 'onMessageOpenedApp':
          final Map<String, dynamic> message = Map<String, dynamic>.from(call.arguments);
          onMessageOpenedApp(message);
          break;
        case 'onRegistrationId':
          final String registrationId = call.arguments as String;
          onRegistrationId(registrationId);
          break;
      }
    });

    await methodChannel.invokeMethod<void>('initialize');
  }
}
