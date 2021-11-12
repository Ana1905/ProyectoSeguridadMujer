package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>
{
    private Context context;
    private ArrayList<Comment> list = new ArrayList<>();
    private String email;

    public CommentListAdapter(Context context, ArrayList<Comment> itemList)
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
        View view = inflater.inflate(R.layout.dialog_comment_row, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentViewHolder holder, int position) {
        holder.User.setText(list.get(position).getNombre());
        holder.Content.setText(list.get(position).getComentario());
        holder.Date.setText(list.get(position).getFecha());

        if(list.get(position).getRutaImagen() != null && !list.get(position).getRutaImagen().isEmpty()){
            Glide.with(context).load(list.get(position).getRutaImagen()).into(holder.Profile);
        }
        else{
            Glide.with(context).load("http://seguridadmujer.com/web/Resources/avatar_woman_people_girl_glasses_icon_159125.png").into(holder.Profile);
        }
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
        TextView User, Date, Content;
        Comment Comment;

        public CommentViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            Profile = itemView.findViewById(R.id.CommentRowImage);
            User = itemView.findViewById(R.id.CommentRowUser);
            Date = itemView.findViewById(R.id.DateRowComment);
            Content = itemView.findViewById(R.id.CommentRowContent);
        }

        public void bind (Comment listElement)
        {
            Comment = listElement;
        }
    }


}