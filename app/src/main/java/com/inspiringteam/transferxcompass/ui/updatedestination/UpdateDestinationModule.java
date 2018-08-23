package com.inspiringteam.transferxcompass.ui.updatedestination;

import com.inspiringteam.transferxcompass.di.scopes.ActivityScoped;
import com.inspiringteam.transferxcompass.di.scopes.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class UpdateDestinationModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract UpdateDestinationFragment updateDestinationFragment();

    @ActivityScoped
    @Binds
    abstract UpdateDestinationContract.Presenter updateDestinationPresenter(UpdateDestinationPresenter presenter);
}
