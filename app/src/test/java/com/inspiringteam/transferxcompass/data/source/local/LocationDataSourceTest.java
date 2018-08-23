package com.inspiringteam.transferxcompass.data.source.local;

import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.inspiringteam.transferxcompass.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test
 * SUT - {@link LocationDataSource}
 */
public class LocationDataSourceTest {

    private LocationDataSource mLocationDataSource;

    @Mock
    FusedLocationProviderClient mFusedClient;

    @Mock
    Task<Location> mTask;


    @Before
    public void setup() {
        // init mocks
        MockitoAnnotations.initMocks(this);

        ArgumentCaptor<OnSuccessListener> listenerCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);
        ArgumentCaptor<OnFailureListener> failureCaptor = ArgumentCaptor.forClass(OnFailureListener.class);

        // prepare expected behaviour
        when(mTask.addOnFailureListener(failureCaptor.capture())).thenReturn(mTask);
        when(mTask.addOnSuccessListener(listenerCaptor.capture())).thenReturn(mTask);

        when(mFusedClient.getLastLocation()).thenReturn(mTask);

        // get reference to the class in test
        mLocationDataSource = new LocationDataSource(mFusedClient);
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocationDataSource);
    }

    /**
     * Test scenario states:
     * Upon getting coordonates, the SUT should first retrieve last known location as starting point,
     * and then activate location updates that propagate through callbacks
     */
    @Test
    public void getCurrentCoordinates_StartLocationEvents() {
        // start querying for coords
        mLocationDataSource.getCurrentCoordinates();

        // first get last know location
        verify(mFusedClient).getLastLocation();

        // then start location updates
        verify(mFusedClient)
                .requestLocationUpdates(any(LocationRequest.class), any(LocationCallback.class), any(Looper.class));

    }

    /**
     * Test scenario states:
     * Upon setting coords, values should be returned
     */
    @Test
    public void setCurrentCoordinates_getCoordinates() {
        // after setting coords
        mLocationDataSource.setCurrentPositionPair(Constants.SAMPLE_COORDINATES);

        // return non-null values
        assertThat(mLocationDataSource.getCurrentCoordinates(), notNullValue());
    }

    /**
     * Test scenario states:
     * Upon stopping location updates, fusion client should stop querying for events
     */
    @Test
    public void stopLocationUpdates() {
        // after stopping updates
        mLocationDataSource.stopLocationUpdates();

        // remove location updates
        verify(mFusedClient).removeLocationUpdates(any(LocationCallback.class));
    }
}
