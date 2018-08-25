package com.pmaptechnotech.iadas.adminside;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pmaptechnotech.iadas.R;
import com.pmaptechnotech.iadas.activities.UserLoginActivity;
import com.pmaptechnotech.iadas.api.Api;
import com.pmaptechnotech.iadas.api.WebServices;
import com.pmaptechnotech.iadas.logics.P;
import com.pmaptechnotech.iadas.models.AdminUploadHospitalInput;
import com.pmaptechnotech.iadas.models.AdminUploadHospitalResult;
import com.pmaptechnotech.iadas.models.MobileNumbersToSendSmsInput;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EmergencyServicesActivity extends AppCompatActivity {
    @BindView(R.id.edt_hospital_name)
    EditText edt_hospital_name;
    @BindView(R.id.edt_phone)
    EditText edt_phone;
    @BindView(R.id.edt_latitude)
    EditText edt_latitude;
    @BindView(R.id.edt_longitude)
    EditText edt_longitude;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private Context context;
    private SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_services);

        context = EmergencyServicesActivity.this;
        ButterKnife.bind(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, EmergencyServicesActivity.class);
                startActivity(intent);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }
    private void validation() {
        if (!P.isValidEditText(edt_hospital_name, "Hospital Name")) return;
        if (!P.isValidEditText(edt_phone, "Phone")) return;
        if (!P.isValidEditText(edt_latitude, "latitude")) return;
        if (!P.isValidEditText(edt_longitude, "longitude")) return;

        Hospitals();
    }

    private void Hospitals() {

        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        AdminUploadHospitalInput AdminUploadHospitalInput = new AdminUploadHospitalInput(
                //"Agriculture",
                edt_hospital_name.getText().toString().trim(),
                edt_phone.getText().toString().trim(),
                edt_latitude.getText().toString().trim(),
                edt_longitude.getText().toString().trim()
        );
        dialog = P.showBufferDialog(context, "Processing...");
        // btn_Submit.setProgress(1);
        btn_submit.setEnabled(false);
        P.hideSoftKeyboard(EmergencyServicesActivity.this);

        //CALL NOW
        webServices.uploadHospital(AdminUploadHospitalInput)
                .enqueue(new Callback<AdminUploadHospitalResult>() {
                    @Override
                    public void onResponse(Call<AdminUploadHospitalResult> call, Response<AdminUploadHospitalResult> response) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (!P.analyseResponse(response)) {
                            // btn_Submit.setProgress(0);
                            btn_submit.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.server_error));
                            return;
                        }
                        AdminUploadHospitalResult result = response.body();
                        if (result.is_success) {
                            // btn_Submit.setProgress(100);
                            P.ShowSuccessDialog(context, getString(R.string.Success), result.msg);
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, EmergencyServicesActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //  btn_Submit.setProgress(0);
                            btn_submit.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), result.msg);
                        }
                    }
                    @Override
                    public void onFailure(Call<AdminUploadHospitalResult> call, Throwable t) {
                        P.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                        if (dialog.isShowing()) dialog.dismiss();
                        //  btn_Submit.setProgress(0);
                        btn_submit.setEnabled(true);
                        P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.check_internet_connection));

                    }
                });
    }
}
