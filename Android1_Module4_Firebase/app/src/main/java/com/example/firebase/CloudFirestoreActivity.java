package com.example.firebase;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CloudFirestoreActivity extends AppCompatActivity {
    private static final String TAG = "CloudFirestoreActivity";
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_api)
    EditText etApi;
    @BindView(R.id.tv_data)
    TextView tvData;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_firestore);
        ButterKnife.bind(this);

        firebaseFirestore = FirebaseFirestore.getInstance();

        //get data once
//        firebaseFirestore.collection("android")
////                .whereGreaterThan("api", 20)
//                .limit(2)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
////                            Log.d(TAG, "onComplete: " + task.getResult());
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                Log.d(TAG, "onComplete: " + documentSnapshot.getData());
//                            }
//                        } else {
//                            Toast.makeText(
//                                    CloudFirestoreActivity.this,
//                                    task.getException().getMessage(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

        //get data real-time
        firebaseFirestore.collection("android")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(CloudFirestoreActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<AndroidModel> androidModels = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Log.d(TAG, "onEvent: " + documentSnapshot.getData());
                            AndroidModel androidModel = new AndroidModel(
                                    documentSnapshot.getData().get("name").toString(),
                                    Integer.parseInt(documentSnapshot.getData().get("api").toString())
                            );
                            androidModels.add(androidModel);
                        }
                        tvData.setText(androidModels.toString());
                    }
                });
    }

    @OnClick(R.id.bt_add)
    public void onViewClicked() {
        AndroidModel androidModel = new AndroidModel(
                etName.getText().toString(),
                Integer.parseInt(etApi.getText().toString())
        );
        firebaseFirestore.collection("android")
                .add(androidModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CloudFirestoreActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CloudFirestoreActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
