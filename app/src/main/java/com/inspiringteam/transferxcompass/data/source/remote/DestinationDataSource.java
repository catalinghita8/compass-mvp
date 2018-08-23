package com.inspiringteam.transferxcompass.data.source.remote;

import android.util.Pair;

import com.inspiringteam.transferxcompass.data.models.DestinationWrapper;
import com.inspiringteam.transferxcompass.di.scopes.AppScoped;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Data Source that returns random destination from API
 */
@AppScoped
public class DestinationDataSource {
    private DestinationService mApiService;

    @Inject
    public DestinationDataSource(DestinationService apiService) {
        this.mApiService = apiService;
    }

    // simple method that forwards the remote location through callbacks
    public void getDestination(final RemoteDestinationCallback cb) {
        mApiService.getDestination()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<DestinationWrapper>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        cb.onDisposableAcquired(d);
                    }

                    @Override
                    public void onSuccess(DestinationWrapper dW) {
                        cb.onDestinationLoaded(new Pair<>(dW.getLat(), dW.getLng()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        cb.onDataNotAvailable();
                    }
                });
    }
}
