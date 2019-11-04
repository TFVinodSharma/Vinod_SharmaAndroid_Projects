package shifoo.com.app.modules.login.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shifoo.com.app.modules.login.model.SignUpResponse;
import shifoo.com.app.modules.login.model.SignupRequest;
import shifoo.com.app.network.manager.OkuRetrofitManager;

public class SignupViewModel extends ViewModel {

    private MutableLiveData<SignUpResponse> signupLiveDataResponse = new MutableLiveData<SignUpResponse>();


    public LiveData<SignUpResponse> registerUser() {
        return signupLiveDataResponse;
    }

    public void doRegisteration(SignupRequest signupRequest) {

        OkuRetrofitManager.OKU_SERVICE.registerUser(signupRequest).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                SignUpResponse signUpResponse = response.body();
                signupLiveDataResponse.setValue(signUpResponse);
            }


            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                SignUpResponse signUpResponse = new SignUpResponse();
                signUpResponse.setMessage(t.getMessage());
                signupLiveDataResponse.setValue(signUpResponse);
            }
        });
    }
}
