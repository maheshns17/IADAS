package com.pmaptechnotech.iadas.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pmaptechnotech.iadas.R;
import com.pmaptechnotech.iadas.logics.C;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationActivity extends AppCompatActivity {

    @BindView(R.id.btn_getlocation)
    Button btn_getlocation;
    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient client;
    public String lat_str, lng_str;
    public String lat1, lat2, lang1, lang2;
    private Context context;
    private SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        context = LocationActivity.this;
        ButterKnife.bind(this);
        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        btn_getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(LocationActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(LocationActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {

                            TextView textView = findViewById(R.id.txt_location);
                            TextView textView1 = findViewById(R.id.txt_lat);
                            TextView textView2 = findViewById(R.id.txt_lang);
                            // textView.setText(location.toString().trim());
                            textView1.setText("Lat   :" + location.getLatitude());//lat
                            textView2.setText("Lang  :" + location.getLongitude());//lang

                            lat_str = location.getLatitude()+"";
                            lng_str = location.getLongitude()+"";

                            GetMinMaxLatLng(lat_str, lng_str);

                            // textView.setText(la2);
                        }
                    }
                });
            }
        });
    }

    private double[] GetMinMaxLatLng(String lat_str, String lng_str) {


        double values[] = {0.0, 0.0, 0.0, 0.0};
        if (lat_str != null && lng_str != null) {
            double lat = Double.parseDouble(lat_str);
            double lng = Double.parseDouble(lng_str);
            if (lat_str.trim().length() > 0 && lng_str.trim().length() > 0) {
                double kmInLongitudeDegree = 111.320 * Math.cos(lat / 180.0 * Math.PI);
                double deltaLat = C.PROXIMITY_RADIUS_NEAR / 111.1;
                double deltaLong = C.PROXIMITY_RADIUS_NEAR / kmInLongitudeDegree;

                values[0] = lat - deltaLat;
                values[1] = lat + deltaLat;
                values[2] = lng - deltaLong;
                values[3] = lng + deltaLong;

                lat1 = values[0] + "";
                lat2 = values[1] + "";
                lang1 = values[2] + "";
                lang2 = values[3] + "";
            }
        }
        return values;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
