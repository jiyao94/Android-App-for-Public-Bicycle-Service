package com.example.publicbicycleservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jiyao on 2017/4/14.
 */

class SaveSharedPreference {
    private static final String PREF_USER_ID= "userid";
    private static final String PREF_USER_NAME= "username";
    private static final String PREF_USER_EMAIL= "useremail";
    private static final String PREF_USER_PASSWD= "userpasswd";
    private static final String PREF_USER_PHONE= "userphone";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    static void setUserName(Context ctx, String userId, String userName, String userEmail, String userPasswd, String userPhone)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userId);
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_USER_EMAIL, userEmail);
        editor.putString(PREF_USER_PASSWD, userPasswd);
        editor.putString(PREF_USER_PHONE, userPhone);
        editor.apply();
    }

    static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }

    static String getUserPasswd(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_PASSWD, "");
    }

    static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }
}
