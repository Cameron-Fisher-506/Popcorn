package com.example.popcorn.menu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.popcorn.R;
import com.example.popcorn.utils.ConstantUtils;
import com.example.popcorn.utils.GeneralUtils;
import com.example.popcorn.utils.SQLiteUtils;
import com.example.popcorn.utils.WSCallsUtils;
import com.example.popcorn.utils.WSCallsUtilsTaskCaller;

import org.json.JSONArray;

public class RefreshFrag extends Fragment
{
    private final int REQ_CODE_GET_ALL_MOVIES = 101;

    private SQLiteUtils sqLiteUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_refresh, container, false);

        this.sqLiteUtils = new SQLiteUtils(getActivity());

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

        return view;
    }
}
