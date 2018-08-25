package com.pmaptechnotech.iadas.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pmaptechnotech.iadas.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ContactTypesActivity extends AppCompatActivity {

    @BindView(R.id.btn_friends)
    Button btn_friends;
    @BindView(R.id.btn_relatives)
    Button btn_relatives;

    private Context context;
    private SweetAlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_types);

        context = ContactTypesActivity.this;
        ButterKnife.bind(this);


        btn_friends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, ContactNumberActivity.class);
                intent.putExtra("trustee_type", "Friends");
                startActivity(intent);
            }
        });

        btn_relatives.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, ContactNumberActivity.class);
                intent.putExtra("trustee_type", "Relatives");
                startActivity(intent);
            }
        });
    }
}
