package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    
    Button onbackclick_btn;
    Button confirmprofile_btn;
    EditText old_password_et, new_password_et, confirm_password_et;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passwod);

        onbackclick_btn = findViewById(R.id.btn_back);
        confirmprofile_btn = findViewById(R.id.confirm_btn);
        old_password_et = findViewById(R.id.old_password_et);
        new_password_et = findViewById(R.id.new_password_et);
        confirm_password_et = findViewById(R.id.confirm_new_password_et);

        onback_click();
        onconfirmclick();
    }
    
    public void onback_click(){
        onbackclick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onconfirmclick(){
        confirmprofile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (old_password_et.getText().toString().isEmpty() || new_password_et.getText().toString().isEmpty() ||
                confirm_password_et.getText().toString().isEmpty()){
                    showAlert("Please fill all fields");
                }else {
                    if (new_password_et.getText().toString().equals(confirm_password_et.getText().toString())){

                    }else {
                        showAlert("New Password does not match Confirm Password");
                    }
                }
            }
        });
    }

//    private void updatePassword(){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//// Get auth credentials from the user for re-authentication. The example below shows
//// email and password credentials but there are multiple possible providers,
//// such as GoogleAuthProvider or FacebookAuthProvider.
//        AuthCredential credential = EmailAuthProvider
//                .getCredential(user.getEmail(), "password1234");
//
//// Prompt the user to re-provide their sign-in credentials
//        user.reauthenticate(credential)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            user.updatePassword(new_password_et.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), "Password Not Updated", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        } else {
//                            Log.d("TAG", "Error auth failed")
//                        }
//                    }
//                });
//    }

    private void showAlert(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}