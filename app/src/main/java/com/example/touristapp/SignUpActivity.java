package com.example.touristapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private EditText editTextName,editTextEmail,editTextPassword;
    private Button buttonRegister;
    private TextView textViewAlreadyAccount;

        boolean imageControl = false;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Uri uri;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        profileImage = findViewById(R.id.profile_image);

        editTextName = findViewById(R.id.et_name);
        editTextEmail = findViewById(R.id.et_email_address_forgot);
        editTextPassword = findViewById(R.id.et_password);

        textViewAlreadyAccount = findViewById(R.id.tv_login);

        buttonRegister = findViewById(R.id.btn_register);

        ////FIREBASE

        //database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference();


        //storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();










        //profile image listener
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ImageChoser();
            }
        });
        //button register listener
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String userName = editTextName.getText().toString();

                if(!email.equals("") && !password.equals("") && !userName.equals("")) {
                    signUp(userName,email,password);
                }

            }
        });


        //already have an account listener
        textViewAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signUp(String userName, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign Up:", "createUserWithEmail:success");
//                            FirebaseUser user = auth.getCurrentUser();
//                            updateUI(user);

                            databaseReference.child("Users").child(auth.getUid())
                                    .child("userName").setValue(userName);
                            if(imageControl) {
                                UUID randomID = UUID.randomUUID();
                                String imageName = "images/" + randomID + ".jpg";
                                storageReference.child(imageName).putFile(uri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            storageReference = firebaseStorage.getReference(imageName);
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String filePath = uri.toString();
                                                    databaseReference.child("Users").child(auth.getUid())
                                                            .child("image").setValue(filePath)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(SignUpActivity.this,
                                                                            "Registration completed",
                                                                            Toast.LENGTH_SHORT)
                                                                            .show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull @NotNull Exception e) {
                                                            Toast.makeText(SignUpActivity.this, "Registration failed.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                    }
                                });
                            }
                        }
                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                        intent.putExtra("userName",userName);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    private void ImageChoser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK && data != null) {
            uri = data.getData();
            Picasso.get().load(uri).into(profileImage);
            imageControl = true;
        } else {
            imageControl = false;
        }
    }
}