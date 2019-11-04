package shifoo.com.app.modules.login.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OtpRequest implements Serializable {

    @SerializedName("PhoneNumber")
    private String PhoneNumber;

    @SerializedName("VerificationOtp")
    private  String VerificationOtp;

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getVerificationOtp() {
        return VerificationOtp;
    }

    public void setVerificationOtp(String verificationOtp) {
        VerificationOtp = verificationOtp;
    }
}
