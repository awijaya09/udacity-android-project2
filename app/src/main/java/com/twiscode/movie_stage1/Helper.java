package com.twiscode.movie_stage1;

import android.net.ParseException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andree on 8/6/17.
 */

public class Helper {

    //Helper method to convert release date to a better format
    public static String convertStringToDate(String inputDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(inputDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        formatter = new SimpleDateFormat("dd MMM yyyy");
        if (null != date){
            return formatter.format(date);
        }
        return "-";
    }
}
