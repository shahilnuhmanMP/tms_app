// lib/terminal_manager.dart

import 'dart:async';
import 'package:flutter/services.dart';

class TerminalManager {
  static const MethodChannel _channel =
      MethodChannel('com.example.my_flutter_plugin');

  static Future<void> rebootSystem() async {
    try {
      await _channel.invokeMethod('rebootSystem');
    } on PlatformException catch (e) {
      print("Failed to reboot system: '${e.message}'.");
    }
  }

  static Future<void> captureScreen(String filePath) async {
    try {
      await _channel.invokeMethod('captureScreen', {"filePath": filePath});
    } on PlatformException catch (e) {
      print("Failed to capture screen: '${e.message}'.");
    }
  }

  static Future<void> setVolume(int volumeLevel) async {
    try {
      await _channel.invokeMethod('setVolume', {"volumeLevel": volumeLevel});
    } on PlatformException catch (e) {
      print("Failed to set volume: '${e.message}'.");
    }
  }

  static Future<void> setBrightness(int brightnessValue) async {
    try {
      await _channel
          .invokeMethod('setBrightness', {"brightnessValue": brightnessValue});
    } on PlatformException catch (e) {
      print("Failed to set brightness: '${e.message}'.");
    }
  }

  static Future<String> getWifiMacAddress() async {
    try {
      return await _channel.invokeMethod('getWifiMacAddress');
    } on PlatformException catch (e) {
      print("Failed to get WiFi MAC address: '${e.message}'.");
      return "";
    }
  }

  static Future<bool> silentInstall(String apkPath) async {
    try {
      final bool result =
          await _channel.invokeMethod('silentInstall', {"apkPath": apkPath});
      return result;
    } on PlatformException catch (e) {
      print("Failed to install APK: '${e.message}'.");
      return false;
    }
  }
}
