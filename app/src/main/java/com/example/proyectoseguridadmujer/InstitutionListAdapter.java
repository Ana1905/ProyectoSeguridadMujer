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

public class InstitutionListAdapter extends RecyclerView.Adapter<InstitutionListAdapter.InstitutionListViewHolder>
{
    Context context;
    List<Institutions> list = new ArrayList<>();

    public InstitutionListAdapter (Context context, List<Institutions> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public InstitutionListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_institution_list, parent, false);
        return new InstitutionListAdapter.InstitutionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InstitutionListViewHolder holder, int position)
    {
        holder.Title.setText(list.get(position).getNombre());
        holder.Area.setText(list.get(position).getArea());
        Glide.with(context).load(list.get(position).getRutaImagenPresentacion()).into(holder.Image);
        holder.bind(list.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Institution", list.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InstitutionListViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title, Area;
        ImageView Image;
        Institutions Data;


        public InstitutionListViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);

            Title = itemView.findViewById(R.id.InstitutionName);
            Area = itemView.findViewById(R.id.InstitutionArea);
            Image = itemView.findViewById(R.id.InstitutionImage);
        }

        public void bind (Institutions institutions)
        {
            Data = institutions;
        }
    }
}
