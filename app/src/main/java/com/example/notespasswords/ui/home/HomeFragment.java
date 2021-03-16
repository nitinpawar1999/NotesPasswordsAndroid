package com.example.notespasswords.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.notespasswords.R;
import com.example.notespasswords.ui.note.NoteFragment;
import com.example.notespasswords.ui.password.PasswordFragment;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final Button noteBtn = root.findViewById(R.id.notesButton);
        final Button passwordBtn = root.findViewById(R.id.passwordsButton);
        Fragment fragmentNote = new NoteFragment();
        Fragment fragmentPassword = new PasswordFragment();
        noteBtn.setOnClickListener(v -> {

            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, fragmentNote).addToBackStack(null).commit();
        });

        passwordBtn.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, fragmentPassword).addToBackStack(null).commit();
        });
        return root;
    }

}