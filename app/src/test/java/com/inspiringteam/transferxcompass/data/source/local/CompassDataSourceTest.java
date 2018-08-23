package com.inspiringteam.transferxcompass.data.source.local;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.inspiringteam.transferxcompass.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CompassDataSourceTest {

    private CompassDataSource mCompassDataSource;

    @Mock
    SensorManager mSensorManager;

    @Mock
    Sensor mSensor;

    @Mock
    CompassListener mCompassListener;


    @Before
    public void setup() {
        // init mocks
        MockitoAnnotations.initMocks(this);

        // get reference on SUT
        mCompassDataSource = new CompassDataSource(mSensorManager);

        // set listener
        mCompassDataSource.setListener(mCompassListener);
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mCompassDataSource);
    }

    /**
     * Test scenario states:
     * Upon having valid sensors, we shoud get 2 sensor register calls after we start emitting
     */
    @Test
    public void testValidSensors_RegisterSensorListeners() {
        // suppose we have sensors on device
        when(mSensorManager.getDefaultSensor(anyInt())).thenReturn(mSensor);

        mCompassDataSource.startEmitting(Constants.SAMPLE_COORDINATES, Constants.SAMPLE_COORDINATES2);

        // verify is the sensors are registered ( should be 2 invocations)
        verify(mSensorManager, times(2))
                .registerListener(any(SensorEventListener.class), any(Sensor.class), anyInt());
    }

    /**
     * Test scenario states:
     * Upon having no sensors, when emission starts, error should be propagated
     */
    @Test
    public void testNullSensors_BailOut() {
        // suppose we don't have required sensors on device
        when(mSensorManager.getDefaultSensor(anyInt())).thenReturn(null);

        mCompassDataSource.startEmitting(Constants.SAMPLE_COORDINATES, Constants.SAMPLE_COORDINATES2);

        // should bail out with error
        verify(mCompassListener).onErrorRetrievingDirection();
    }

    /**
     * Test scenario states:
     * When the source is prompted to stop events from being fetched,
     * the sensor manager should unregister the listener
     */
    @Test
    public void stopEmitting() {
        mCompassDataSource.stopEmitting();

        // should unregister listener
        verify(mSensorManager).unregisterListener(any(SensorEventListener.class));
    }

}
