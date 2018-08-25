package com.pmaptechnotech.iadas.models;

/**
 * Created by intel on 02-04-18.
 */

public class TrusteeMobileNumberInput {

    public String user_id;
    public String trustee_type;
    public String trustee_mobile_number;

    // ALT INSERT FOR Constructor//


    public TrusteeMobileNumberInput(String user_id, String trustee_type, String trustee_mobile_number) {
        this.user_id = user_id;
        this.trustee_type = trustee_type;
        this.trustee_mobile_number = trustee_mobile_number;
    }
}
