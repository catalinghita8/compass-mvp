package com.inspiringteam.transferxcompass.data.source.local;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.inspiringteam.transferxcompass.di.scopes.AppScoped;
import com.inspiringteam.transferxcompass.util.Constants;

import javax.inject.Inject;

/**
 * Data Source that handles location events and data
 */
@AppScoped
public class LocationDataSource {

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationListener mLocationListener;

    private LocationRequest mLocationRequest;

    private LocationCallback mLocationCallback;

    // sample coordinates that are overwritten as soon as the current location is provided
    private Pair<Double, Double> currentPositionPair = Constants.SAMPLE_COORDINATES2;

    @Inject
    public LocationDataSource(FusedLocationProviderClient fusedClient) {
        mFusedLocationClient = fusedClient;
    }

    public Pair<Double, Double> getCurrentCoordinates() {
        enableLocationUpdates();

        return currentPositionPair;
    }

    public void setListener(LocationListener l) {
        mLocationListener = l;
    }

    public void setCurrentPositionPair(Pair<Double, Double> currentPositionPair) {
        this.currentPositionPair = currentPositionPair;
    }

    public void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void enableLocationUpdates() {
        // first, we get the last known position to have a starting pointing
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                currentPositionPair = processedCurrentLocation(location);
                                // update repository
                                if (mLocationListener != null)
                                    mLocationListener.onNewCurrentLocation(currentPositionPair);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mLocationListener.onErrorGettingLocation();
                        }
                    });

            // then, we get location updates if user strays away from the path for more than 100m
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        mLocationListener.onErrorGettingLocation();
                    }
                    if (mLocationListener != null && locationResult != null) {
                        // get location
                        Location location = locationResult.getLocations().get(0);

                        currentPositionPair = processedCurrentLocation(location);

                        // update repository too
                        mLocationListener.onNewCurrentLocation(currentPositionPair);
                    }

                }
            };

            // now we need location updates whenever a certain distance has been covered, only because
            // direction changes if distance is not covered directly to the target
            createLocationRequest();
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        } catch (SecurityException e) {
            mLocationListener.onErrorGettingLocation();
        }
    }

    private Pair<Double, Double> processedCurrentLocation(Location location) {
        return new Pair<>(location.getLatitude(), location.getLongitude());
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // 100m is more than enough to recalibrate the direction
        mLocationRequest.setSmallestDisplacement(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

}
