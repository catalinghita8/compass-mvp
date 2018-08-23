package com.inspiringteam.transferxcompass.ui.updatedestination;

import android.util.Pair;

import com.inspiringteam.transferxcompass.mvp.BaseView;
import com.inspiringteam.transferxcompass.ui.compass.CompassContract;

/**
 * Contract that rules how the Update Destination Components behave
 */
public interface UpdateDestinationContract {
    interface View extends BaseView<CompassContract.Presenter> {
        void showInputError();
    }

    interface Presenter {
        void onInputError();

        void updateDestination(Pair<Double, Double> coords);
    }
}
