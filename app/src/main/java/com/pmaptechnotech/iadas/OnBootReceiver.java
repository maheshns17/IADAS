package com.pmaptechnotech.iadas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.pmaptechnotech.iadas.activities.AlertActivity;
import com.pmaptechnotech.iadas.logics.P;

public class OnBootReceiver extends BroadcastReceiver {

    Context mContext;
    private static final int FORCE_THRESHOLD = 350;
    private static final int TIME_THRESHOLD = 100;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int SHAKE_DURATION = 1000;
    private static final int SHAKE_COUNT = 3;

    private SensorManager mSensorMgr;
    private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
    private long mLastTime;
    private int mShakeCount = 0;
    private long mLastShake;
    private long mLastForce;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        SensorManager sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sManager.registerListener(new ShakeEventListener(), sensor, SensorManager.SENSOR_DELAY_NORMAL); // or other delay

    }

    class ShakeEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {

            long now = System.currentTimeMillis();

            if ((now - mLastForce) > SHAKE_TIMEOUT) {
                mShakeCount = 0;
            }

            if ((now - mLastTime) > TIME_THRESHOLD) {
                long diff = now - mLastTime;
                float speed = Math.abs(event.values[SensorManager.DATA_X] + event.values[SensorManager.DATA_Y] + event.values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
                if (speed > FORCE_THRESHOLD) {
                    if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
                        mLastShake = now;
                        mShakeCount = 0;
                        foundShake();
                    }
                    mLastForce = now;
                }
                mLastTime = now;
                mLastX = event.values[SensorManager.DATA_X];
                mLastY = event.values[SensorManager.DATA_Y];
                mLastZ = event.values[SensorManager.DATA_Z];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        public void foundShake() {
            //Toast.makeText(context, "Mobile Falls Down", Toast.LENGTH_LONG).show();
            if (P.getUserDetails(mContext).on_off) {
                Intent intent = new Intent(mContext, AlertActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }
    }
}
