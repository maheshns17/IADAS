package com.pmaptechnotech.iadas.api;

import com.pmaptechnotech.iadas.models.AdminUploadHospitalInput;
import com.pmaptechnotech.iadas.models.AdminUploadHospitalResult;
import com.pmaptechnotech.iadas.models.ForgotPasswordInput;
import com.pmaptechnotech.iadas.models.ForgotPasswordResult;
import com.pmaptechnotech.iadas.models.GetMobileNumbersToSendSmsInput;
import com.pmaptechnotech.iadas.models.GetMobileNumbersToSendSmsResult;
import com.pmaptechnotech.iadas.models.TrusteeMobileNumberInput;
import com.pmaptechnotech.iadas.models.TrusteeMobileNumberResult;
import com.pmaptechnotech.iadas.models.UserEditProfileInput;
import com.pmaptechnotech.iadas.models.UserEditProfileResult;
import com.pmaptechnotech.iadas.models.UserLoginInput;
import com.pmaptechnotech.iadas.models.UserLoginResult;
import com.pmaptechnotech.iadas.models.UserRegisterInput;
import com.pmaptechnotech.iadas.models.UserRegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface WebServices {

    @POST("UserLogin_c/userLogin")
    Call<UserLoginResult> userLogin(@Body UserLoginInput input);

    @POST("UserRegister_c/userRegister")
    Call<UserRegisterResult> userRegister(@Body UserRegisterInput input);

    @POST("UserForgotPassword_c/changePassword")
    Call<ForgotPasswordResult> changePassword(@Body ForgotPasswordInput input);

    @POST("UserEditProfile_c/userEditProfile")
    Call<UserEditProfileResult> userEditProfile(@Body UserEditProfileInput input);

    @POST("AdminUploadHospital_c/uploadHospital")
    Call<AdminUploadHospitalResult> uploadHospital(@Body AdminUploadHospitalInput input);

    @POST("UserUploadTrusteeMobileNumber_c/uploadTrusteeMobileNumber")
    Call<TrusteeMobileNumberResult> uploadTrusteeMobileNumber(@Body TrusteeMobileNumberInput input);

    @POST("GetMobileNumbersToSendSMS_c/getMobileNumbersToSendSms")
    Call<GetMobileNumbersToSendSmsResult> getMobileNumbersToSendSms(@Body GetMobileNumbersToSendSmsInput input);

}
