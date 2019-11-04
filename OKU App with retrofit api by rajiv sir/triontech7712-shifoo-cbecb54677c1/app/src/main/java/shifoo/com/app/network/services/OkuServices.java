package shifoo.com.app.network.services;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import shifoo.com.app.modules.login.model.OtpRequest;
import shifoo.com.app.modules.login.model.OtpResponse;
import shifoo.com.app.modules.login.model.SignUpResponse;
import shifoo.com.app.modules.login.model.SignupRequest;

public interface OkuServices {

    @POST("/api/account/getOtp")
    Call<SignUpResponse> registerUser(@Body SignupRequest signupRequest);

    @POST("/api/account/VerifyOtp")
    Call<OtpResponse> verifyOtp(@Body OtpRequest otpRequest);




}
