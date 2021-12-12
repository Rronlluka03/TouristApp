package com.example.touristapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonSignIn;
    private TextView textViewForgotPassword, textViewRegister;


    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.et_email);
        editTextPassword = findViewById(R.id.et_password);

        buttonSignIn = findViewById(R.id.btn_login);

        textViewForgotPassword = findViewById(R.id.tv_forgot_password);
        textViewRegister = findViewById(R.id.tv_register);

        auth = FirebaseAuth.getInstance();

        //register listener
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        //forgot password listener
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });


        //sign in listener
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();


                if (!emailAddress.equals("") && !password.equals("")) {
                        SignIn(emailAddress,password);
                }
            }
        });


    }

    private void SignIn(String emailAddress, String password) {
        auth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("SIGN IN", "signInWithEmail:success");

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this,"LOG IN SUCCESFUL",Toast.LENGTH_SHORT).show();

                        } else {

                            Log.w("SIGN IN :", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Username or password incorrect",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}