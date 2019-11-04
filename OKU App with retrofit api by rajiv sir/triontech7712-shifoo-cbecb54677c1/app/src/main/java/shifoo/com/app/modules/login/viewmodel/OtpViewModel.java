package shifoo.com.app.modules.login.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shifoo.com.app.modules.login.model.OtpRequest;
import shifoo.com.app.modules.login.model.OtpResponse;

import shifoo.com.app.network.manager.OkuRetrofitManager;

public class OtpViewModel extends ViewModel {

    private MutableLiveData<OtpResponse> otpResponseMutableLiveData = new MutableLiveData<OtpResponse>();

    public LiveData<OtpResponse> verifyOtp() {
        return otpResponseMutableLiveData;
    }


    public void VerifyOtp(OtpRequest otpRequest) {

        OkuRetrofitManager.OKU_SERVICE.verifyOtp(otpRequest).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                System.out.println("Otp Response   "  + response);
                OtpResponse otpResponse = response.body();
                otpResponseMutableLiveData.setValue(otpResponse);
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                System.out.println("Otp Failure   " + call.toString());
                OtpResponse otpResponse = new OtpResponse();
                otpResponse.setMessage(t.getMessage());
                otpResponseMutableLiveData.setValue(otpResponse);
            }


        });
    }


}
