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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    TextView textViewRegister;
    EditText editTextEmail;
    Button buttonResetPassword;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        textViewRegister = findViewById(R.id.tv_register);
        editTextEmail = findViewById(R.id.et_email_address_forgot);
        buttonResetPassword = findViewById(R.id.btn_reset_password);
        auth = FirebaseAuth.getInstance();


        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String emailAddress = editTextEmail.getText().toString();
                    if(!emailAddress.equals("")) {
                        resetEmail(emailAddress);
                    }else {
                        Toast.makeText(ForgotPassword.this,"Please enter email address",Toast.LENGTH_SHORT);
                    }
            }
        });




        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void resetEmail(String emailAddress) {
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("ForgotPassword", "Email sent.");
                            Toast.makeText(ForgotPassword.this, "Please check your email", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPassword.this, "This email is not registered", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}