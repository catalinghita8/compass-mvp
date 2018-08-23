package com.inspiringteam.transferxcompass.data;

import android.util.Pair;

import com.inspiringteam.transferxcompass.data.source.local.CompassDataSource;
import com.inspiringteam.transferxcompass.data.source.local.CompassListener;
import com.inspiringteam.transferxcompass.data.source.local.LocationDataSource;
import com.inspiringteam.transferxcompass.data.source.local.LocationListener;
import com.inspiringteam.transferxcompass.data.source.remote.DestinationDataSource;
import com.inspiringteam.transferxcompass.data.source.remote.RemoteDestinationCallback;
import com.inspiringteam.transferxcompass.di.scopes.AppScoped;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Data Repository that handles location events, sensor data/events and remote queries
 */
@AppScoped
public class CompassRepository {
    private CompassDataSource mCompassDataSource;

    private LocationDataSource mLocationDataSource;

    private DestinationDataSource mDestinationDataSource;

    @Inject
    public CompassRepository(CompassDataSource compassDataSource, LocationDataSource locationDataSource,
                             DestinationDataSource destinationDataSource) {
        mCompassDataSource = compassDataSource;
        mLocationDataSource = locationDataSource;
        mDestinationDataSource = destinationDataSource;
    }

    public void startEmittingData() {
        mCompassDataSource.startEmitting(mLocationDataSource.getCurrentCoordinates(),
                mCompassDataSource.getDestinationCoordinatesPair());
    }

    public void stopEmittingData() {
        mCompassDataSource.stopEmitting();
        mLocationDataSource.stopLocationUpdates();

        // reset listeners
        mLocationDataSource.setListener(null);
        mCompassDataSource.setListener(null);
    }

    public void setListeners(CompassListener compassListener, LocationListener locationListener) {
        mCompassDataSource.setListener(compassListener);
        mLocationDataSource.setListener(locationListener);
    }

    public void updateCurrentPosition(Pair<Double, Double> currentPositionPair) {
        mLocationDataSource.setCurrentPositionPair(currentPositionPair);
        mCompassDataSource.setCurrentPositionPair(currentPositionPair);
    }

    public void getDestinationRemotely(final RemoteDestinationCallback cb) {
        mDestinationDataSource.getDestination(new RemoteDestinationCallback() {
            @Override
            public void onDisposableAcquired(Disposable disposable) {
                cb.onDisposableAcquired(disposable);
            }

            @Override
            public void onDestinationLoaded(Pair<Double, Double> coords) {
                cb.onDestinationLoaded(coords);

                // also update destination so that it reflects on repository data
                updateDestination(coords);
            }

            @Override
            public void onDataNotAvailable() {
                cb.onDataNotAvailable();
            }
        });
    }

    public void updateDestination(Pair<Double, Double> destinationPair) {
        mCompassDataSource.setDestinationCoordinatesPair(destinationPair);
    }

    public Pair<Double, Double> getDestination() {
        return mCompassDataSource.getDestinationCoordinatesPair();
    }
}
