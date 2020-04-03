package com.example.popcorn.utils;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;



public class WSCallsUtils
{
    public static JSONObject genericPOST(String url, JSONObject body)
    {
        JSONObject toReturn = new JSONObject();

        HttpURLConnection connection = null;

        try
        {
            URL target = new URL(url);
            connection = (HttpURLConnection)target.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(body.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            connection.connect();
            if(connection.getResponseCode() >= 400)
            {
                String message = "\n\nError Code: " + connection.getResponseCode() +
                        "\nMessage: " + connection.getResponseMessage() +
                        "\nData: " + body.toString() +
                        "\nRequest Method: " + connection.getRequestMethod() +
                        "\nURL: " + connection.getURL() +
                        "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
                Log.d(ConstantUtils.TAG, message);
            }else
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String output = "";

                while((output = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(output);
                }

                toReturn = new JSONObject(stringBuilder.toString());
            }

        }catch(Exception e)
        {
            String message = "\n\nError Message: " + e.getMessage() +
                    "\nURL: " + url +
                    "\nBody: " + body.toString() +
                    "\nMethod: genericPOST" +
                    "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
            Log.d(ConstantUtils.TAG, message);
        }finally {
            if(connection != null)
            {
                connection.disconnect();
                connection = null;
            }
        }

        return toReturn;
    }


    public static JSONArray genericGET(String url)
    {
        JSONArray toReturn = new JSONArray();

        HttpURLConnection connection = null;


        try
        {
            URL target = new URL(url);
            connection = (HttpURLConnection) target.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoInput(true);

            connection.connect();
            if(connection.getResponseCode() >= 400)
            {
                String message = "\n\nError Code: " + connection.getResponseCode() +
                        "\nMessage: " + connection.getResponseMessage() +
                        "\nRequest Method: " + connection.getRequestMethod() +
                        "\nURL: " + connection.getURL() +
                        "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
                Log.d(ConstantUtils.TAG, message);
            }else
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String output = "";

                while((output = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(output);
                }
                Log.d(ConstantUtils.TAG, stringBuilder.toString());
                toReturn = new JSONArray(stringBuilder.toString());
            }
        }catch(Exception e)
        {
            String message = "\n\nError Message: " + e.getMessage() +
                    "\nURL: " + url +
                    "\nMethod: genericGET" +
                    "\nCreatedTime: " + GeneralUtils.getCurrentDateTime();
            Log.d(ConstantUtils.TAG, message);
        }finally {
            if(connection != null)
            {
                connection.disconnect();
                connection = null;
            }
        }

        return toReturn;
    }


}
