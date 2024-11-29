import 'flutter_amazon_adm_platform_interface.dart';

class FlutterAmazonAdm {
  Future<String?> getAdmRegistrationId() {
    return FlutterAmazonAdmPlatform.instance.getAdmRegistrationId();
  }

  Future<bool> isSupported() {
    return FlutterAmazonAdmPlatform.instance.isSupported();
  }

  Future<void> initialize({
    required Function(Map<String, dynamic>) onMessage,
    required Function(Map<String, dynamic>) onMessageOpenedApp,
    required Function(String) onRegistrationId,
  }) {
    return FlutterAmazonAdmPlatform.instance.initialize(
      onMessage: onMessage,
      onMessageOpenedApp: onMessageOpenedApp,
      onRegistrationId: onRegistrationId,
    );
  }

  Future<void> startUnregister() {
    return FlutterAmazonAdmPlatform.instance.startUnregister();
  }
}
