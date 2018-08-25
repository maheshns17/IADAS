package com.pmaptechnotech.iadas.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pmaptechnotech.iadas.R;
import com.pmaptechnotech.iadas.api.Api;
import com.pmaptechnotech.iadas.api.WebServices;
import com.pmaptechnotech.iadas.logics.P;
import com.pmaptechnotech.iadas.logics.U;
import com.pmaptechnotech.iadas.models.UserEditProfileInput;
import com.pmaptechnotech.iadas.models.UserEditProfileResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.edt_address)
    EditText edt_address;
    @BindView(R.id.edt_blood_type)
    EditText edt_blood_type;
    @BindView(R.id.btn_select_image)
    Button btn_select_image;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private Context context;
    private SweetAlertDialog dialog;

    private ImageView imageview;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    Bitmap bm_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = ProfileActivity.this;
        ButterKnife.bind(this);
        btn_select_image = (Button) findViewById(R.id.btn_select_image);
        imageview = (ImageView) findViewById(R.id.image);
        btn_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
      /*  btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, UserDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }

    public void validation() {
        if (bm_image == null) {
            Toast.makeText(context, "Please Capture Image", Toast.LENGTH_LONG).show();
            if (!P.isValidEditText(edt_user_name, "User Name")) return;
            if (!P.isValidEditText(edt_address, "Address")) return;
            if (!P.isValidEditText(edt_blood_type, "Blood Type")) return;
            return;
        }
        UserProfile();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "GALLERY.",
                "CAMERA."};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bm_image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bm_image);
                    Toast.makeText(ProfileActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(bm_image);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            bm_image = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(bm_image);
            saveImage(bm_image);
            Toast.makeText(ProfileActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void UserProfile() {

        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        UserEditProfileInput UserEditProfileInput = new UserEditProfileInput(
                U.user.user_id,
                edt_user_name.getText().toString().trim(),
                edt_blood_type.getText().toString().trim(),
                edt_address.getText().toString().trim(),
                P.BitmapToString(bm_image)
        );
        dialog = P.showBufferDialog(context, "Processing...");
        // btn_Submit.setProgress(1);
        btn_submit.setEnabled(false);
        P.hideSoftKeyboard(ProfileActivity.this);

        //CALL NOW
        webServices.userEditProfile(UserEditProfileInput)
                .enqueue(new Callback<UserEditProfileResult>() {
                    @Override
                    public void onResponse(Call<UserEditProfileResult> call, Response<UserEditProfileResult> response) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (!P.analyseResponse(response)) {
                            // btn_Submit.setProgress(0);
                            btn_submit.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.server_error));
                            return;
                        }
                        UserEditProfileResult result = response.body();
                        if (result.is_success) {
                            // btn_Submit.setProgress(100);
                            P.ShowSuccessDialog(context, getString(R.string.Success), result.msg);
                            Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, UserDetailsActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //  btn_Submit.setProgress(0);
                            btn_submit.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), result.msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserEditProfileResult> call, Throwable t) {
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
