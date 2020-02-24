package com.example.eventapp.Utils;

import android.util.Log;

public class StringFormat {

    public static final String TAG = "StringFormat";


    public static String removebrakets(String str){

        str = str.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        Log.d(TAG, "Formatted String: "+str);

        return str;
    }

    public static String removeQoutes(String str){

        str =  str.replaceAll("^\"|\"$", "");
        Log.d(TAG, "Formatted String: "+str);

        return str;
    }

    public static String removeSlash(String str){

        str =  str.replace("\\", "");
        Log.d(TAG, "Formatted String: "+str);

        return str;
    }


}
