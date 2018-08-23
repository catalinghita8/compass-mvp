package com.inspiringteam.transferxcompass.compass;

import android.util.Pair;

import com.inspiringteam.transferxcompass.data.CompassRepository;
import com.inspiringteam.transferxcompass.data.source.local.CompassListener;
import com.inspiringteam.transferxcompass.data.source.local.LocationListener;
import com.inspiringteam.transferxcompass.data.source.remote.RemoteDestinationCallback;
import com.inspiringteam.transferxcompass.ui.compass.CompassContract;
import com.inspiringteam.transferxcompass.ui.compass.CompassPresenter;
import com.inspiringteam.transferxcompass.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Unit tests
 * SUT - {@link CompassPresenter}
 */
public class CompassPresenterTest {

    private CompassPresenter mCompassPresenter;

    @Mock
    private CompassRepository mRepository;

    @Mock
    private CompassContract.View mCompassView;

    @Mock
    private RemoteDestinationCallback mCallback;

    @Captor
    private ArgumentCaptor<RemoteDestinationCallback> mCallbackCaptor;

    @Before
    public void setupNewsPresenter() {
        // inject the mocks
        MockitoAnnotations.initMocks(this);

        mCompassPresenter = new CompassPresenter(mRepository);

    }
    // TODO implements tests to check callbacks are made correctly


    @Test
    public void subscribeToEventsFromRepository() {
        mCompassPresenter.subscribe(mCompassView);

        verify(mRepository).setListeners(any(CompassListener.class),
                any(LocationListener.class));

        verify(mRepository).startEmittingData();
    }

    @Test
    public void unsubscribeFromEventsFromRepository() {
        mCompassPresenter.subscribe(mCompassView);

        mCompassPresenter.unsubscribe();

        verify(mRepository).stopEmittingData();
    }

    @Test
    public void getDestinationRemotely_OnSuccess() {
        mCompassPresenter.subscribe(mCompassView);

        mCompassPresenter.getDestinationRemotely();

        verify(mRepository).getDestinationRemotely(mCallbackCaptor.capture());
        mCallbackCaptor.getValue().onDestinationLoaded(Constants.SAMPLE_COORDINATES);

        ArgumentCaptor<Pair> showCoordsCaptor = ArgumentCaptor.forClass(Pair.class);
        verify(mCompassView).showDestinationCoordinates(showCoordsCaptor.capture());

        assertTrue(showCoordsCaptor.getValue() == Constants.SAMPLE_COORDINATES);
    }
}
