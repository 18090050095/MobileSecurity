package com.coderjj.gps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        getLocation();
    }

    private void initView() {
        mTvLocation = findViewById(R.id.tv_location);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //经度
                double longitude = location.getLongitude();
                //纬度
                double latitude = location.getLatitude();
                mTvLocation.setText("longitude:"+longitude+"\nlatitude:"+latitude);
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
        });
    }
}
