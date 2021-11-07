package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>
{
    private Context context;
    private List<ListElement> list = new ArrayList<>();
    private String email;

    public CommentListAdapter(Context context, List<ListElement> itemList)
    {
        this.context = context;
        this.list =itemList;
    }

    @NonNull
    @NotNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_comment_row,null);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentViewHolder holder, int position) {
        holder.User.setText(list.get(position).getNombre());
        holder.Content.setText(list.get(position).getContenido());
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = context.getSharedPreferences("Credencials",context.MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder
    {
        ImageView Profile;
        TextView User, Content;
        ListElement Data;

        public CommentViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            Profile = itemView.findViewById(R.id.CommentRowImage);
            User = itemView.findViewById(R.id.CommentRowUser);
            Content = itemView.findViewById(R.id.CommentRowContent);
        }

        public void bind (ListElement listElement)
        {
            Data = listElement;
        }
    }
}
