package com.pmaptechnotech.iadas.googleplacesdetail;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pmaptechnotech.iadas.R;
import com.pmaptechnotech.iadas.logics.C;
import com.pmaptechnotech.iadas.logics.P;

public class GooglePlacesActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback {

    GoogleMap googleMap;
    EditText placeText;
    double latitude = 0;
    double longitude = 0;

    Button btnFind;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_google_places);
        context = GooglePlacesActivity.this;
        placeText = (EditText) findViewById(R.id.placeText);
        placeText.setText(getIntent().getStringExtra("geo_place_type"));

        btnFind = (Button) findViewById(R.id.btnFind);
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        fragment.getMapAsync(this);

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

        btnFind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask(context);
                //FAR
                Object[] toPass = new Object[4];
                toPass[0] = googleMap;
                toPass[1] = getGooglePlacesUrl(C.PROXIMITY_RADIUS_NEAR);
                toPass[2] = false;//FALSE=FAR, TRUE=NEAR (1 KM)
                toPass[3] = context;
                googlePlacesReadTask.execute(toPass);

                //NEAR
                GooglePlacesReadTask googlePlacesReadTask1 = new GooglePlacesReadTask(context);
                Object[] toPass1 = new Object[4];
                toPass1[0] = googleMap;
                toPass1[1] = getGooglePlacesUrl(C.PROXIMITY_RADIUS_NEAR);
                toPass1[2] = true;//FALSE=FAR, TRUE=NEAR (1 KM)
                toPass1[3] = context;
                googlePlacesReadTask1.execute(toPass1);

            }
        });

        //String source = getIntent().getStringExtra("source");
        //String dest = getIntent().getStringExtra("destination");
        //C.sourceLatLng = P.getLocationFromAddress(this, source);
        //C.destLatLng = P.getLocationFromAddress(this, dest);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // Enabling MyLocation Layer of Google Map
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
        this.googleMap.setMyLocationEnabled(true);
    }
    public String getGooglePlacesUrl(int radius) {

        String type = placeText.getText().toString();
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + radius);
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&sensor=false");
        googlePlacesUrl.append("&key=" + getResources().getString(R.string.google_maps_key));
        return googlePlacesUrl.toString();
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        P.LogD("GooglePlacesActivity : onLocationChanged ");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        if(googleMap!=null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            P.LogD("GooglePlacesActivity : Location Changed ");
            btnFind.performClick();
        }else{
            P.LogD("GooglePlacesActivity : Map not loaded ");
        }
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