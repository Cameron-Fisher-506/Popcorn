package com.example.popcorn.utils;

import android.content.Context;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GeneralUtils {

    public static String getCurrentDateTime()
    {
        String toReturn = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        now = calendar.getTime();

        toReturn = simpleDateFormat.format(now);

        return toReturn;

    }

    public static void makeToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG);
    }
}
