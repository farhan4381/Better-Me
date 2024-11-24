package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class InstructorLoginActivity extends AppCompatActivity {


    Button forgot_btn;
    Button login_btn;
    Button oncreateaccount_btn;
    Button onbackclick_btn;

    private EditText editTextEmail, editTextPassword;

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_login);


        forgot_btn = findViewById(R.id.forgotBtn);
        login_btn = findViewById(R.id.login_btn);
        oncreateaccount_btn = findViewById(R.id.create_acc_btn);
        onbackclick_btn = findViewById(R.id.btn_back);
        editTextEmail = (EditText) findViewById(R.id.login_email_et);
        editTextPassword= (EditText) findViewById(R.id.login_password_et);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        onClicks();
    }


    private void onClicks(){
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });


        oncreateaccount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1){
                    Intent intent = new Intent(InstructorLoginActivity.this, InstructorSignupActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(InstructorLoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }

            }
        });

        onbackclick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InstructorLoginActivity.this, StudentOrInstructorActivity.class);
                startActivity(intent);
            }
        });

        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InstructorLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });
    }


    private void performLogin(){

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(password.isEmpty() || password.length() < 6 ) {
            editTextPassword.setError("Enter proper Password");
        } else {

            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                progressDialog.setMessage("Please wait While Login...");
                progressDialog.setTitle("Login");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            loginDone();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(InstructorLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }

    }

    private void loginDone() {
        Intent intent = new Intent(InstructorLoginActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ActivityCompat.finishAffinity(InstructorLoginActivity.this);

    }

}