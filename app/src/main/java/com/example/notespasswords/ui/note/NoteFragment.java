package com.example.notespasswords.ui.note;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.notespasswords.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

        swipeRefreshLayout = root.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.black),
                getResources().getColor(android.R.color.white),
                getResources().getColor(android.R.color.darker_gray),
                getResources().getColor(android.R.color.white));
        recyclerView = root.findViewById(R.id.noteRecyclerView);
        noteList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!= null) {
            Log.d(TAG, "Inside Data collection Method");
            db.collection("users").document(Objects.requireNonNull(firebaseUser.getEmail())).collection("notes").orderBy("noteTitle").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                        .setTitle("Title: " + noteList.get(position).getTitle()+" "+noteList.get(position).getId())
                                        .setMessage("Note Description\n"+noteList.get(position).getDescription())
                                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                db.collection("users").document(firebaseUser.getEmail()).collection("notes")
                                                        .document(noteList.get(position).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error deleting document", e);
                                                            }
                                                        });
                                            }
                                        });
                                viewDialog.show();

                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                Toast.makeText(getActivity(),"SinglelongClickon"+position,Toast.LENGTH_SHORT).show();

                            }
                        }));

                    }else{
                        Log.d(TAG, "Error getting data", task.getException());
                    }
                }
            });
        }



        FloatingActionButton fab = root.findViewById(R.id.fabNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNoteFragment addNoteFragment = new AddNoteFragment();
                addNoteFragment.show(getParentFragmentManager(), "dialog");
            }
        });
        return root;
    }

    @Override
    public void onRefresh() {
        noteList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!= null) {
            Log.d(TAG, "*********************Inside Data collection Method Refreshing mode***********************************");
            db.collection("users").document(Objects.requireNonNull(firebaseUser.getEmail())).collection("notes").orderBy("noteTitle").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
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

                        noteAdapter.notifyDataSetChanged();


                        swipeRefreshLayout.setRefreshing(false);
                    }else{
                        Log.d(TAG, "Error getting data", task.getException());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        }
    }

    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

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
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}