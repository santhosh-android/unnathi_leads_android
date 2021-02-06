package com.leadapplication.app;

import android.content.Context;
import android.content.SharedPreferences;

import static com.leadapplication.app.Utils.Constants.IS_LOGGED_IN;
import static com.leadapplication.app.Utils.Constants.USER_ID;
import static com.leadapplication.app.Utils.Constants.USER_PREF_NAME;

public class UserSessionManagement {

    private static UserSessionManagement userSessionManagement;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private UserSessionManagement(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(USER_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }




    public static UserSessionManagement getUserSessionManagement(Context context) {
        if (userSessionManagement == null) {
            userSessionManagement = new UserSessionManagement(context);
        }
        return userSessionManagement;
    }

    public void saveUser(String userId) {
        editor.putString(USER_ID, userId);
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.apply();
    }
    public String getUserId() {
        return sharedPreferences.getString(USER_ID, "");
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
    public void logOut() {
        editor.clear();
        editor.apply();
    }


}
