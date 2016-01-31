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
    static final String KEY_STUDENT_ID = "studentId";
    static final String KEY_PROGRAMME = "programme";
    static final String KEY_FACULTY = "faculty";
    static final String KEY_LEVEL = "level";
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
    public void createLoginSession(String email, String key, String name, String studentId, String programme, String faculty, String level) {
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putString(KEY_USER_FIREBASE_KEY, key);
        mEditor.putString(KEY_USER_NAME, name);
        mEditor.putString(KEY_STUDENT_ID, studentId);
        mEditor.putString(KEY_PROGRAMME, programme);
        mEditor.putString(KEY_FACULTY, faculty);
        mEditor.putString(KEY_LEVEL, level);
        mEditor.commit();
    }

    /**
     * Get stored mSession data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_EMAIL, mPref.getString(KEY_EMAIL, null));
        return user;
    }

    public String getProgramme() {
        return mPref.getString(KEY_PROGRAMME, null);
    }

    public String getFaculty() {
        return mPref.getString(KEY_FACULTY, null);
    }

    public String getLevel() {
        return mPref.getString(KEY_LEVEL, null);
    }

    public String getUserEmail() {
        return mPref.getString(KEY_EMAIL, null);
    }

    public String getUserName() {
        return mPref.getString(KEY_USER_NAME, null);
    }

    public String getStudentId() {
        return mPref.getString(KEY_STUDENT_ID, null);
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
