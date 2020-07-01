package com.juganu.accesspoint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import java.lang.reflect.Method;
import java.util.List;

import static android.content.Context.*;

@NativePlugin(permissions = {
        Manifest.permission.CHANGE_WIFI_STATE
})

public class JuganuAccessPoint extends Plugin {

    private static final String TAG = "JuganuAccessPoint";

    @PluginMethod()
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }

    @PluginMethod()
    public void connectAP(PluginCall call) {
        String ssid = call.getString("ssid");
        String password = call.getString("password");

        Context context = getContext().getApplicationContext();

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && (!Settings.System.canWrite(context))) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connect_API29(ssid, password);
        } else {
            connect_BeforeAPI29(ssid, password);
        }
        JSObject ret = new JSObject();
        ret.put("value", true);
        call.resolve(ret);
    }

    private void connect_API29(String ssid, String password) {
        WifiNetworkSpecifier specifier = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            specifier = new WifiNetworkSpecifier.Builder()
                    .setSsid(ssid)
                    .setWpa2Passphrase(password)
                    .build();

            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED)
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_CONGESTED)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING)
                    .setNetworkSpecifier(specifier)
                    .build();
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
            cm.requestNetwork(networkRequest, new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    Log.d(TAG, "requestNetwork onAvailable()");
                }

                @Override
                public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                    Log.d(TAG, "requestNetwork onCapabilitiesChanged()");
                }

                @Override
                public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                    Log.d(TAG, "requestNetwork onLinkPropertiesChanged()");
                }

                @Override
                public void onLosing(Network network, int maxMsToLive) {
                    Log.d(TAG, "requestNetwork onLosing()");
                }

                @Override
                public void onLost(Network network) {
                    Log.d(TAG, "requestNetwork onLost()");
                }
            });
        }
    }

    private void connect_BeforeAPI29(String ssid, String password) {
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", password);
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

}
