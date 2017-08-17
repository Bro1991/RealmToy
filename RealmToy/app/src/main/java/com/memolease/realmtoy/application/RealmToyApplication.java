package com.memolease.realmtoy.application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by bro on 2017-08-17.
 */

public class RealmToyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("hobby.realm")
                .build();
        Realm.setDefaultConfiguration(config);
    }

}
