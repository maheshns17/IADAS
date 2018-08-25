package com.pmaptechnotech.iadas.models;

/**
 * Created by intel on 02-04-18.
 */

public class UserEditProfileInput {

    public String user_id;
    public String username;
    public String blood_type;
    public String address;
    public String user_image;

    // ALT INSERT FOR Constructor//


    public UserEditProfileInput(String user_id, String username, String blood_type, String address, String user_image) {
        this.user_id = user_id;
        this.username = username;
        this.blood_type = blood_type;
        this.address = address;
        this.user_image = user_image;
    }
}
