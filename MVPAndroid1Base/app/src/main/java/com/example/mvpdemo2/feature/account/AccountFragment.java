package com.example.mvpdemo2.feature.account;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mvpdemo2.BaseFragment;
import com.example.mvpdemo2.R;
import com.example.mvpdemo2.models.share_pref.AccountSharePref;
import com.example.mvpdemo2.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends BaseFragment implements AccountContract.View {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_sign_in)
    TextView tvSignIn;
    @BindView(R.id.ll_sign_in)
    LinearLayout llSignIn;
    @BindView(R.id.tv_sign_out)
    TextView tvSignOut;
    @BindView(R.id.ll_account)
    LinearLayout llAccount;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    public AccountFragment() {
        // Required empty public constructor
    }


    @OnClick({R.id.tv_sign_in, R.id.tv_sign_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_in:
                presenter.signIn(etUsername.getText().toString(),etPassword.getText().toString());
                break;
            case R.id.tv_sign_out:
                break;
        }
    }

    AccountContract.Presenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account;
    }

    @Override
    protected void onInit() {
        presenter = new AccountPresenter(this, new AccountSharePref(getContext()));
        presenter.getSessionId();
    }

    @Override
    public void showLogInSession() {
        llAccount.setVisibility(View.VISIBLE);
        llSignIn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAccountSession() {
        llSignIn.setVisibility(View.INVISIBLE);
        llAccount.setVisibility(View.VISIBLE);
    }


    @Override
    public void showLoadingIndicator() {
        llLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        llLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrorFromService(Response response) {
        Utils.showErrorFromServer(response, getContext());
    }

    @Override
    public void showErrorWhenFailure(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
