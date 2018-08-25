package com.pmaptechnotech.iadas.models;

/**
 * Created by intel on 03-04-18.
 */

public class ForgotPasswordInput {
    public String mobile_number;
    public String password;

    // ALT INSERT FOR Constructor//

    public ForgotPasswordInput(String mobile_number, String password) {
        this.mobile_number = mobile_number;
        this.password = password;
    }
}

