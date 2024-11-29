import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'flutter_amazon_adm_method_channel.dart';

abstract class FlutterAmazonAdmPlatform extends PlatformInterface {
  /// Constructs a FlutterAmazonAdmPlatform.
  FlutterAmazonAdmPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterAmazonAdmPlatform _instance = MethodChannelFlutterAmazonAdm();

  /// The default instance of [FlutterAmazonAdmPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterAmazonAdm].
  static FlutterAmazonAdmPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterAmazonAdmPlatform] when
  /// they register themselves.
  static set instance(FlutterAmazonAdmPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getAdmRegistrationId() {
    throw UnimplementedError('getAdmRegistrationId() has not been implemented.');
  }

  Future<bool> isSupported() {
    throw UnimplementedError('isSupported() has not been implemented.');
  }

  Future<void> initialize({
    required Function(Map<String, dynamic>) onMessage,
    required Function(Map<String, dynamic>) onMessageOpenedApp,
    required Function(String) onRegistrationId,
  }) {
    throw UnimplementedError('initialize() has not been implemented.');
  }
}
