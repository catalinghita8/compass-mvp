package com.inspiringteam.transferxcompass.data.source.local;

public interface CompassListener {
    void onNewAzimuth(float azimuth, float currentAzimuth);

    void onNewDirectionAzimuth(float azimuth, float currentAzimuth);

    void onErrorRetrievingDirection();
}
