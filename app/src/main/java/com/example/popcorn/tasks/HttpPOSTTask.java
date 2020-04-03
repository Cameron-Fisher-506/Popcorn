package com.example.popcorn.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.popcorn.utils.ConstantUtils;
import com.example.popcorn.utils.GeneralUtils;
import com.example.popcorn.utils.WSCallsUtils;

import org.json.JSONObject;

public class HttpPOSTTask extends AsyncTask<String, JSONObject, JSONObject> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... strings)
    {
        String url = strings[0];

        JSONObject toReturn = null;
        try
        {
            JSONObject body = new JSONObject(strings[1]);
            toReturn = WSCallsUtils.genericPOST(url, body);
        }catch(Exception e)
        {
            String message = "\n\nError Message: " + e.getMessage() +
                    "\nURL: " + url +
                    "\nMethod: HttpPOSTTask" +
                    "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
            Log.d(ConstantUtils.TAG, message);
        }

        return toReturn;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

    }
}
