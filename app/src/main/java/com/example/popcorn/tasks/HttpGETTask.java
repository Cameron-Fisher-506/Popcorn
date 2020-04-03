package com.example.popcorn.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.popcorn.utils.ConstantUtils;
import com.example.popcorn.utils.GeneralUtils;
import com.example.popcorn.utils.WSCallsUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class HttpGETTask extends AsyncTask<Void, Void, JSONArray> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONArray doInBackground(Void... voids)
    {
        JSONArray toReturn = null;

        toReturn = WSCallsUtils.genericGET(ConstantUtils.GLOBAL_POPCORN_WS_URL);

        return toReturn;
    }

    @Override
    protected void onPostExecute(JSONArray jsonObjectMovies) {
        super.onPostExecute(jsonObjectMovies);

    }


}
