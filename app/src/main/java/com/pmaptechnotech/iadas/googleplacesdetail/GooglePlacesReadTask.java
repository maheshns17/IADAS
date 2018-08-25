package com.pmaptechnotech.iadas.googleplacesdetail;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null;
    GoogleMap googleMap;
    boolean isNear = false;
    Context context;

    public GooglePlacesReadTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Object... inputObj) {
        try {

            googleMap = (GoogleMap) inputObj[0];
            isNear = (Boolean) inputObj[2];
            //context = (Context) inputObj[3];
            String googlePlacesUrl = (String) inputObj[1];
            Log.d("Http url1", googlePlacesUrl);
            Http http = new Http();
            googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {
            Log.d("Google Place Read Task", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        if (!isNear) {
            PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask(context);
            Object[] toPass = new Object[4];
            toPass[0] = googleMap;
            toPass[1] = result;
            toPass[2] = isNear;
            toPass[3] = context;
            placesDisplayTask.execute(toPass);
        } else {
            PlacesDisplayTask placesDisplayTask1 = new PlacesDisplayTask(context);
            Object[] toPass1 = new Object[4];
            toPass1[0] = googleMap;
            toPass1[1] = result;
            toPass1[2] = isNear;
            toPass1[3] = context;
             placesDisplayTask1.execute(toPass1);

        }
    }
}