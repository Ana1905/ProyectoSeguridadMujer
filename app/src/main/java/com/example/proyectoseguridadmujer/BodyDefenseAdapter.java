package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoseguridadmujer.ui.informative.InstitutionListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BodyDefenseAdapter extends RecyclerView.Adapter<BodyDefenseAdapter.BodyDefenseViewHolder> {

    String Titles[], Descriptions[];
    int Images[];
    Context context;
    List<BodyDefenseTechniques> list;

    public BodyDefenseAdapter (Context context, String[] Titles, String[] Descriptions, int[] Images) {
        this.context = context;
        this.Titles = Titles;
        this.Descriptions = Descriptions;
        this.Images = Images;
    }

    public BodyDefenseAdapter (Context context, List<BodyDefenseTechniques> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public BodyDefenseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_body_defense, parent, false);
        return new BodyDefenseAdapter.BodyDefenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BodyDefenseViewHolder holder, int position) {
        /*
        holder.Title.setText(Titles[position]);
        holder.Description.setText(Descriptions[position]);
        holder.Image.setImageResource(Images[position]);
        */


        holder.Title.setText(list.get(position).getTitulo());
        holder.Description.setText(list.get(position).getComentario());
        holder.Image.setImageResource(list.get(position).getImagen());

    }

    @Override
    public int getItemCount() {
        //return Titles.length;
        return list.size();
    }


    public class BodyDefenseViewHolder extends RecyclerView.ViewHolder {

        TextView Title, Description;
        ImageView Image;

        public BodyDefenseViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.BodyDefenseTechnique);
            Description = itemView.findViewById(R.id.BodyDefenseDescription);
            Image = itemView.findViewById(R.id.BodyDefenseImage);
        }
    }
}
