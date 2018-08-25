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
import com.pmaptechnotech.iadas.models.ForgotPasswordInput;
import com.pmaptechnotech.iadas.models.ForgotPasswordResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewPasswordActivity extends AppCompatActivity {
    @BindView(R.id.edt_mobile_number)
    EditText edt_mobile_number;
    @BindView(R.id.edt_new_password)
    EditText edt_new_password;
    @BindView(R.id.edt_confirm_password)
    EditText edt_confirm_password;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private Context context;
    private SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        context = NewPasswordActivity.this;
        ButterKnife.bind(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, UserLoginActivity.class);
                startActivity(intent);
                finish();
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
        if (!P.isValidEditText(edt_mobile_number, "mobile number")) return;
        if (!P.isValidEditText(edt_new_password, "new password")) return;
        if (!P.isValidEditText(edt_confirm_password, "confirm password")) return;
        Password();
    }

    private void Password() {

        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        ForgotPasswordInput ForgotPasswordInput = new ForgotPasswordInput(
                edt_mobile_number.getText().toString().trim(),
                edt_confirm_password.getText().toString().trim()
        );

        dialog = P.showBufferDialog(context, "Processing...");
        // btn_Submit.setProgress(1);
        btn_submit.setEnabled(false);
        P.hideSoftKeyboard(NewPasswordActivity.this);

        //CALL NOW
        webServices.changePassword(ForgotPasswordInput)
                .enqueue(new Callback<ForgotPasswordResult>() {
                    @Override
                    public void onResponse(Call<ForgotPasswordResult> call, Response<ForgotPasswordResult> response) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (!P.analyseResponse(response)) {
                            // btn_Submit.setProgress(0);
                            btn_submit.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.server_error));
                            return;
                        }
                        ForgotPasswordResult result = response.body();
                        if (result.is_success) {
                            // btn_Submit.setProgress(100);
                            P.ShowSuccessDialog(context, getString(R.string.Success), result.msg);
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, UserLoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //  btn_Submit.setProgress(0);
                            btn_submit.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), result.msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<ForgotPasswordResult> call, Throwable t) {
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