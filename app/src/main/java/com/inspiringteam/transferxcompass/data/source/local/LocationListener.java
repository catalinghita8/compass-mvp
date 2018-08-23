package com.inspiringteam.transferxcompass.data.source.local;

import android.util.Pair;

public interface LocationListener {
    void onNewCurrentLocation(Pair<Double, Double> currentPositionPair);

    void onErrorGettingLocation();
}
