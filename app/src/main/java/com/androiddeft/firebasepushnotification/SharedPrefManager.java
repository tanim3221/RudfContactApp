package com.androiddeft.firebasepushnotification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Belal on 9/5/2017.
 */

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "firebasepushnotificationsharedpref";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_DEPARTMENT = "keydepartment";
    private static final String KEY_SESSION = "keysession";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_POSITION = "keyposition";
    private static final String KEY_ID = "keyid";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_DEPARTMENT, user.getDepartment());
        editor.putString(KEY_SESSION, user.getSession());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_POSITION, user.getRudf_position());

        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_DEPARTMENT, null),
                sharedPreferences.getString(KEY_SESSION, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_POSITION, null)

        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
