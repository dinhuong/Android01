package com.example.mvpdemo2.feature.account;

import android.content.Context;

import retrofit2.Response;

public class AccountContract {
    interface View {
        void showAccountSession();

        void showLogInSession();

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showErrorFromService(Response response);

        void showErrorWhenFailure(String error);

    }

    interface Presenter {
        void getSessionId();

        void signIn(String username, String password);
    }


}
