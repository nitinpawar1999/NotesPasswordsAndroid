package com.example.notespasswords.ui.note;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ActivityInfoCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.notespasswords.LandingActivity;
import com.example.notespasswords.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AddNoteFragment extends DialogFragment {
    String titleString = "";
    String descriptionString = "";
    private static final String TAG = "addNoteFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View noteFragment = inflater.inflate(R.layout.fragment_add_note, container, false);
        final TextInputLayout title = noteFragment.findViewById(R.id.noteTitleField);
        final TextInputLayout description = noteFragment.findViewById(R.id.noteDescriptionField);
        final Button subBtn = noteFragment.findViewById(R.id.noteSubmitButton);

        Objects.requireNonNull(title.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    title.setError("Cannot Be Empty");
                    subBtn.setEnabled(false);
                }else{
                    title.setErrorEnabled(false);
                    subBtn.setEnabled(true);
                    titleString=s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Objects.requireNonNull(description.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    description.setError("Cannot Be Empty");
                    subBtn.setEnabled(false);
                }else{
                    description.setErrorEnabled(false);
                    subBtn.setEnabled(true);
                    descriptionString = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( titleString.isEmpty() || descriptionString.isEmpty()){
                    Toast.makeText(getContext(), "Enter Note Details",Toast.LENGTH_SHORT).show();
                }
                else
                {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!= null){
                    Map<String, Object> noteData = new HashMap<>();
                    noteData.put("userEmail", user.getEmail());
                    noteData.put("noteTitle", titleString);
                    noteData.put("noteDescription", descriptionString);
                    noteData.put("time", new Date());
                    db.collection("users").document(Objects.requireNonNull(user.getEmail())).collection("notes").add(noteData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(getContext(), "Note Saved",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(getContext(), "Error Note Not Saved",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    });
                }
            }
            }
        });
        return noteFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}