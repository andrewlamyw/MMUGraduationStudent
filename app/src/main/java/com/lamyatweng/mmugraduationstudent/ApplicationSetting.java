package com.lamyatweng.mmugraduationstudent;

import android.app.Application;

import com.firebase.client.Firebase;

public class ApplicationSetting extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        // Enabling disk persistence allows app to keep all of its state even after an app restart.
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
