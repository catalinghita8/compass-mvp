package com.inspiringteam.transferxcompass.ui.updatedestination;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.inspiringteam.transferxcompass.R;
import com.inspiringteam.transferxcompass.di.scopes.ActivityScoped;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * News Screen {@link UpdateDestinationContract.View}
 */
@ActivityScoped
public class UpdateDestinationFragment extends DaggerFragment implements UpdateDestinationContract.View {
    private static final String TAG = "UpdateDestinationFragment"; // for debugging

    private EditText mLatEditText;

    private EditText mLongEditText;

    private Button mSaveAndExitButton;

    @Inject
    UpdateDestinationPresenter mPresenter;

    @Inject
    public UpdateDestinationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_update_destinations, container, false);

        //setup views
        mLatEditText = root.findViewById(R.id.edit_lat);
        mLongEditText = root.findViewById(R.id.edit_long);
        mSaveAndExitButton = root.findViewById(R.id.button_save);

        // button functionality
        mSaveAndExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latText = mLatEditText.getText().toString();
                String longText = mLongEditText.getText().toString();
                if (latText.isEmpty() || longText.isEmpty()) {
                    mPresenter.onInputError();
                } else {
                    Pair<Double, Double> coords =
                            new Pair<>(Double.parseDouble(latText), Double.parseDouble(longText));

                    // update destination in repository
                    mPresenter.updateDestination(coords);

                    // then let's get back to the main screen
                    if (getActivity() != null) getActivity().finish();
                }
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.subscribe(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        mPresenter.unsubscribe();
    }

    @Override
    public void showInputError() {
        if (getView() != null)
            Snackbar.make(getView(), getString(R.string.error_on_input), Snackbar.LENGTH_LONG).show();
    }
}
