package shifoo.com.app.modules.login.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignupRequest implements Serializable {

    @SerializedName("PhoneNumber")
    private String PhoneNumber;

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
