package com.inspiringteam.transferxcompass.ui.compass;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inspiringteam.transferxcompass.R;
import com.inspiringteam.transferxcompass.di.scopes.ActivityScoped;
import com.inspiringteam.transferxcompass.ui.updatedestination.UpdateDestinationActivity;
import com.inspiringteam.transferxcompass.util.FormatUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


/**
 * News Screen {@link CompassContract.View}
 */
@ActivityScoped
public class CompassFragment extends DaggerFragment implements CompassContract.View {
    private static final String TAG = "CompassFragment";

    private ImageView mArrowView;

    private ImageView mDirectionView;

    private TextView mDestinationLatView;

    private TextView mDestinationLongView;

    private Button mUpdateDestManuallyButton;

    private Button mUpdateDestRemotelyButton;

    private ProgressBar mProgressBar;

    @Inject
    public CompassFragment() {
        // Required empty public constructor
    }

    @Inject
    CompassPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_compass, container, false);

        linkViews(root);

        mUpdateDestManuallyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.updateDestinationManually();
            }
        });

        mUpdateDestRemotelyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getDestinationRemotely();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.subscribe(this);

        // loading the destination in onResume allows us to keep an updated destination
        mPresenter.loadDestinationCoordinates();
    }

    @Override
    public void onPause() {
        super.onPause();

        mPresenter.unsubscribe();
    }

    @Override
    public void showCardinalDirection(float azimuth, float currentAzimuth) {
        adjustArrow(azimuth, currentAzimuth, mArrowView);
    }

    @Override
    public void showDestinationDirection(float azimuth, float currentAzimuth) {
        adjustArrow(azimuth, currentAzimuth, mDirectionView);
    }

    @Override
    public void showOnError(int errorId) {
        if (getView() != null)
            Snackbar.make(getView(), getAppropriateMessage(errorId), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showDestinationCoordinates(Pair<Double, Double> coords) {
        mDestinationLatView.setText(FormatUtils.getStringFromDouble(coords.first));
        mDestinationLongView.setText(FormatUtils.getStringFromDouble(coords.second));
    }

    @Override
    public void showUpdateDestinationManually() {
        Intent intent = new Intent(getContext(), UpdateDestinationActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoadingBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Internal helper methods
     */
    private void adjustArrow(float azimuth, float currentAzimuth, View targetView) {
        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        targetView.startAnimation(an);
    }

    private String getAppropriateMessage(int errorId) {
        if (errorId == 0) return getString(R.string.error_location);
        else if (errorId == 1) return getString(R.string.error_direction);

        return getString(R.string.error_on_remote_source);
    }

    private void linkViews(View root) {
        mArrowView = root.findViewById(R.id.main_image_hands);
        mDirectionView = root.findViewById(R.id.main_image_direction);
        mDestinationLatView = root.findViewById(R.id.destination_lat);
        mDestinationLongView = root.findViewById(R.id.destination_long);
        mUpdateDestManuallyButton = root.findViewById(R.id.button_enter_destination);
        mUpdateDestRemotelyButton = root.findViewById(R.id.button_get_destination);
        mProgressBar = root.findViewById(R.id.pbHeaderProgress);
    }
}
