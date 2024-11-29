# flutter_amazon_adm

A Flutter plugin for Amazon Device Messaging (ADM) push notifications. This plugin enables Flutter applications to receive push notifications through Amazon's ADM service on Amazon devices.

## Getting Started

### Prerequisites

1. Register your app with Amazon and obtain the necessary credentials
2. Add the ADM SDK to your project
3. Configure your Amazon developer account

### Installation

Add this to your package's `pubspec.yaml` file:

```yaml
dependencies:
  flutter_amazon_adm: ^0.0.1
```

### Android Setup

1. Add the following to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.amazon.device.messaging:adm:1.2.0'
}
```

2. Make sure your `AndroidManifest.xml` includes the necessary permissions and configurations (already included in the plugin's manifest).

### Usage

```dart
import 'package:flutter_amazon_adm/flutter_amazon_adm.dart';

// Initialize the plugin
final admPlugin = FlutterAmazonAdm();

// Initialize ADM and set up message handlers
await admPlugin.initialize(
  onMessage: (Map<String, dynamic> message) {
    print('Got a message whilst in the foreground!');
    print('Message data: $message');
  },
  onMessageOpenedApp: (Map<String, dynamic> message) {
    print('Message opened app from background state!');
    print('Message data: $message');
  },
);

// Get the ADM registration ID
String? registrationId = await admPlugin.getAdmRegistrationId();
print('ADM Registration ID: $registrationId');
```

## Features

- Initialize ADM
- Get ADM registration ID
- Handle foreground messages
- Handle background messages
- Handle messages that open the app

## License

This project is licensed under the MIT License - see the LICENSE file for details
