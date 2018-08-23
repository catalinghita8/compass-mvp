package com.inspiringteam.transferxcompass.ui.compass;

import com.inspiringteam.transferxcompass.di.scopes.ActivityScoped;
import com.inspiringteam.transferxcompass.di.scopes.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link CompassPresenter}.
 */
@Module
public abstract class CompassModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract CompassFragment compassFragment();

    @ActivityScoped
    @Binds
    abstract CompassContract.Presenter compassPresenter(CompassPresenter presenter);
}
