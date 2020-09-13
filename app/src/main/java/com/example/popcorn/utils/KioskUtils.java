package com.example.popcorn.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

public class KioskUtils
{
    public static String getIPAddress(Context context)
    {
        String toReturn = null;

        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);

        if(wifiManager != null)
        {
            toReturn = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        }

        return toReturn;
    }

}
