package com.inspiringteam.transferxcompass.ui.updatedestination;

import android.util.Pair;

import com.inspiringteam.transferxcompass.data.CompassRepository;
import com.inspiringteam.transferxcompass.di.scopes.ActivityScoped;
import com.inspiringteam.transferxcompass.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * Presenter that acts as bridge between the Model Layer and the View layer
 */
@ActivityScoped
public class UpdateDestinationPresenter extends BasePresenter<UpdateDestinationContract.View>
        implements UpdateDestinationContract.Presenter {

    private final CompassRepository mRepository;

    @Inject
    public UpdateDestinationPresenter(CompassRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public void subscribe(UpdateDestinationContract.View view) {
        super.subscribe(view);
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public void onInputError() {
        view.showInputError();
    }

    @Override
    public void updateDestination(Pair<Double, Double> coords) {
        // basically the only action required by this screen
        mRepository.updateDestination(coords);
    }
}
