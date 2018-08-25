package com.pmaptechnotech.iadas.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pmaptechnotech.iadas.R;
import com.pmaptechnotech.iadas.googleplacesdetail.GooglePlacesActivity;
import com.pmaptechnotech.iadas.logics.P;
import com.pmaptechnotech.iadas.logics.U;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.btn_contact_list)
    Button btn_contact_list;
    @BindView(R.id.btn_police_station)
    Button btn_police_station;
    @BindView(R.id.btn_fire_station)
    Button btn_fire_station;
    @BindView(R.id.btn_ambulance)
    Button btn_ambulance;
    @BindView(R.id.btn_pharmacy)
    Button btn_pharmacy;
    @BindView(R.id.btn_hospitals)
    Button btn_hospitals;
    @BindView(R.id.btn_mobile_falls)
    Button btn_mobile_falls;
    @BindView(R.id.rbtn_on)
    RadioButton rbtn_on;
    @BindView(R.id.rbtn_off)
    RadioButton rbtn_off;
    TextView slideshow, gallery;

    private Context context;
    private SweetAlertDialog dialog;
    private ImageView img_user_profile;
    private TextView txt_name;
    private TextView txt_address;
    private TextView txt_blood_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = UserDetailsActivity.this;
        ButterKnife.bind(this);

        // nav_user_name.setText(U.user.username);

        btn_contact_list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, ContactTypesActivity.class);
                startActivity(intent);
            }
        });

        btn_police_station.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, GooglePlacesActivity.class);
                intent.putExtra("geo_place_type", "police");
                startActivity(intent);
            }
        });

        btn_fire_station.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, GooglePlacesActivity.class);
                intent.putExtra("geo_place_type", "fire_station");
                startActivity(intent);
            }
        });

        btn_ambulance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, GooglePlacesActivity.class);
                intent.putExtra("geo_place_type", "hospital");
                startActivity(intent);
            }
        });

        btn_pharmacy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, GooglePlacesActivity.class);
                intent.putExtra("geo_place_type", "pharmacy");
                startActivity(intent);
            }
        });

        btn_hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GooglePlacesActivity.class);
                intent.putExtra("geo_place_type", "hospital");
                startActivity(intent);
            }
        });


        btn_mobile_falls.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, AlertActivity.class);
                startActivity(intent);
            }
        });

        rbtn_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P.saveOnOff(context, true);
            }
        });
        rbtn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P.saveOnOff(context, false);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // NavigationView Image
        View hView = navigationView.getHeaderView(0);
        img_user_profile = (ImageView) hView.findViewById(R.id.img_user_profile);
        img_user_profile.setImageResource(R.drawable.common_full_open_on_phone);
        Log.d("UserDetailsActivity", "Image: " + P.IMAGE_URL + U.user.user_image);
        Glide.with(context).load(P.IMAGE_URL + U.user.user_image).into(img_user_profile);

        txt_name = (TextView) hView.findViewById(R.id.txt_name);
        txt_name.setText("Name : " + U.user.user_username);
        txt_address = (TextView) hView.findViewById(R.id.txt_address);
        txt_address.setText("Address : " + U.user.user_address);
        txt_blood_type = (TextView) hView.findViewById(R.id.txt_blood_type);
        txt_blood_type.setText("Blood Group : " + U.user.user_blood_type);

        navigationView.setNavigationItemSelectedListener(this);
        checkOnOff();
    }

    private void checkOnOff() {
        if(P.getUserDetails(context).on_off){
            rbtn_on.setChecked(true);
            rbtn_off.setChecked(false);
        }else{
            rbtn_on.setChecked(false);
            rbtn_off.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_profile) {
            Intent intent = new Intent(UserDetailsActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            P.userLogout(context);
            Intent intent = new Intent(UserDetailsActivity.this, UserLoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

      /*  if (id == R.id.nav_user_name) {

        }

        else if (id == R.id.nav_address) {


        }
        else if (id == R.id.nav_blood_type) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
