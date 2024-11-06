package com.example.social_medial_app.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Utils {

    public static Task<String> uploadImage(Uri uri, Context context, String folderName) {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();
        ProgressDialog progressDialog = new ProgressDialog(context);  // Use context here
        progressDialog.setTitle("Uploading ...");
        progressDialog.show();  // Make sure to show the progress dialog
        FirebaseStorage.getInstance().getReference(folderName)
                .child(UUID.randomUUID().toString())
                .putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUri) {
                                        taskCompletionSource.setResult(downloadUri.toString());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        taskCompletionSource.setException(e);
                                    }
                                });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        long uploadedValue = (snapshot.getBytesTransferred() * 100) / snapshot.getTotalByteCount();  // Calculate percentage correctly
                        progressDialog.setMessage("Uploaded " + uploadedValue + "%");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletionSource.setException(e);
                    }
                });

        return taskCompletionSource.getTask();
    }
    public static Task<String> uploadVideo(Uri uri, Context context, String folderName) {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();
        ProgressDialog progressDialog = new ProgressDialog(context);  // Use context here
        progressDialog.setTitle("Uploading ...");
        progressDialog.show();  // Make sure to show the progress dialog

        FirebaseStorage.getInstance().getReference(folderName)
                .child(UUID.randomUUID().toString())
                .putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUri) {
                                        taskCompletionSource.setResult(downloadUri.toString());
                                        progressDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        taskCompletionSource.setException(e);
                                        progressDialog.dismiss();  // Dismiss progress dialog on failure
                                    }
                                });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        long uploadedValue = (snapshot.getBytesTransferred() * 100) / snapshot.getTotalByteCount();  // Calculate percentage correctly
                        progressDialog.setMessage("Uploaded " + uploadedValue + "%");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletionSource.setException(e);
                        progressDialog.dismiss();  // Dismiss progress dialog on failure
                    }
                });

        return taskCompletionSource.getTask();
    }


}
