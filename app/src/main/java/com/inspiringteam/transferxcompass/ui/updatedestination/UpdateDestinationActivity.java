package com.inspiringteam.transferxcompass.ui.updatedestination;

import android.os.Bundle;

import com.inspiringteam.transferxcompass.R;
import com.inspiringteam.transferxcompass.util.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Secondary light-weight Activity that acts as container to the UpdateDestination Screen
 */
public class UpdateDestinationActivity extends DaggerAppCompatActivity {
    @Inject
    UpdateDestinationFragment mInjectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_destination);

        UpdateDestinationFragment fragment = (UpdateDestinationFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = mInjectedFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
    }
}
