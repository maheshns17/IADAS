package com.pmaptechnotech.iadas.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.pmaptechnotech.iadas.R;
import com.pmaptechnotech.iadas.logics.P;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PasswordCreationActivity extends AppCompatActivity {

    @BindView(R.id.edt_mobile_number)
    EditText edt_mobile_number;
    @BindView(R.id.edt_otp)
    EditText edt_otp;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.btn_next)
    Button btn_next;
    private Context context;
    private SweetAlertDialog dialog;
    private String OTP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_creation);
        context = PasswordCreationActivity.this;
        ButterKnife.bind(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!P.isValidEditText(edt_mobile_number, "OTP")) return;
                OTP = P.generateOTP();
                P.sendSMS(edt_mobile_number.getText().toString().trim(), "IADAS OTP is : " + OTP);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                validateOTP();
            }
        });
    }

    public void validateOTP() {
        if (!P.isValidEditText(edt_otp, "OTP")) return;
        String enteredOTP = edt_otp.getText().toString().trim();
        if (enteredOTP.equalsIgnoreCase(OTP)) {
            Intent intent = new Intent(context, NewPasswordActivity.class);
            startActivity(intent);
            finish();
        } else {
            edt_otp.setError("Wrong OTP");
        }

    }

}
