package com.example.notespasswords.ui.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notespasswords.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final List<Note> noteData;

    public NoteAdapter(List<Note> noteData){
        this.noteData = noteData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View noteItem = layoutInflater.inflate(R.layout.note_item, parent, false);
        return new ViewHolder(noteItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Note note = noteData.get(position);
        holder.noteTitle.setText(note.getTitle());
    }

    @Override
    public int getItemCount() {
        return noteData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView noteTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.noteTitle = itemView.findViewById(R.id.noteTitle);
        }
    }
}
