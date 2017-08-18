package com.memolease.realmtoy.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.memolease.realmtoy.client.SearchClient;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by bro on 2017-08-17.
 */

public class RealmToyApplication extends Application {
    private static RealmToyApplication instance;
    private Bus mBus = BusProvider.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("hobby.realm")
                .build();
        Realm.setDefaultConfiguration(config);

        mBus.register(this);
        instance = this;
        clientInit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mBus.unregister(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static RealmToyApplication getInstance() {
        return instance;
    }

    private void clientInit() {
        SearchClient.getClient();
    }

}
