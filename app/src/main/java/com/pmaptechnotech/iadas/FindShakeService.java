package com.pmaptechnotech.iadas;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.pmaptechnotech.iadas.activities.AlertActivity;
import com.pmaptechnotech.iadas.logics.P;

public class FindShakeService extends IntentService implements SensorListener {

    public FindShakeService() {
        super("FindShakeService");
    }

    private static final int FORCE_THRESHOLD = 350;
    private static final int TIME_THRESHOLD = 100;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int SHAKE_DURATION = 1000;
    private static final int SHAKE_COUNT = 3;

    private SensorManager mSensorMgr;
    private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
    private long mLastTime;
    private Context mContext;
    private int mShakeCount = 0;
    private long mLastShake;
    private long mLastForce;

    private static final int SHAKE_THRESHOLD = 800;
    SensorManager sensorMgr;
    private Context context;

    @Override
    protected void onHandleIntent(Intent intent) {
        context = FindShakeService.this;

        sensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorMgr.registerListener(this,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);
        PrimeThread p = new PrimeThread();
        p.start();
    }

    class PrimeThread extends Thread {

        public void run() {
            sensorMgr.registerListener(FindShakeService.this,
                    SensorManager.SENSOR_ACCELEROMETER,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        {
            if (sensor != SensorManager.SENSOR_ACCELEROMETER) return;
            long now = System.currentTimeMillis();

            if ((now - mLastForce) > SHAKE_TIMEOUT) {
                mShakeCount = 0;
            }

            if ((now - mLastTime) > TIME_THRESHOLD) {
                long diff = now - mLastTime;
                float speed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
                if (speed > FORCE_THRESHOLD) {
                    if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
                        mLastShake = now;
                        mShakeCount = 0;
                        foundShake();
                    }
                    mLastForce = now;
                }
                mLastTime = now;
                mLastX = values[SensorManager.DATA_X];
                mLastY = values[SensorManager.DATA_Y];
                mLastZ = values[SensorManager.DATA_Z];
            }

        }
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }

    public void foundShake() {
        Toast.makeText(context, "Mobile Falls Down", Toast.LENGTH_LONG).show();
        if (P.getUserDetails(context).on_off) {
            Intent intent = new Intent(this, AlertActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
