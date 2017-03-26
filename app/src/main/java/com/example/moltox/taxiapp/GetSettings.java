package com.example.moltox.taxiapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by moltox on 02.06.2016.
 */
public class GetSettings {
    private static final String TAG = "vOut: GetSettings Class";
    SharedPreferences sPrefs;
    private boolean debugIsEnabled;

    public GetSettings(Context context) {

        sPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d(TAG, "Shared Prefs initialized");
        debugIsEnabled = sPrefs.getBoolean("pref_debug_settings_enabled", false);
        if (debugIsEnabled) {
            Log.v(TAG, "Debug -> enabled");
        }  else  {
            Log.v(TAG, "Debug -> disabled");
        }
    }

    public boolean getDebugIsEnabled() {
        return debugIsEnabled;
    }
}
