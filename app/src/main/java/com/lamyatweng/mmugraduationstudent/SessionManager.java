package com.lamyatweng.mmugraduationstudent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    static final String PREF_NAME = "MMUGradPref";
    static final String IS_LOGIN = "IsLoggedIn";
    static final String KEY_EMAIL = "email";
    static final String KEY_USER_FIREBASE_KEY = "userFirebaseKey";
    static final String KEY_USER_NAME = "studentName";
    static final String KEY_ROBE_SIZE = "robeSize";
    static final String KEY_GRATITUDE_MESSAGE = "gratitudeMessage";
    static final String KEY_NUM_OF_GUEST = "numberOfGuest";
    static final String KEY_CONVO_ATTEND = "convocationAttendance";
    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;
    Context mContext;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        mContext = context;
        mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String email, String key, String name) {
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putString(KEY_USER_FIREBASE_KEY, key);
        mEditor.putString(KEY_USER_NAME, name);
        mEditor.commit();
    }

    public void setAttendConvo(Boolean convoAttend, String robeSize, String gratitudeMessage, int numberOfGuest) {
        mEditor.putBoolean(KEY_CONVO_ATTEND, convoAttend);
        mEditor.putString(KEY_ROBE_SIZE, robeSize);
        mEditor.putString(KEY_GRATITUDE_MESSAGE, gratitudeMessage);
        mEditor.putInt(KEY_NUM_OF_GUEST, numberOfGuest);
    }

    public void setConvocationAttendance(Boolean convoAttend) {
        mEditor.putBoolean(KEY_CONVO_ATTEND, convoAttend);
    }

    public void setRobesize(String robeSize) {
        mEditor.putString(KEY_ROBE_SIZE, robeSize);
    }

    public void setGratitudeMessage(String gratitudeMessage) {
        mEditor.putString(KEY_GRATITUDE_MESSAGE, gratitudeMessage);
    }

    public void setNumberOfGuest(int numberOfGuest) {
        mEditor.putInt(KEY_NUM_OF_GUEST, numberOfGuest);
    }

    /**
     * Get stored mSession data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_EMAIL, mPref.getString(KEY_EMAIL, null));
        return user;
    }

    public String getUserEmail() {
        return mPref.getString(KEY_EMAIL, null);
    }

    public String getUserName() {
        return mPref.getString(KEY_USER_NAME, null);
    }

    public String getKeyUserFirebaseKey() {
        return mPref.getString(KEY_USER_FIREBASE_KEY, null);
    }

    /**
     * Check user login status
     */
    public boolean isLoggedIn() {
        return mPref.getBoolean(IS_LOGIN, false);
    }

    /**
     * Redirect user to login page user is not logged in
     */
    public void checkLogin() {
        if (!isLoggedIn()) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            // Close all activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Start new Activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    /**
     * Clear mSession detail
     */
    public void logoutUser() {
        // Clearing all data from SharedPreferences
        mEditor.clear().commit();
        // Redirect user to login sMainActivity
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


}
