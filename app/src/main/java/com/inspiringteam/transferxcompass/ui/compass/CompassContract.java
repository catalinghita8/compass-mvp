package com.inspiringteam.transferxcompass.ui.compass;

import android.util.Pair;

import com.inspiringteam.transferxcompass.mvp.BaseView;

/**
 * Contract that rules how the Compass Screen Components behave
 */
public interface CompassContract {
    interface View extends BaseView<Presenter> {
        void showCardinalDirection(float azimuth, float currentAzimuth);

        void showDestinationDirection(float azimuth, float currentAzimuth);

        void showOnError(int errorId);

        void showDestinationCoordinates(Pair<Double, Double> coords);

        void showUpdateDestinationManually();

        void showLoadingBar();

        void hideLoadingBar();
    }

    interface Presenter {
        void loadDestinationDirection(float azimuth, float currentAzimuth);

        void loadCardinalDirection(float azimuth, float currentAzimuth);

        void loadDestinationCoordinates();

        void updateDestinationManually();

        void getDestinationRemotely();
    }
}
