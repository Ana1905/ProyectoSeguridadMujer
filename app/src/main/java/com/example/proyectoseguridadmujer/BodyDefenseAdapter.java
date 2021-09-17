package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
        //view.setOnClickListener(mOnClickListener);
        return new BodyDefenseAdapter.BodyDefenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BodyDefenseViewHolder holder, int position)
    {
        holder.Title.setText(list.get(position).getTitulo());
        holder.Description.setText(list.get(position).getContenido());
        Glide.with(context).load(list.get(position).getRutaImagenPresentacion()).into(holder.Image);
        holder.bind(list.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context.getApplicationContext(), "Toast watonoso", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), DefenseTechniqueActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Technique", list.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
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
        DefenseTechniques Data;

        public BodyDefenseViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);

            Title = itemView.findViewById(R.id.DefenseTechnique);
            Description = itemView.findViewById(R.id.DefenseDescription);
            Image = itemView.findViewById(R.id.DefenseImage);
        }

        public void bind (DefenseTechniques techniques)
        {
            Data = techniques;
        }
    }
}