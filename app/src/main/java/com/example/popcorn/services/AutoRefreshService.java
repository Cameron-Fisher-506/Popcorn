package com.example.popcorn.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.popcorn.utils.ConstantUtils;
import com.example.popcorn.utils.GeneralUtils;
import com.example.popcorn.utils.SQLiteUtils;
import com.example.popcorn.utils.WSCallsUtils;
import com.example.popcorn.utils.WSCallsUtilsTaskCaller;
import org.json.JSONArray;
import java.util.Timer;
import java.util.TimerTask;

public class AutoRefreshService extends Service
{
    private final int REQ_CODE_GET_ALL_MOVIES = 101;

    private Timer timer;
    private TimerTask timerTask;

    private SQLiteUtils sqLiteUtils;

    @Override
    public void onCreate()
    {
        this.sqLiteUtils = new SQLiteUtils(getApplicationContext());

        this.timerTask = new TimerTask() {
            @Override
            public void run()
            {
                refresh();
            }
        };

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(this.timerTask, 0, 86400000);

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
    }

    private void refresh()
    {
        try
        {
            WSCallsUtils.get(new WSCallsUtilsTaskCaller() {
                @Override
                public void taskCompleted(String response, int reqCode)
                {
                    if(response != null)
                    {
                        if(reqCode == REQ_CODE_GET_ALL_MOVIES)
                        {
                            try
                            {
                                JSONArray movies = new JSONArray(response);
                                sqLiteUtils.cacheMovies(movies);
                            }catch(Exception e)
                            {
                                Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                                        + "\nResponse: " + response
                                        + "\nReqCode: " + reqCode
                                        + "\nMethod: MainActivity - onCreate"
                                        + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                            }

                        }
                    }else
                    {
                        Log.e(ConstantUtils.TAG, "\nResponse: " + response
                                + "\nReqCode: " + reqCode
                                + "\nMethod: AutoRefreshService - refresh"
                                + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                    }
                }

                @Override
                public Activity getActivity() {
                    return null;
                }
            }, ConstantUtils.GLOBAL_POPCORN_URL + "/rest/popcorn/getAllMovies", REQ_CODE_GET_ALL_MOVIES);


        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
