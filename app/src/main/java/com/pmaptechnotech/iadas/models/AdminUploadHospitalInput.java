package com.pmaptechnotech.iadas.models;

/**
 * Created by intel on 03-04-18.
 */

public class AdminUploadHospitalInput {

    public String hospital_name;
    public String hospital_mobile_number;
    public String hospital_lat;
    public String hospital_lng;

    // ALT INSERT FOR Constructor//

    public AdminUploadHospitalInput(String hospital_name, String hospital_mobile_number, String hospital_lat, String hospital_lng) {
        this.hospital_name = hospital_name;
        this.hospital_mobile_number = hospital_mobile_number;
        this.hospital_lat = hospital_lat;
        this.hospital_lng = hospital_lng;
    }
}
