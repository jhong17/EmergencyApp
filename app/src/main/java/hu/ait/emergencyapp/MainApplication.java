package hu.ait.emergencyapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Sooyoung Kim on 6/27/2017.
 */

public class MainApplication extends Application {

    private Realm realmCityList;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    public void openRealm() {

        RealmConfiguration config = new RealmConfiguration.Builder().
                deleteRealmIfMigrationNeeded().
                build();
        realmCityList = Realm.getInstance(config);
    }

    public void closeRealm() {

        realmCityList.close();
    }

    public Realm getRealmTodo() {

        return realmCityList;
    }


}
