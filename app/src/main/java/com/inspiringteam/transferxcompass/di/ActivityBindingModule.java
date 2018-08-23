package com.inspiringteam.transferxcompass.di;

import com.inspiringteam.transferxcompass.di.scopes.ActivityScoped;
import com.inspiringteam.transferxcompass.ui.compass.CompassActivity;
import com.inspiringteam.transferxcompass.ui.compass.CompassModule;
import com.inspiringteam.transferxcompass.ui.updatedestination.UpdateDestinationActivity;
import com.inspiringteam.transferxcompass.ui.updatedestination.UpdateDestinationModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of
 * whichever module ActivityBindingModule is on (AppComponent, here).
 * we never need to tell AppComponent that it is going to have all or any of these subcomponents
 * nor do we need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and
 * be aware of a scope annotation @ActivityScoped
 * In this case, when Dagger.Android annotation processor runs it will create 2 subcomponent for us
 */
@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = CompassModule.class)
    abstract CompassActivity compassActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = UpdateDestinationModule.class)
    abstract UpdateDestinationActivity updateDestinationActivity();
}

