package com.example.mytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView loemail,lopassword,dont;
    Button signin;
    TextView resetpassword;
    FirebaseAuth fauth;


   ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loemail = findViewById(R.id.inputEmail);
        lopassword = findViewById(R.id.inputPassword);
        signin = findViewById(R.id.btnLogin);
        dont = findViewById(R.id.gotoRegister);
        resetpassword = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressBar2);


        fauth =FirebaseAuth.getInstance();


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loemail.getText().toString().trim();
                String password = lopassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    loemail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    lopassword.setError("Password Is Required");
                    return;
                } else if (password.length() < 8) {
                    lopassword.setError("Password mast be >=8 Characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this,"Login Succcessfully",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        dont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgetActivity.class);
                startActivity(intent);
            }
        });




    }
}

