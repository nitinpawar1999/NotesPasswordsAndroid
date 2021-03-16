package com.example.notespasswords.ui.note;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.notespasswords.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "NoteFragment";

    View root;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    List<Note> noteList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_note, container, false);

        swipeRefreshLayout = root.findViewById(R.id.note_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.black),
                getResources().getColor(android.R.color.white),
                getResources().getColor(android.R.color.darker_gray),
                getResources().getColor(android.R.color.white));
        recyclerView = root.findViewById(R.id.noteRecyclerView);
        noteList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            Log.d(TAG, "Inside Data collection Method");
            db.collection("users").document(Objects.requireNonNull(firebaseUser.getEmail())).collection("notes").orderBy("noteTitle").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                        String title = (String) documentSnapshot.get("noteTitle");
                        String description = (String) documentSnapshot.get("noteDescription");
                        Timestamp date = (Timestamp) documentSnapshot.get("time");
                        String id = documentSnapshot.getId();
                        Note note = new Note(id, title, description, date);
                        noteList.add(note);
                    }
                    noteAdapter = new NoteAdapter(noteList);


                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(noteAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            MaterialAlertDialogBuilder viewDialog = new MaterialAlertDialogBuilder(getActivity())
                                    .setTitle("Title: " + noteList.get(position).getTitle())
                                    .setMessage("Note Description\n" + noteList.get(position).getDescription())
                                    .setPositiveButton("Delete", (dialog, which) -> db.collection("users").document(firebaseUser.getEmail()).collection("notes")
                                            .document(noteList.get(position).getId()).delete().addOnSuccessListener(aVoid -> {
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                onRefresh();
                                            })
                                            .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e)));
                            viewDialog.show();

                        }

                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));

                }else{
                    Log.d(TAG, "Error getting data", task.getException());
                }
            });
        }



        FloatingActionButton fab = root.findViewById(R.id.fabNote);
        fab.setOnClickListener(view -> {
            AddNoteFragment addNoteFragment = new AddNoteFragment();
            addNoteFragment.show(getParentFragmentManager(), "dialogNote");
        });
        return root;
    }

    @Override
    public void onRefresh() {
        noteList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            Log.d(TAG, "*********************Inside Data collection Method Refreshing mode***********************************");
            db.collection("users").document(Objects.requireNonNull(firebaseUser.getEmail())).collection("notes").orderBy("noteTitle").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                        String title = (String) documentSnapshot.get("noteTitle");
                        String description = (String) documentSnapshot.get("noteDescription");
                        Timestamp date = (Timestamp) documentSnapshot.get("time");
                        String id = documentSnapshot.getId();
                        Note note = new Note(id, title, description, date);
                        noteList.add(note);
                    }

                    noteAdapter.notifyDataSetChanged();


                }else{
                    Log.d(TAG, "Error getting data", task.getException());
                }
                swipeRefreshLayout.setRefreshing(false);
            });
        }
    }

    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private final ClickListener clicklistener;
        private final GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(@NotNull RecyclerView rv, @NotNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}