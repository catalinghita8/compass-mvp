package com.inspiringteam.transferxcompass.data.source.local;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Pair;

import com.inspiringteam.transferxcompass.di.scopes.AppScoped;
import com.inspiringteam.transferxcompass.util.Constants;

import javax.inject.Inject;

/**
 * Data Source that handles sensors events and data
 * It also holds true values of current and destination coordinates
 */
@AppScoped
public class CompassDataSource implements SensorEventListener {
    private static final String TAG = "CompassDataSource";

    private CompassListener mCompassListener;

    // sensor-related variables
    private SensorManager mSensorManager;
    private Sensor mAccSensor;
    private Sensor mMagSensor;

    private Pair<Double, Double> currentPositionPair;

    // sample coordinates for destination
    private Pair<Double, Double> destinationCoordinatesPair = Constants.SAMPLE_COORDINATES;

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float[] R = new float[9];
    private float[] I = new float[9];

    private float azimuth;

    private float currentAzimuth;

    private float currentDirectionAzimuth;

    @Inject
    public CompassDataSource(SensorManager sensorManager) {
        mSensorManager = sensorManager;
    }

    // point where flow of sensors events starts
    public void startEmitting(Pair<Double, Double> currentPos, Pair<Double, Double> destPos) {
        // acquire sensors
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // check if device has the these sensors and if not, bail out
        if (mAccSensor == null || mMagSensor == null) {
            mCompassListener.onErrorRetrievingDirection();
            return;
        }

        // else register the listeners
        mSensorManager.registerListener(this, mAccSensor,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagSensor,
                SensorManager.SENSOR_DELAY_GAME);

        currentPositionPair = currentPos;
        destinationCoordinatesPair = destPos;
    }

    // point where flow of sensors events stops
    public void stopEmitting() {
        mSensorManager.unregisterListener(this);
    }

    public void setListener(CompassListener l) {
        mCompassListener = l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];

            }

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                // orientation
                azimuth = (float) Math.toDegrees(orientation[0]);
                azimuth = (azimuth + 360) % 360;

                if (mCompassListener != null) {
                    // first calculate orientation regarding magnetic north
                    mCompassListener.onNewAzimuth(azimuth, currentAzimuth);

                    // update current azimuth
                    currentAzimuth = azimuth;

                    // now adapt current azimuth to the desired direction
                    double directionAzimuth =
                            azimuth - bearing(currentPositionPair.first, currentPositionPair.second,
                                    destinationCoordinatesPair.first, destinationCoordinatesPair.second);

                    mCompassListener.onNewDirectionAzimuth((float) directionAzimuth, currentDirectionAzimuth);
                    currentDirectionAzimuth = (float) directionAzimuth;
                }
            }
        }
    }

    public void setCurrentPositionPair(Pair<Double, Double> currentPositionPair) {
        this.currentPositionPair = currentPositionPair;
    }

    public Pair<Double, Double> getDestinationCoordinatesPair() {
        return destinationCoordinatesPair;
    }

    public void setDestinationCoordinatesPair(Pair<Double, Double> destinationCoordinatesPair) {
        this.destinationCoordinatesPair = destinationCoordinatesPair;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // method that allows us to calculate the bearing between two points
    private double bearing(double startLat, double startLng, double endLat, double endLng) {
        double latitude1 = Math.toRadians(startLat);
        double latitude2 = Math.toRadians(endLat);
        double longDiff = Math.toRadians(endLng - startLng);
        double y = Math.sin(longDiff) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }
}
