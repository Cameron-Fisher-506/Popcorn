package za.co.popcorn.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.text.format.Formatter;
import androidx.annotation.Nullable;
import za.co.popcorn.utils.ConstantUtils;
import za.co.popcorn.utils.WSCallsUtils;
import za.co.popcorn.utils.WSCallsUtilsTaskCaller;
import java.util.Timer;
import java.util.TimerTask;

public class UpdatePiAddressService extends Service
{
    private final int REQ_CODE_PING = 101;

    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void onCreate()
    {
        this.timerTask = new TimerTask() {
            @Override
            public void run()
            {
                try
                {
                    if(ConstantUtils.GLOBAL_POPCORN_URL == null)
                    {
                        setGlobalPopcornUrl();
                    }else
                    {
                        resetGlobalPopcornUrl(ConstantUtils.GLOBAL_POPCORN_URL);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        };

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(this.timerTask, 0, 300000);


    }

    private void setGlobalPopcornUrl()
    {
        WifiManager wm = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiInfo connectionInfo = wm.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        String ipString = Formatter.formatIpAddress(ipAddress);

        String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);

        for (int i = 0; i < 255; i++)
        {
            String ip = prefix + i;

            String url = "http://"+ ip +":8080/rest/popcorn/ping/" + ip;

            //ping the server
            ping(url);
        }
    }

    private void ping(String url)
    {
        WSCallsUtils.get(new WSCallsUtilsTaskCaller() {
            @Override
            public void taskCompleted(String response, int reqCode)
            {
                if(response != null)
                {
                    if(reqCode == REQ_CODE_PING)
                    {
                        ConstantUtils.GLOBAL_POPCORN_URL = "http://" + response + ":8080";
                    }
                }
            }

            @Override
            public Activity getActivity() {
                return null;
            }
        }, url , REQ_CODE_PING);
    }

    private void resetGlobalPopcornUrl(String url)
    {
        WSCallsUtils.get(new WSCallsUtilsTaskCaller() {
            @Override
            public void taskCompleted(String response, int reqCode)
            {
                if(response == null)
                {
                    setGlobalPopcornUrl();
                }
            }

            @Override
            public Activity getActivity() {
                return null;
            }
        }, url , REQ_CODE_PING);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(this.timer != null)
        {
            this.timer.cancel();
            this.timer.purge();
            this.timer = null;
        }

        if(this.timerTask != null)
        {
            this.timerTask.cancel();
            this.timerTask = null;
        }
    }

}
