package com.pmaptechnotech.iadas.models;

/**
 * Created by intel on 12-02-18.
 */

public class UserLoginInput {
    public String username;
    public String password;

    // ALT INSERT FOR Constructor//

    public UserLoginInput(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

