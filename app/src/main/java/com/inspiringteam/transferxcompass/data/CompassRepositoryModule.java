package com.inspiringteam.transferxcompass.data;

import android.hardware.SensorManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.inspiringteam.transferxcompass.data.source.local.CompassDataSource;
import com.inspiringteam.transferxcompass.data.source.local.CompassDataSourceModule;
import com.inspiringteam.transferxcompass.data.source.local.LocationDataSource;
import com.inspiringteam.transferxcompass.data.source.local.LocationDataSourceModule;
import com.inspiringteam.transferxcompass.data.source.remote.DestinationSourceModule;
import com.inspiringteam.transferxcompass.di.scopes.AppScoped;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompassDataSourceModule.class, LocationDataSourceModule.class,
        DestinationSourceModule.class})
public class CompassRepositoryModule {
    @Provides
    @AppScoped
    CompassDataSource provideCompassDataSource(SensorManager sensorManager) {
        return new CompassDataSource(sensorManager);
    }

    @Provides
    @AppScoped
    LocationDataSource provideLocationDataSource(FusedLocationProviderClient fusedLocationProviderClient) {
        return new LocationDataSource(fusedLocationProviderClient);
    }
}
