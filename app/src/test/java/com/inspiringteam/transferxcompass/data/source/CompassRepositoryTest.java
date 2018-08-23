package com.inspiringteam.transferxcompass.data.source;

import android.util.Pair;

import com.inspiringteam.transferxcompass.data.CompassRepository;
import com.inspiringteam.transferxcompass.data.source.local.CompassDataSource;
import com.inspiringteam.transferxcompass.data.source.local.CompassListener;
import com.inspiringteam.transferxcompass.data.source.local.LocationDataSource;
import com.inspiringteam.transferxcompass.data.source.local.LocationListener;
import com.inspiringteam.transferxcompass.data.source.remote.DestinationDataSource;
import com.inspiringteam.transferxcompass.data.source.remote.RemoteDestinationCallback;
import com.inspiringteam.transferxcompass.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Unit tests
 * SUT - {@link CompassRepository}
 */
public class CompassRepositoryTest {

    private CompassRepository mCompassRepository;

    @Mock
    private CompassDataSource mCompassDatasource;

    @Mock
    private LocationDataSource mLocationDataSource;

    @Mock
    private DestinationDataSource mDestinationDataSource;

    @Mock
    private CompassListener mCompassListener;

    @Mock
    private LocationListener mLocationListener;

    @Mock
    private RemoteDestinationCallback mCallback;

    @Captor
    private ArgumentCaptor<RemoteDestinationCallback> mCallbackCaptor;


    @Before
    public void setupCompassRepository() {
        // init mocks
        MockitoAnnotations.initMocks(this);

        // get a reference to the class under test
        mCompassRepository = new CompassRepository(mCompassDatasource, mLocationDataSource,
                mDestinationDataSource);
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mCompassRepository);
    }

    @Test
    public void emitData_LocationAndCompassSource() {
        mCompassRepository.startEmittingData();

        verify(mCompassDatasource)
                .startEmitting(any(Pair.class), any(Pair.class));
    }

    /**
     * Scenario states:
     * Both data sources should stop upon command
     */
    @Test
    public void stopEmittingData_stopSources() {
        mCompassRepository.stopEmittingData();

        verify(mCompassDatasource).stopEmitting();
        verify(mLocationDataSource).stopLocationUpdates();
    }

    /**
     * Scenario states:
     * Both listeners should be set
     */
    @Test
    public void setListeners() {
        mCompassRepository.setListeners(mCompassListener, mLocationListener);

        verify(mCompassDatasource).setListener(mCompassListener);
        verify(mLocationDataSource).setListener(mLocationListener);
    }

    /**
     * Scenario states:
     * Upon updating current position from fused client, both Location sources and Compass sources
     * should be updated. This is because the compass data source needs updated position data,
     * whereas the location repository is queried for current location the first time a presenter
     * is binding, which might happen upon different events
     */
    @Test
    public void updateCurrentPositionInBothSources() {
        mCompassRepository.setListeners(mCompassListener, mLocationListener);

        verify(mCompassDatasource).setListener(mCompassListener);
        verify(mLocationDataSource).setListener(mLocationListener);
    }

    @Test
    public void getDestinationRemotely_OnSuccess() {
        mCompassRepository.getDestinationRemotely(mCallback);

        // check if service is queried
        verify(mDestinationDataSource).getDestination(mCallbackCaptor.capture());

        // on success
        mCallbackCaptor.getValue().onDestinationLoaded(Constants.SAMPLE_COORDINATES);

        // compass source should save destination coords
        verify(mCompassDatasource).setDestinationCoordinatesPair(Constants.SAMPLE_COORDINATES);
    }

    @Test
    public void getDestination_fromCompassSource() {
        mCompassRepository.getDestination();

        //destination should only bet obtained from CompassSource
        verify(mCompassDatasource).getDestinationCoordinatesPair();

    }

}
