package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.betterme.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText mName, mMobile, mEmail, mPassword ;
    private Button mcreateAccountBtn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USER = "user";
    private User user;
    ProgressDialog progressDialog;


    Button create_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        create_account = findViewById(R.id.signup_create_btn);



        mName = findViewById(R.id.signup_name_et);
        mMobile = findViewById(R.id.signup_mobile_et);
        mEmail = findViewById(R.id.signup_email_et);
        mPassword = findViewById(R.id.signup_password_et);
        mcreateAccountBtn = findViewById(R.id.signup_create_btn);

        mAuth = FirebaseAuth.getInstance();



        mcreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

    }

    private void createUser(){
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("loading");
        progressDialog.show();

        String name = mName.getText().toString();
        String mobile = mMobile.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();


        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!password.isEmpty()){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    user = new User(name,mobile,email,password,0, FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.hide();

                                                Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                                finish();
                                            }else {
                                                progressDialog.hide();

                                                Toast.makeText(SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }else {
                                    progressDialog.hide();

                                    Toast.makeText(SignUpActivity.this, "Task 1 failed Successfully", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                mPassword.setError("Empty Fields are not Allowed");
            }
        }else if (email.isEmpty()){
            mEmail.setError("Empty Fields are not Allowed");
        }else{
            mEmail.setError("Please Enter Correct Email");
        }
    }



    public void onCreateClick(View view) {
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    public void onBackClick(View view) {
        finish();
    }

}