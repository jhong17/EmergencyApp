package hu.ait.emergencyapp;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


/**
 * Created by jessicahong on 6/28/17.
 */

public class MyLocationMonitor implements LocationListener{

    public interface MyLocationListener {
        void newLocationReceived(Location location);
    }

    private Context context;
    private LocationManager locMan;
    private MyLocationListener myLocationListener;



    public MyLocationMonitor(Context context, MyLocationListener myLocationListener) {
        this.context = context;
        this.myLocationListener = myLocationListener;
    }

    public void startLocationMonitoring() throws SecurityException {
        locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        //if using emulator, don't use this - WILL CRASH
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


    }

    public void stopLocationMonitoring(){
        locMan.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        myLocationListener.newLocationReceived(location);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

