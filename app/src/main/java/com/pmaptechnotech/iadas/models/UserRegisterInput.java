package com.pmaptechnotech.iadas.models;

/**
 * Created by intel on 10-02-18.
 */

public class UserRegisterInput {

    public String username;
    public String mobile_number;
    public String password;

// ALT INSERT FOR Constructor//

    public UserRegisterInput(String username, String mobile_number, String password) {
        this.username = username;
        this.mobile_number = mobile_number;
        this.password = password;
    }
}


