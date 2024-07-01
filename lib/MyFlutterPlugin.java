package com.example.my_flutter_plugin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class MyFlutterPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler {

    private static final String TAG = "MyFlutterPlugin";
    private Context context;
    private MethodChannel methodChannel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext();
        methodChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "com.example.my_flutter_plugin");
        methodChannel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        methodChannel.setMethodCallHandler(null);
        context = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "rebootSystem":
                rebootSystem();
                result.success(null);
                break;
            case "captureScreen":
                String filePath = call.argument("filePath");
                captureScreen(filePath);
                result.success(null);
                break;
            case "setVolume":
                int volumeLevel = call.argument("volumeLevel");
                setVolume(volumeLevel);
                result.success(null);
                break;
            case "setBrightness":
                int brightnessValue = call.argument("brightnessValue");
                setBrightness(brightnessValue);
                result.success(null);
                break;
            case "getWifiMacAddress":
                String macAddress = getWifiMacAddress();
                result.success(macAddress);
                break;
            case "silentInstall":
                String apkPath = call.argument("apkPath");
                boolean installSuccess = silentInstall(apkPath);
                result.success(installSuccess);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void rebootSystem() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot"});
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Error rebooting system", e);
        }
    }

    private void captureScreen(String filePath) {
        try {
            Process process = Runtime.getRuntime().exec("screencap " + filePath);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Error capturing screen", e);
        }
    }

    private void setVolume(int volumeLevel) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, 0);
        }
    }

    private void setBrightness(int brightnessValue) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightnessValue);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error setting brightness", e);
        }
    }

    private String getWifiMacAddress() {
        String macAddress = "";
        try {
            FileInputStream fin = new FileInputStream(new File("/sys/class/net/wlan0/address"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            macAddress = reader.readLine();
            fin.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting WiFi MAC address", e);
        }
        return macAddress;
    }

    private boolean silentInstall(String apkPath) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "pm install -r " + apkPath});
            process.waitFor();
            return true; // Installation successful
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Error installing APK", e);
            return false; // Installation failed
        }
    }

    // Register plugin with the Flutter engine
    public static void registerWith(PluginRegistry.Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "com.example.my_flutter_plugin");
        channel.setMethodCallHandler(new MyFlutterPlugin());
    }
}
