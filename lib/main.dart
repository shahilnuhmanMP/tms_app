// Dart code in your Flutter app
import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'package:tms_app/terminal_manager.dart';

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('Terminal Manager')),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              ElevatedButton(
                onPressed: () {
                  TerminalManager.rebootSystem();
                },
                child: Text('Restart System'),
              ),
              ElevatedButton(
                onPressed: () async {
                  String filePath = await _captureScreen();
                  print('Screen captured at: $filePath');
                },
                child: Text('Capture Screen'),
              ),
              ElevatedButton(
                onPressed: () {
                  TerminalManager.setVolume(10); // Set volume to 10
                },
                child: Text('Set Volume'),
              ),
              ElevatedButton(
                onPressed: () {
                  TerminalManager.setBrightness(200); // Set brightness to 200
                },
                child: Text('Set Brightness'),
              ),
              ElevatedButton(
                onPressed: () async {
                  String macAddress = await TerminalManager.getWifiMacAddress();
                  print('WiFi MAC Address: $macAddress');
                },
                child: Text('Get WiFi MAC Address'),
              ),
              ElevatedButton(
                onPressed: () async {
                  bool success = await _silentInstallApk();
                  if (success) {
                    print('APK installed successfully');
                  } else {
                    print('Failed to install APK');
                  }
                },
                child: Text('Silent Install APK'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<String> _captureScreen() async {
    String fileName = 'screenshot.png';
    String directory = (await getExternalStorageDirectory())!.path;
    String filePath = '$directory/$fileName';

    await TerminalManager.captureScreen(filePath);
    return filePath;
  }

  Future<bool> _silentInstallApk() async {
    String apkPath = '/sdcard/your_app.apk'; // Replace with your APK path
    return await TerminalManager.silentInstall(apkPath);
  }
}

void main() {
  runApp(MyApp());
}
