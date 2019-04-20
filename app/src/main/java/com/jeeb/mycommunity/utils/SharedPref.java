package com.jeeb.mycommunity.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
        private static final String SHARED_APP_PREFERENCE_NAME = "SharedPref";
        Context cxt;
        private SharedPreferences pref;
        private SharedPreferences.Editor mEditor;

        public SharedPref(Context context) {
            this.pref = context.getSharedPreferences(SHARED_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        public static SharedPref getInstance(Context context) {
            return new SharedPref(context);
        }

        public void put(String key, String value) {
            pref.edit().putString(key, value).apply();

        }
        public String getString(String key)
        {
            return pref.getString(key,"");
        }
}
