package com.pmaptechnotech.iadas.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pmaptechnotech.iadas.FindShakeService;
import com.pmaptechnotech.iadas.R;
import com.pmaptechnotech.iadas.adminside.EmergencyServicesActivity;
import com.pmaptechnotech.iadas.api.Api;
import com.pmaptechnotech.iadas.api.WebServices;
import com.pmaptechnotech.iadas.logics.P;
import com.pmaptechnotech.iadas.logics.U;
import com.pmaptechnotech.iadas.models.UserLoginInput;
import com.pmaptechnotech.iadas.models.UserLoginResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserLoginActivity extends AppCompatActivity {

    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.edt_Password)
    EditText edt_Password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.txt_forgot_password)
    TextView txt_forgot_password;
    private SweetAlertDialog dialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        context = UserLoginActivity.this;
        ButterKnife.bind(this);
        // tool_bar_logo.setImageResource(R.drawable.ic_more_vert_black_24dp);// tool bar icon

        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, UserRegisterActivity.class);
                startActivity(intent);
            }
        });

        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, PasswordCreationActivity.class);
                startActivity(intent);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
       // edt_user_name.setText("mahesh");
       // edt_Password.setText("mahesh");
        Intent intent = new Intent(this, FindShakeService.class);
        startService(intent);

        if (P.isUserLoggedIn(context)) {
            U.user = P.getUserDetails(context);
            Intent intentt = new Intent(context, UserDetailsActivity.class);
            startActivity(intentt);
            finish();
        }
    }

    private void validation() {
        if (!P.isValidEditText(edt_user_name, "User Name")) return;
        if (!P.isValidEditText(edt_Password, "Password")) return;
        userLogin();
    }

    private void userLogin() {

        if (edt_user_name.getText().toString().equals("admin") && (edt_Password.getText().toString().equals("admin"))) {
            Intent intent = new Intent(context, EmergencyServicesActivity.class);
            startActivity(intent);
            finish();
        } else {
            Retrofit retrofit = Api.getRetrofitBuilder(this);
            WebServices webServices = retrofit.create(WebServices.class);

            //PREPARE INPUT/REQUEST PARAMETERS
            UserLoginInput userLoginInput = new UserLoginInput(
                    edt_user_name.getText().toString().trim(),
                    edt_Password.getText().toString().trim()
            );
            dialog = P.showBufferDialog(context, "Processing...");
            //btn_login.setProgress(1);
            btn_login.setEnabled(false);
            P.hideSoftKeyboard(UserLoginActivity.this);


            //CALL NOW
            webServices.userLogin(userLoginInput)
                    .enqueue(new Callback<UserLoginResult>() {
                        @Override
                        public void onResponse(Call<UserLoginResult> call, Response<UserLoginResult> response) {
                            if (dialog.isShowing()) dialog.dismiss();
                            if (!P.analyseResponse(response)) {
                                //btn_login.setProgress(0);
                                btn_login.setEnabled(true);
                                //Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                                P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.server_error));
                                return;
                            }
                            UserLoginResult result = response.body();
                            if (result.is_success) {
                                //btn_login.setProgress(100);
                                U.user = result.user.get(0);
                                P.saveUserLoginData(context, result.user.get(0));
                                Log.d("UserDetailsActivity", "Image: " + P.IMAGE_URL + result.user.get(0).user_image);
                                Intent intent = new Intent(context, UserDetailsActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // btn_login.setProgress(0);
                                btn_login.setEnabled(true);
                                P.ShowErrorDialogAndBeHere(context, getString(R.string.error), result.msg);
                                //Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLoginResult> call, Throwable t) {
                            P.displayNetworkErrorMessage(getApplicationContext(), null, t);
                            t.printStackTrace();
                            if (dialog.isShowing()) dialog.dismiss();
                            // btn_login.setProgress(0);

                            btn_login.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.check_internet_connection));
                        }
                    });



         /*  btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (edt_user_name.getText().toString().equals("admin") && (edt_Password.getText().toString().equals("admin"))) {
                    Intent intent = new Intent(context, EmergencyServicesActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                   // Toast.makeText(context, "incorrect username or password", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, UserDetailsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });*/

        }
    }
}

