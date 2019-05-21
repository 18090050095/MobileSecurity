package com.coderjj.phonedefend.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //以最优的方式获取定位
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);//允许花费
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//制定获取经纬度的精确度
        String bestProvider = lm.getBestProvider(criteria, true);
        MyLocationListener myLocationListener = new MyLocationListener();
        lm.requestLocationUpdates(bestProvider,0,0,myLocationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            //经度
            double longitude = location.getLongitude();
            //纬度
            double latitude = location.getLatitude();

            //发送短信

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
}
