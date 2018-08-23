package com.inspiringteam.transferxcompass.ui.compass;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.inspiringteam.transferxcompass.R;
import com.inspiringteam.transferxcompass.util.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Main Activity that acts as container to the CompassScreen
 */
public class CompassActivity extends DaggerAppCompatActivity {
    @Inject
    CompassPresenter mPresenter;

    @Inject
    CompassFragment mInjectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // if permissions are granted, let's move on
        if (appHasPermissions()) launchFragment();
        else requestPermission();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // if permissions are granted, let's move on
                launchFragment();
            } else {
                // vicious cycle here
                requestPermission();
            }
        }
    }

    // Easy method that launches specified fragment
    private void launchFragment() {
        CompassFragment fragment = (CompassFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = mInjectedFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
    }

    /**
     * Boilerplate code that handles permission, could be moved to helper class
     */
    private boolean appHasPermissions() {
        return checkFinePermission() && checkCoarsePermission();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]
                {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1);
    }

    private boolean checkFinePermission() {
        int result = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkCoarsePermission() {
        int result = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED;
    }
}

