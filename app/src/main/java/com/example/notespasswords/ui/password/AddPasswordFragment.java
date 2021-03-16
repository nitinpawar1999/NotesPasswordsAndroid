package com.example.notespasswords.ui.password;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.notespasswords.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddPasswordFragment extends DialogFragment {

    String siteString = "";
    String userNameString = "";
    String passwordString = "";
    private static final String TAG = "addPasswordFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View passwordFragment = inflater.inflate(R.layout.fragment_add_password, container, false);
        final TextInputLayout site = passwordFragment.findViewById(R.id.siteField);
        final  TextInputLayout userName = passwordFragment.findViewById(R.id.addUsernameField);
        final TextInputLayout password = passwordFragment.findViewById(R.id.addPasswordField);
        final Button subBtn = passwordFragment.findViewById(R.id.passwordSubmitButton);

        Objects.requireNonNull(site.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()) {
                    site.setError("Cannot Be Empty");
                    subBtn.setEnabled(false);
                }else{
                    site.setErrorEnabled(false);
                    subBtn.setEnabled(true);
                    siteString = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Objects.requireNonNull(userName.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    userName.setError("Cannot Be Empty");
                    subBtn.setEnabled(false);
                }else{
                    userName.setErrorEnabled(false);
                    subBtn.setEnabled(true);
                    userNameString = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Objects.requireNonNull(password.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    password.setError("Cannot Be Empty");
                    subBtn.setEnabled(false);
                }else{
                    password.setErrorEnabled(false);
                    subBtn.setEnabled(true);
                    passwordString = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        subBtn.setOnClickListener(v -> {
            if(siteString.isEmpty() || userNameString.isEmpty() || passwordString.isEmpty()){
                Toast.makeText(getContext(), "Enter Password Details",Toast.LENGTH_SHORT).show();
            }else{
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Map<String, Object> passwordData = new HashMap<>();
                    passwordData.put("siteName", siteString);
                    passwordData.put("username", userNameString);
                    passwordData.put("password", passwordString);
                    passwordData.put("time", new Date());

                    db.collection("users").document(Objects.requireNonNull(user.getEmail())).collection("passwords").add(passwordData)
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(getContext(), "Password Saved",Toast.LENGTH_SHORT).show();
                                dismiss();
                            }).addOnFailureListener(e -> {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(getContext(), "Error Password Not Saved",Toast.LENGTH_SHORT).show();
                                dismiss();
                            });
                }
            }
        });
        return passwordFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
