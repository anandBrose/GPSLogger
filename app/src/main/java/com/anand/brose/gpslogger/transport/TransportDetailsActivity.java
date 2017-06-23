package com.anand.brose.gpslogger.transport;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anand.brose.gpslogger.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;

public class TransportDetailsActivity extends AppCompatActivity implements View.OnClickListener, ITransportView {

    private ILocationSenderPresenter locationPresenter;

    private FusedLocationProviderClient mFusedLocationClient;

    private String username;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ((Button)findViewById(R.id.sendLocation)).setOnClickListener(this);
        username = getIntent().getStringExtra("username");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationPresenter = new LocationSenderPresentImpl(this, new LocationDetailsReader(this) );
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                locationPresenter.sendLocation(username,locationResult.getLocations().get(0));
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                mProgressDialog.dismiss();
            };
        };

    }

    @Override
    public void onClick(View v) {
        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                boolean gpsStatus = ((LocationManager)TransportDetailsActivity.this.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(gpsStatus){
                    mProgressDialog = ProgressDialog.show(this, "Loading", "Wait while fetching your location...");
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                }
                else {
                    Toast.makeText(TransportDetailsActivity.this, "Please enable location settings",Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 199;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission. WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission. WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void showFileStoredMessage() {
        Toast.makeText(this,"xml file exported to sdcard/locationdetails/", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNoStoragePermissionMessage() {
        Toast.makeText(this,"Allow storage permission to export the xml file", Toast.LENGTH_LONG).show();
    }

    @Override
    public void openXmlFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try{
            intent.setDataAndType(Uri.fromFile(file), "text/html");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        boolean gpsStatus = ((LocationManager)TransportDetailsActivity.this.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
                        if(gpsStatus){
                            mProgressDialog = ProgressDialog.show(this, "Loading", "Wait while fetching your location...");
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                        }
                        else {
                            Toast.makeText(TransportDetailsActivity.this, "Please enable location settings",Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission. WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        locationPresenter.exportToXML();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    public File getFileDirectory() {
        return Environment.getExternalStorageDirectory();
    }
}
