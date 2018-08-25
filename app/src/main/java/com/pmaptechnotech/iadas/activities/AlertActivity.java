package com.pmaptechnotech.iadas.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.pmaptechnotech.iadas.R;
import com.pmaptechnotech.iadas.api.Api;
import com.pmaptechnotech.iadas.api.WebServices;
import com.pmaptechnotech.iadas.logics.P;
import com.pmaptechnotech.iadas.logics.U;
import com.pmaptechnotech.iadas.models.GetMobileNumbersToSendSmsInput;
import com.pmaptechnotech.iadas.models.GetMobileNumbersToSendSmsResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AlertActivity extends Activity implements LocationListener {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @BindView(R.id.edt_number)
    EditText edt_number;
    @BindView(R.id.edt_sms)
    EditText edt_sms;
    @BindView(R.id.btn_flashlight)
    Button btn_flashlight;
    @BindView(R.id.btn_Play)
    Button btn_Play;
    @BindView(R.id.btn_send)
    Button btn_send;
    private SweetAlertDialog dialog;
    private Context context;

    EditText txtphoneNo;
    EditText txtMessage;
    String phoneNo;
    String message;
    private Camera camera;
    private Camera.Parameters parameters;
    boolean isFlashLightOn = false;
    Button flashLightButton;
    MediaPlayer mp;
    private boolean checked = true;
    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        mp = MediaPlayer.create(this, R.raw.alert);
        mp.start();
        context = AlertActivity.this;
        ButterKnife.bind(this);

        txtphoneNo = (EditText) findViewById(R.id.edt_number);
        txtMessage = (EditText) findViewById(R.id.edt_sms);

        flashLightButton = (Button) findViewById(R.id.btn_flashlight);
        flashLightButton.setOnClickListener(new FlashOnOffListener());

        btn_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMSMessage();
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 60000, 0, this);
        if (isFlashSupported()) {
            camera = Camera.open();
            parameters = camera.getParameters();
        } else {
            showNoFlashAlert();
        }
        getMobileNumbersToSendSMS();
        startCountDownTimer();


    }

    private void sendSMSMessage() {
        phoneNo = txtphoneNo.getText().toString();
        message = txtMessage.getText().toString();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        //FLASH LIGHT
      /*  if (isFlashSupported()) {
            camera = Camera.open();
            parameters = camera.getParameters();
        } else {
            showNoFlashAlert();
        }*/
    }

    private class FlashOnOffListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (isFlashLightOn) {
                //flashLightButton.setImageResource(R.drawable.flashlight_off);
                parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
                isFlashLightOn = false;


            } else {
                //flashLightButton.setImageResource(R.drawable.flashlight_on);
                parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();
                isFlashLightOn = true;
            }
        }
    }

    private void showNoFlashAlert() {
        new AlertDialog.Builder(this)
                .setMessage("Your device hardware does not support flashlight!")
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle("Error")
                .setPositiveButton("Ok", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    private boolean isFlashSupported() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /* @Override
     protected void onDestroy() {
         if (camera != null) {
             camera.stopPreview();
             camera.release();
             camera = null;
         }
         super.onDestroy();
     }*/
    public void onDestroy() {

        mp.stop();
        try {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    //ALERT SOUND
    public void playSong(View v) {
        mp.start();
    }


    //PRASAD CODE
    private void startTorchOnOff() {
        Log.d("ALERTACTIVITY", "startTorchOnOff Called : " + checked);
        // if (camera != null) {
        if (checked) {
            //ToDo something

            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            checked = false;
        } else {

            //ToDo something

            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            //camera.release();
            checked = true;

        }
        // }
    }


    private void startCountDownTimer() {
        new CountDownTimer(60 * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                startTorchOnOff();
            }

            public void onFinish() {

            }
        }.start();
    }


    private void getMobileNumbersToSendSMS() {

        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS

        double latlngs[] = P.GetMinMaxLatLng(latitude + "", longitude + "");
        GetMobileNumbersToSendSmsInput GetMobileNumbersToSendSmsInput = new GetMobileNumbersToSendSmsInput(
                P.getUserDetails(context).user_id,
                latlngs[0] + "",
                latlngs[1] + "",
                latlngs[2] + "",
                latlngs[3] + ""
        );

        dialog = P.showBufferDialog(context, "Processing...");


        //CALL NOW
        webServices.getMobileNumbersToSendSms(GetMobileNumbersToSendSmsInput)
                .enqueue(new Callback<GetMobileNumbersToSendSmsResult>() {
                    @Override
                    public void onResponse(Call<GetMobileNumbersToSendSmsResult> call, Response<GetMobileNumbersToSendSmsResult> response) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (!P.analyseResponse(response)) {
                            //Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.server_error));
                            return;
                        }
                        GetMobileNumbersToSendSmsResult result = response.body();
                        String url = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude;
                        if (result.is_success) {

                            for (int i = 0; i < result.trustees.size(); i++) {
                                //mobile_numbers = mobile_numbers + "" + result.trustees.get(i).trustee_mobile_number + ",";
                                P.sendSMS(result.trustees.get(i).trustee_mobile_number, P.getUserDetails(context).user_username + " LatLng:" + url);
                            }
                            for (int i = 0; i < result.hospitals.size(); i++) {
                                //mobile_numbers = mobile_numbers + "" + result.hospitals.get(i).hospital_mobile_number + ",";
                                P.sendSMS(result.hospitals.get(i).hospital_mobile_number, P.getUserDetails(context).user_username + " LatLng:" + url);
                            }
                            //P.sendSMS(mobile_numbers, P.getUserDetails(context).user_username + " seems met with an accident at Lat:" + latitude + ", Lng:" + longitude + "");
                        } else {
                            getMobileNumbersToSendSMS();
                            //P.ShowErrorDialogAndBeHere(context, getString(R.string.error), result.msg);
                            //Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetMobileNumbersToSendSmsResult> call, Throwable t) {
                        P.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                        if (dialog.isShowing()) dialog.dismiss();
                        P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.check_internet_connection));
                        getMobileNumbersToSendSMS();
                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {
        P.LogD("GooglePlacesActivity : onLocationChanged ");
        latitude = location.getLatitude();
        longitude = location.getLongitude();


    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
