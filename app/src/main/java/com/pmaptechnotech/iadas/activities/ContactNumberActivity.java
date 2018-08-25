package com.pmaptechnotech.iadas.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.pmaptechnotech.iadas.R;
import com.pmaptechnotech.iadas.api.Api;
import com.pmaptechnotech.iadas.api.WebServices;
import com.pmaptechnotech.iadas.logics.P;
import com.pmaptechnotech.iadas.models.TrusteeMobileNumberInput;
import com.pmaptechnotech.iadas.models.TrusteeMobileNumberResult;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactNumberActivity extends AppCompatActivity {
    @BindView(R.id.edt_number)
    EditText edt_number;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private Context context;
    private SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_number);
        context = ContactNumberActivity.this;
        ButterKnife.bind(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, ContactNumberActivity.class);
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
        if (!P.isValidEditText(edt_number, "Number")) return;
        Register();
    }
    private void Register() {

        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        TrusteeMobileNumberInput uploadDepartmentsInput = new TrusteeMobileNumberInput(
                P.getUserDetails(context).user_id,
                getIntent().getStringExtra("trustee_type"),
                edt_number.getText().toString().trim()
        );

        dialog = P.showBufferDialog(context, "Processing...");
        // btn_Submit.setProgress(1);
        btn_submit.setEnabled(false);
        P.hideSoftKeyboard(ContactNumberActivity.this);

        //CALL NOW
        webServices.uploadTrusteeMobileNumber(uploadDepartmentsInput)
                .enqueue(new Callback<TrusteeMobileNumberResult>() {
                    @Override
                    public void onResponse(Call<TrusteeMobileNumberResult> call, Response<TrusteeMobileNumberResult> response) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (!P.analyseResponse(response)) {
                            // btn_Submit.setProgress(0);
                            btn_submit.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.server_error));
                            return;
                        }
                        TrusteeMobileNumberResult result = response.body();
                        if (result.is_success) {
                            // btn_Submit.setProgress(100);
                            P.ShowSuccessDialog(context, getString(R.string.Success), result.msg);
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                         /* Intent intent = new Intent(context, UserLoginActivity.class);
                            startActivity(intent);
                            finish();*/
                        } else {
                            //  btn_Submit.setProgress(0);
                            btn_submit.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), result.msg);
                        }

                    }

                    @Override
                    public void onFailure(Call<TrusteeMobileNumberResult> call, Throwable t) {
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