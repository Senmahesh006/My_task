package com.example.mytask;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {


    public static final String TAP = "Tap";
    private  EditText reemail,repassword,reUsername;

    Button signup;
    TextView signin;

    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    String userID;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reUsername = findViewById(R.id.fullname);
        reemail = findViewById(R.id.inputEmail);
        repassword = findViewById(R.id.inputPassword);
        signup = findViewById(R.id.btnsignup);
        signin =findViewById(R.id.SIGN);
        progressBar =findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, password, Username;

                Username = reUsername.getText().toString().trim();
                email = reemail.getText().toString().trim();
                password = repassword.getText().toString().trim();



                if (TextUtils.isEmpty(email)){
                    reemail.setError("Email is Required");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    reemail.setError("Please enter a valid email address");

                } else {
                    reemail.setError(null);
                }
                if (TextUtils.isEmpty(Username)){
                    reUsername.setError("User Name is Required");

                }

                if (TextUtils.isEmpty(password)){
                    repassword.setError("password is Required");
                }
                if (password.length()<8){
                    repassword.setError("password must be 8 Digit");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Register.this,"User Created",Toast.LENGTH_LONG).show();
                        userID=fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference= firestore.collection("Users").document(userID);
                        Map<String,Object>user  = new HashMap<>();
                        user.put("femail",email);
                        user.put("password",password);
                        user.put("UserName",Username);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAP,"onSuccess: User Created"+userID);
                            }
                        });

                        startActivity(new Intent(getApplicationContext(),Login.class));

                    }else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Register.this," Have Some Error"+task.getException().getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
            });

        }
    });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });

    }
}