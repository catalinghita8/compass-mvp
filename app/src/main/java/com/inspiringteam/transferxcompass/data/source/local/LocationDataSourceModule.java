package com.inspiringteam.transferxcompass.data.source.local;

import android.app.Application;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.inspiringteam.transferxcompass.di.scopes.AppScoped;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationDataSourceModule {
    @AppScoped
    @Provides
    FusedLocationProviderClient provideFusedLocationClient(Application context) {
        return new FusedLocationProviderClient(context);
    }
}
