package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StorageActivity extends AppCompatActivity {
    private static final String TAG = "StorageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_choose_image)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.setType("image/*"); // video/*
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setData(Uri.parse("mailto:"));
//        intent.setType("text/plain");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                Log.d(TAG, "onActivityResult: " + data.getData());

                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReference();
                StorageReference imageRef = storageReference.child("images");
                StorageReference testRef = imageRef.child(Calendar.getInstance().getTimeInMillis() + ".jpg");

                UploadTask uploadTask = testRef.putFile(data.getData());
                uploadTask
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d(TAG, "onProgress: getBytesTransferred " + taskSnapshot.getBytesTransferred());
                                Log.d(TAG, "onProgress: getTotalByteCount " + taskSnapshot.getTotalByteCount());
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(StorageActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(StorageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
}
