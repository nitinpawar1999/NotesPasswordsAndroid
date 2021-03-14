package com.example.notespasswords.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notespasswords.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class registerActivity extends AppCompatActivity {

    private static final String TAG = "registerActivity";
    FirebaseAuth mAuth;
    String userNameString = "";
    String emailString = "";
    String passwordString = "";
    LinearProgressIndicator progressIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        TextInputLayout username = findViewById(R.id.userNameField);
        TextInputLayout email = findViewById(R.id.emailFieldR);
        TextInputLayout password = findViewById(R.id.passwordFieldR);
        TextInputLayout confirmPass = findViewById(R.id.confirmPasswordR);
        Button registerBtn = findViewById(R.id.registerButton);
        TextView loginPage = findViewById(R.id.loginPage);
        progressIndicator = findViewById(R.id.registerProgress);
        loginPage.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userNameString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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
                passwordString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!passwordString.equals(s.toString())){
                    confirmPass.setError("Password Doesn't Match");
                    registerBtn.setEnabled(false);
                }else{
                    confirmPass.setErrorEnabled(false);
                    registerBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerBtn.setOnClickListener(v -> {
            if (userNameString == null || userNameString.isEmpty() || passwordString == null || passwordString.isEmpty() || emailString == null || emailString.isEmpty()) {
                Toast.makeText(registerActivity.this, "Please enter Credentials",Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                register(userNameString, emailString, passwordString, intent);
            }
        });
    }

    public void register(String userName, String email, String password, Intent intent){
        progressIndicator.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(userName).build();
                            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(registerActivity.this, "Registration Successful, you will be redirected to login page.",Toast.LENGTH_SHORT).show();
                                    final Handler handler = new Handler(Looper.getMainLooper());
                                    progressIndicator.setVisibility(View.INVISIBLE);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(intent);
                                        }
                                    }, 1000);
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            progressIndicator.setVisibility(View.INVISIBLE);
                            Toast.makeText(registerActivity.this, "Registration failed. "+ task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

