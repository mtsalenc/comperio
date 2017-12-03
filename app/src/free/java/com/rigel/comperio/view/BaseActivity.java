package com.rigel.comperio.view;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rigel.comperio.ComperioApplication;
import com.rigel.comperio.LoggingManager;
import com.rigel.comperio.NavigationManager;
import com.rigel.comperio.PersistenceManager;
import com.rigel.comperio.R;
import com.rigel.comperio.model.UserData;

public abstract class BaseActivity extends AppCompatActivity {
    
    private static final String LOG_TAG = BaseColumns.class.getSimpleName();
    
    private static final int REQUEST_CODE_LOCATION = 1;

    private NavigationManager NavigationManager;
    private PersistenceManager persistanceManagerImpl;
    private LoggingManager logger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        checkLocationPermissions();
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission( this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {
                            android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    REQUEST_CODE_LOCATION);
        }else{
            initLocationServices();
        }
    }

    protected abstract void initDataBinding();

    private void initLocationServices() {

        if (ContextCompat.checkSelfPermission( this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED ) {

            return;
        }

        if(alreadySavedLocation()){
            return;
        }

        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Float[] loc = new Float[2];
                        if (location == null) {
                            loc[0] = 0f;
                            loc[1] = 0f;
                        }else{
                            loc[0] = (float) location.getLongitude();
                            loc[1] = (float) location.getLatitude();
                        }

                        UserData userData = getPersistanceManager().loadUserData();
                        userData.userLoc = loc;
                        persistanceManagerImpl.saveUserData(userData);
                    }
                });
    }

    private boolean alreadySavedLocation() {
        UserData userData = getPersistanceManager().loadUserData();
        return userData.userLoc[0]!=null &&
                userData.userLoc[1]!=null &&
                userData.userLoc[0]!=0f &&
                userData.userLoc[1]!=0f;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode!=REQUEST_CODE_LOCATION){
            return;
        }

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initLocationServices();
        } else {
            logger.toast(getString(R.string.msg_location_disabled));
        }
    }

    protected void showInterstitialAd(){
        InterstitialAd interstitialAd = ComperioApplication.get(this).getInterstitialAd();
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Log.d(LOG_TAG,"The interstitial wasn't loaded yet.");
        }
    }

    protected NavigationManager getNavigationManager() {
        if (NavigationManager == null) {
            NavigationManager = new NavigationManager(this);
        }        

        return NavigationManager;
    }

    protected PersistenceManager getPersistanceManager() {
        if (persistanceManagerImpl == null) {
            persistanceManagerImpl = new PersistenceManager(this);
        }     

        return persistanceManagerImpl;
    }

    protected LoggingManager getLogger() {
        if (logger == null) {
            logger = new LoggingManager(this,LOG_TAG);
        }       

        return logger;
    }

}
