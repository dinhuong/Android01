package com.example.mvpdemo2.feature.account;

//import android.content.Context; ko duoc import android vi vi pham mvp

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.mvpdemo2.api.APIService;
import com.example.mvpdemo2.api.RetrofitConfiguration;
import com.example.mvpdemo2.models.GetCreateRequestTokenResponse;
import com.example.mvpdemo2.models.PostCreateSessionRequest;
import com.example.mvpdemo2.models.PostCreateSessionResponse;
import com.example.mvpdemo2.models.PostCreateSessionWithLoginRequest;
import com.example.mvpdemo2.models.PostCreateSessionWithLoginResponse;
import com.example.mvpdemo2.models.share_pref.AccountSharePref;
import com.example.mvpdemo2.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountPresenter implements AccountContract.Presenter {

    AccountContract.View view;
    AccountSharePref accountSharePref;
    APIService service;

    public AccountPresenter(AccountContract.View view, AccountSharePref accountSharePref) {
        this.view = view;
        this.accountSharePref = accountSharePref;
        service= RetrofitConfiguration.getInstance().create(APIService.class);
    }

    private void createRequestToken(String username, String password) {
        view.hideLoadingIndicator();
        Call<GetCreateRequestTokenResponse> call = service.getCreateRequestToken();
        call.enqueue(new Callback<GetCreateRequestTokenResponse>() {
                @Override
                public void onResponse(Call<GetCreateRequestTokenResponse> call, Response<GetCreateRequestTokenResponse> response) {
                    if (response.code() == 200) {
                        createSessionWithLogin(response.body().getRequest_token(), username, password);
                    } else {
                        view.hideLoadingIndicator();
                        view.showErrorFromService(response);
                    }
                }

            @Override
            public void onFailure(Call<GetCreateRequestTokenResponse> call, Throwable t) {
                view.hideLoadingIndicator();
                view.showErrorWhenFailure(t.toString());
            }
        });
    }

    private void createSessionWithLogin(String token, String username, String password) {
        PostCreateSessionWithLoginRequest body = new PostCreateSessionWithLoginRequest();
        body.setUsername(username);
        body.setPassword(password);
        body.setRequest_token(token);

        Call<PostCreateSessionWithLoginResponse> call = service.postCreateSessionWithLogin(body);
        call.enqueue(new Callback<PostCreateSessionWithLoginResponse>() {
            @Override
            public void onResponse(Call<PostCreateSessionWithLoginResponse> call, Response<PostCreateSessionWithLoginResponse> response) {
                if (response.code() == 200) {
                    createSession(response.body().getRequest_token());
                } else {
                    view.hideLoadingIndicator();
                    view.showErrorFromService(response);
                }
            }

            @Override
            public void onFailure(Call<PostCreateSessionWithLoginResponse> call, Throwable t) {
                view.hideLoadingIndicator();
                view.showErrorWhenFailure(t.toString());
            }
        });
    }

    private void createSession(String token) {
        PostCreateSessionRequest body = new PostCreateSessionRequest();
        body.setRequest_token(token);

        Call<PostCreateSessionResponse> call = service.postCreateSession(body);
        call.enqueue(new Callback<PostCreateSessionResponse>() {
            @Override
            public void onResponse(Call<PostCreateSessionResponse> call, Response<PostCreateSessionResponse> response) {
                if (response.code() == 200) {
                    view.hideLoadingIndicator();
                    accountSharePref.saveSessionId(response.body().getSession_id());
                } else {
                    view.hideLoadingIndicator();
                    view.showErrorFromService(response);
                }
            }

            @Override
            public void onFailure(Call<PostCreateSessionResponse> call, Throwable t) {
                view.hideLoadingIndicator();
                view.showErrorWhenFailure(t.toString());
            }
        });
    }

    @Override
    public void getSessionId() {
        if (accountSharePref.getSessionId() == null) {
            view.showLogInSession();
        } else {
            view.showAccountSession();
        }
    }

    @Override
    public void signIn(String username, String password) {
        createRequestToken(username, password);
    }


}
