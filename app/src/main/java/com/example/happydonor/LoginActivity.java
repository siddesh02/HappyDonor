package com.example.happydonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password;
    private TextView forgot,gotoregister;
    private Button login;
    private FirebaseAuth mAuth;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        /*
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Happy Donor");*/

        gotoregister = (TextView) findViewById(R.id.gotoregister);
        gotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


        email = (EditText) findViewById(R.id.edtLoginEmail);
        password = (EditText) findViewById(R.id.edtLoginPassword);
        login = (Button) findViewById(R.id.btnLoginLogin);
        login.setOnClickListener(this);
        forgot = (TextView) findViewById(R.id.txtForgotPassword);
        forgot.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginLogin:
                userLogin();
                break;

            case R.id.txtForgotPassword:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
                break;
        }
    }

    private void userLogin() {
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        if(e.isEmpty()) {
            email.setError("Email required!");
            email.requestFocus();
            return;
        }

        if(p.isEmpty()) {
            password.setError("Password required!");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, ViewPostsActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

