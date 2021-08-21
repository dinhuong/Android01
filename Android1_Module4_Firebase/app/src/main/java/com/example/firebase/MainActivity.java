package com.example.firebase;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_info)
    TextView tvInfo;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        updateUI();
    }

    private void updateUI() {
        if (firebaseUser == null) {
            tvInfo.setText("Not logged in yet");
        } else {
            tvInfo.setText(firebaseUser.getEmail());
        }
    }

    @OnClick({R.id.bt_sign_up, R.id.bt_sign_in, R.id.bt_sign_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sign_up:
                firebaseAuth
                        .createUserWithEmailAndPassword(
                                etEmail.getText().toString(),
                                etPassword.getText().toString()
                        )
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    firebaseUser = task.getResult().getUser();
                                    updateUI();
                                } else {
                                    Toast.makeText(
                                            MainActivity.this,
                                            task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_sign_in:
                firebaseAuth
                        .signInWithEmailAndPassword(
                                etEmail.getText().toString(),
                                etPassword.getText().toString()
                        )
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    firebaseUser = task.getResult().getUser();
                                    updateUI();
                                } else {
                                    Toast.makeText(
                                            MainActivity.this,
                                            task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_sign_out:
                firebaseAuth.signOut();
                firebaseUser = firebaseAuth.getCurrentUser();
                updateUI();
        }
    }
}
