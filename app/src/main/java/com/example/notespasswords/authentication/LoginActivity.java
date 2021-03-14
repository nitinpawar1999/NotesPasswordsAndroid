package com.example.notespasswords.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notespasswords.R;
import com.example.notespasswords.landingActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "loginActivity";
    String emailString;
    String passwordString;
    LinearProgressIndicator progressIndicator;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(this, "Already Signed in, Happy to see you again "+currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            updateUI(currentUser);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        progressIndicator = findViewById(R.id.loginProgress);
        TextInputLayout email = findViewById(R.id.emailField);
        TextInputLayout password = findViewById(R.id.passwordField);
        TextView registerPage = findViewById(R.id.registerPage);
        registerPage.setOnClickListener(v -> {
            Intent intent = new Intent(this, registerActivity.class);
            startActivity(intent);
        });

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordString = s.toString();}

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        Button login = findViewById(R.id.loginButton);


        login.setOnClickListener(v -> {
            if(emailString == null || passwordString == null || emailString.isEmpty() || passwordString.isEmpty()){
                Toast.makeText(LoginActivity.this, "Please enter Credentials",Toast.LENGTH_SHORT).show();
            }else{
                login(emailString, passwordString);
            }

        });
    }


    public void updateUI(FirebaseUser user){
        if(user != null) {
            Intent intent = new Intent(this, landingActivity.class);
            startActivity(intent);
        }
    }

    public void login(String emailString, String passwordString) {
        progressIndicator.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        progressIndicator.setVisibility(View.INVISIBLE);
                        updateUI(user);

                    } else {
                        Log.d(TAG, "Signin Failed", task.getException());
                        progressIndicator.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Authentication failed. "+task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }
}