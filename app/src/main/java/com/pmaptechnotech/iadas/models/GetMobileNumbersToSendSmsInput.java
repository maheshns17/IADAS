package com.pmaptechnotech.iadas.models;

public class GetMobileNumbersToSendSmsInput {
    public String user_id;
    public String lat1;
    public String lat2;
    public String lng1;
    public String lng2;


    public GetMobileNumbersToSendSmsInput(String user_id, String lat1, String lat2, String lng1, String lng2) {
        this.user_id = user_id;
        this.lat1 = lat1;
        this.lat2 = lat2;
        this.lng1 = lng1;
        this.lng2 = lng2;
    }
}
