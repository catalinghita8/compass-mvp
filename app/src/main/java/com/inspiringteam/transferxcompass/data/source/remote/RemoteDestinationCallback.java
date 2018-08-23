package com.inspiringteam.transferxcompass.data.source.remote;

import android.util.Pair;

import io.reactivex.disposables.Disposable;

public interface RemoteDestinationCallback {
    void onDisposableAcquired(Disposable disposable);

    void onDestinationLoaded(Pair<Double, Double> coords);

    void onDataNotAvailable();
}
