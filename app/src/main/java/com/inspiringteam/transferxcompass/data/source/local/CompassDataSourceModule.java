package com.inspiringteam.transferxcompass.data.source.local;

import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;

import com.inspiringteam.transferxcompass.di.scopes.AppScoped;

import dagger.Module;
import dagger.Provides;

@Module
public class CompassDataSourceModule {
    @Provides
    @AppScoped
    SensorManager provideSensorManager(Application context) {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }
}
