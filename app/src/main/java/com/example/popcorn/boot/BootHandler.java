package com.example.popcorn.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.JsonReader;
import android.util.Log;

import com.example.popcorn.services.AutoRefreshService;
import com.example.popcorn.services.UpdatePiAddressService;
import com.example.popcorn.utils.ConstantUtils;
import com.example.popcorn.utils.GeneralUtils;
import com.example.popcorn.utils.SharedPreferencesUtils;

import org.json.JSONObject;

public class BootHandler extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        startAutoRefreshService(context);
        startUpdatePiAddressService(context);
    }

    private void startAutoRefreshService(Context context)
    {
        try
        {
            JSONObject jsonObject = SharedPreferencesUtils.get(context, SharedPreferencesUtils.AUTO_REFRESH);
            if(jsonObject != null && jsonObject.has("isStart"))
            {
                Boolean isStart = jsonObject.getBoolean("isStart");
                if(isStart != null && isStart)
                {
                    Intent autoRefreshService = new Intent(context, AutoRefreshService.class);
                    context.startService(autoRefreshService);
                }
            }

        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: BootHandler - startAutoRefreshService"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

    }

    private void startUpdatePiAddressService(Context context)
    {
        try
        {
            JSONObject jsonObject = SharedPreferencesUtils.get(context, SharedPreferencesUtils.AUTO_UPDATE_PI_ADDRESS);
            if(jsonObject != null && jsonObject.has("isStart"))
            {
                Boolean isStart = jsonObject.getBoolean("isStart");
                if(isStart != null && isStart)
                {
                    Intent updatePiAddressService = new Intent(context, UpdatePiAddressService.class);
                    context.startService(updatePiAddressService);
                }
            }
        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: BootHandler - startUpdatePiAddressService"
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }

    }
}
