package za.co.popcorn.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import za.co.popcorn.utils.ConstantUtils;
import za.co.popcorn.utils.GeneralUtils;
import za.co.popcorn.utils.SQLiteUtils;
import za.co.popcorn.utils.WSCallsUtils;
import za.co.popcorn.utils.WSCallsUtilsTaskCaller;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class AutoRefreshService extends Service
{
    private final int REQ_CODE_GET_ALL_MOVIES = 101;

    private Timer timer;
    private TimerTask timerTask;

    private TimerTask refreshTimerTask;


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


            this.refreshTimerTask = new TimerTask()
            {
                int pageNumber = 1;
                @Override
                public void run()
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
                                        JSONObject jsonObject = new JSONObject(response);
                                        if(jsonObject != null && jsonObject.has("data"))
                                        {
                                            JSONObject data = jsonObject.getJSONObject("data");
                                            if(data != null && data.has("movies"))
                                            {
                                                JSONArray movies = data.getJSONArray("movies");
                                                sqLiteUtils.cacheMovies(movies);
                                            }else
                                            {
                                                refreshTimerTask.cancel();
                                            }
                                        }

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
                                refreshTimerTask.cancel();

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
                    }, ConstantUtils.YTS_URL + "/list_movies.json?page=" + pageNumber, REQ_CODE_GET_ALL_MOVIES);

                    pageNumber++;
                }
            };

            this.timer.scheduleAtFixedRate(this.refreshTimerTask, 0, 60000);


        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
