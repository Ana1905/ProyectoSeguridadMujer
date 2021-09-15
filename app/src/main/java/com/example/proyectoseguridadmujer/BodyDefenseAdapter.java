package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BodyDefenseAdapter extends RecyclerView.Adapter<BodyDefenseAdapter.BodyDefenseViewHolder>
{
    Context context;
    List<DefenseTechniques> list = new ArrayList();

    public BodyDefenseAdapter (Context context, List<DefenseTechniques> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public BodyDefenseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_defense, parent, false);
        return new BodyDefenseAdapter.BodyDefenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BodyDefenseViewHolder holder, int position)
    {
        holder.Title.setText(list.get(position).getTitulo());
        holder.Description.setText(list.get(position).getContenido());
        Glide.with(context).load(list.get(position).getRutaImagenPresentacion()).into(holder.Image);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class BodyDefenseViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title, Description;
        ImageView Image;

        public BodyDefenseViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);

            Title = itemView.findViewById(R.id.DefenseTechnique);
            Description = itemView.findViewById(R.id.DefenseDescription);
            Image = itemView.findViewById(R.id.DefenseImage);
        }
    }
}