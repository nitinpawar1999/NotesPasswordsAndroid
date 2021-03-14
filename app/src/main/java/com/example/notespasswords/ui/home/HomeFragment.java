package com.example.notespasswords.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.notespasswords.R;
import com.example.notespasswords.ui.note.noteFragment;
import com.example.notespasswords.ui.password.passwordFragment;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final Button noteBtn = root.findViewById(R.id.notesButton);
        final Button passwordBtn = root.findViewById(R.id.passwordsButton);
        Fragment fragmentNote = new noteFragment();
        Fragment fragmentPassword = new passwordFragment();
        noteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentNote).addToBackStack(null).commit();
            }
        });

        passwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentPassword).addToBackStack(null).commit();
            }
        });
        return root;
    }
}