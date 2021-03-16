package com.example.notespasswords.ui.password;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notespasswords.R;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    private final List<Password> passwordData;

    public PasswordAdapter(List<Password> passwordData){
        this.passwordData = passwordData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View passwordItem = layoutInflater.inflate(R.layout.password_item, parent, false);
        return new ViewHolder(passwordItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Password password = passwordData.get(position);
        holder.siteTitle.setText(password.getSite());
    }

    @Override
    public int getItemCount() {
        return passwordData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView siteTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.siteTitle = itemView.findViewById(R.id.passwordTitle);
        }
    }
}
