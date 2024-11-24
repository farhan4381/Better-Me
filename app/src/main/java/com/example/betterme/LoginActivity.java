package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.betterme.Models.Instructor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    Button forgot_btn;
    Button login_btn;
    Button oncreateaccount_btn;
    Button onbackclick_btn;

    DatabaseReference database;
    private EditText editTextEmail, editTextPassword;

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        if (type == 1){
            database = FirebaseDatabase.getInstance().getReference("Instructors");
        }else {
            database = FirebaseDatabase.getInstance().getReference("Users");
        }

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
                    Intent intent = new Intent(LoginActivity.this, InstructorSignupActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            }
        });
        onbackclick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
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

                            database.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                    addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                loginDone();
                                            }else {

                                                FirebaseAuth.getInstance().signOut();
                                                Toast.makeText(LoginActivity.this, "Wrong email or password",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }
    }


    private void loginDone(){

        SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
        editor.putInt("type", type);
        editor.apply();

        if (type == 1){
            Intent intent = new Intent(LoginActivity.this, BookingListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            ActivityCompat.finishAffinity(LoginActivity.this);
        }else {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            ActivityCompat.finishAffinity(LoginActivity.this);
        }
    }

}