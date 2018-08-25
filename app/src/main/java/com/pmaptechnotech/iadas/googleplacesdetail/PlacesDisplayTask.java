package com.pmaptechnotech.iadas.googleplacesdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pmaptechnotech.iadas.R;
import com.pmaptechnotech.iadas.logics.C;
import com.pmaptechnotech.iadas.logics.P;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    JSONObject googlePlacesJson;
    GoogleMap googleMap;
    boolean isNear = false;
    Context context;

    public PlacesDisplayTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googleMap = (GoogleMap) inputObj[0];
            isNear = (Boolean) inputObj[2];
            //context = (Context) inputObj[3];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        P.LogD("Places : isNear" + isNear);
        /*if (isNear) {
            P.LogD("Places : 1");
            if (list != null) {
                P.LogD("Places : 2");
                if (list.size() > 0) {
                    P.LogD("Places : 3");
                    HashMap<String, String> googlePlace = list.get(0);
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String placeName = googlePlace.get("place_name");
                    String vicinity = googlePlace.get("vicinity");
                    LatLng latLng = new LatLng(lat, lng);
                    if(context !=null) {
                        //Intent intent = new Intent(context, MyIntentService.class);
                        //intent.putExtra("desc", placeName + " " + vicinity);
                        //intent.putExtra("lat", lat+"");
                        //intent.putExtra("lng", lng+"");
                        //context.startService(intent);
                    }
                    P.LogD("Places : 4");
                }
            }
        } else {*/
            googleMap.clear();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    HashMap<String, String> googlePlace = list.get(i);
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    String placeName = googlePlace.get("place_name");
                    String vicinity = googlePlace.get("vicinity");
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    googleMap.addMarker(markerOptions);
                }
            }
           /* MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(C.sourceLatLng);
            markerOptions.title("Source");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_source));
            googleMap.addMarker(markerOptions);

            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.position(C.destLatLng);
            markerOptions1.title("Destination");
            markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_destination));
            googleMap.addMarker(markerOptions1);*/

           /* final GMapV2Direction md = new GMapV2Direction();

            class Doc extends AsyncTask<Object, Integer, PolylineOptions> {

                @Override
                protected PolylineOptions doInBackground(Object... params) {
                    Document doc = md.getDocument(C.sourceLatLng, C.destLatLng,
                            GMapV2Direction.MODE_DRIVING);
                    ArrayList<LatLng> directionPoint = md.getDirection(doc);
                    PolylineOptions rectLine = new PolylineOptions().width(3).color(
                            Color.RED);

                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    return rectLine;
                }

                @Override
                protected void onPostExecute(PolylineOptions polylineOptions) {
                    super.onPostExecute(polylineOptions);
                    polylineOptions.width(10);
                    Polyline polylin = googleMap.addPolyline(polylineOptions);
                }
            }
            ;*/

            //new Doc().execute();
        }
    //}


}

