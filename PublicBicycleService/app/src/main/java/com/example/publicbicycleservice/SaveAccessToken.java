package com.example.publicbicycleservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jiyao on 2017/4/14.
 */

class SaveAccessToken {
    private static String PREF_ACCESS_TOKEN;

    static void setAccessToken(Context ctx, String accessToken)
    {
        PREF_ACCESS_TOKEN = accessToken;
    }

    static String getAccessToken(Context ctx)
    {
        return PREF_ACCESS_TOKEN;
    }

    static void clearAccessToken(Context ctx)
    {
        PREF_ACCESS_TOKEN = "";
    }
}
