package com.pmaptechnotech.iadas.models;

/**
 * Created by intel on 03-04-18.
 */

public class MobileNumbersToSendSmsInput {

    public String lat1;
    public String lat2;
    public String lng1;
    public String lng2;

    // ALT INSERT FOR Constructor//

    public MobileNumbersToSendSmsInput(String lat1, String lat2, String lng1, String lng2) {
        this.lat1 = lat1;
        this.lat2 = lat2;
        this.lng1 = lng1;
        this.lng2 = lng2;
    }
}
