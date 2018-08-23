package com.inspiringteam.transferxcompass.ui.compass;

import android.util.Pair;

import com.inspiringteam.transferxcompass.data.CompassRepository;
import com.inspiringteam.transferxcompass.data.source.local.CompassListener;
import com.inspiringteam.transferxcompass.data.source.local.LocationListener;
import com.inspiringteam.transferxcompass.data.source.remote.RemoteDestinationCallback;
import com.inspiringteam.transferxcompass.di.scopes.ActivityScoped;
import com.inspiringteam.transferxcompass.mvp.BasePresenter;
import com.inspiringteam.transferxcompass.util.Constants;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Presenter that acts as bridge between the Model Layer and the View layer
 */
@ActivityScoped
public class CompassPresenter extends BasePresenter<CompassContract.View>
        implements CompassContract.Presenter {
    private final CompassRepository mRepository;

    private CompositeDisposable disposables;

    @Inject
    public CompassPresenter(CompassRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public void subscribe(CompassContract.View view) {
        super.subscribe(view);
        disposables = new CompositeDisposable();

        if (mRepository != null) {
            // establishing communication between presenter and sensors data
            CompassListener compassListener = new CompassListener() {
                @Override
                public void onNewAzimuth(float azimuth, float currentAzimuth) {
                    // new orientation regarding the magnetic pole
                    loadCardinalDirection(azimuth, currentAzimuth);
                }

                @Override
                public void onNewDirectionAzimuth(float azimuth, float currentAzimuth) {
                    // new orientation regarding the destination
                    loadDestinationDirection(azimuth, currentAzimuth);
                }

                @Override
                public void onErrorRetrievingDirection() {
                    showError(Constants.COMPASS_DATA_SOURCE);
                }
            };

            // establishing communication between presenter and location events
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onNewCurrentLocation(Pair<Double, Double> currentPositionPair) {
                    mRepository.updateCurrentPosition(currentPositionPair);
                }

                @Override
                public void onErrorGettingLocation() {
                    showError(Constants.LOCATION_DATA_SOURCE);
                }
            };
            // first set the listeners to look out for missing sensors
            mRepository.setListeners(compassListener, locationListener);

            // now let's start the flow of data
            mRepository.startEmittingData();
        }
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();

        disposables.clear();
        disposables.dispose();

        if (mRepository != null) {
            mRepository.stopEmittingData();
        }
    }

    @Override
    public void loadDestinationDirection(float azimuth, float currentAzimuth) {
        view.showDestinationDirection(azimuth, currentAzimuth);
    }

    @Override
    public void loadCardinalDirection(float azimuth, float currentAzimuth) {
        view.showCardinalDirection(azimuth, currentAzimuth);
    }

    @Override
    public void loadDestinationCoordinates() {
        view.showDestinationCoordinates(mRepository.getDestination());
    }

    @Override
    public void getDestinationRemotely() {
        // show progress bar
        view.showLoadingBar();

        mRepository.getDestinationRemotely(new RemoteDestinationCallback() {
            @Override
            public void onDisposableAcquired(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onDestinationLoaded(Pair<Double, Double> coords) {
                view.showDestinationCoordinates(coords);

                // data was loaded, hide it
                view.hideLoadingBar();
            }

            @Override
            public void onDataNotAvailable() {
                view.showOnError(Constants.REMOTE_DATA_SOURCE);

                // also stop progress bar
                view.hideLoadingBar();
            }
        });

    }

    @Override
    public void updateDestinationManually() {
        view.showUpdateDestinationManually();
    }

    private void showError(int errorId) {
        view.showOnError(errorId);
    }

    private void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }
}
