package com.example.proyectoseguridadmujer.ui.informative;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;

public class InstitutionListAdapter extends RecyclerView.Adapter<InstitutionListAdapter.ListViewHolder> {

    String Titles[], Descriptions[];
    int Images[];
    Context context;

    public InstitutionListAdapter (Context context, String[] Titles, String[] Descriptions, int[] Images) {
        this.context = context;
        this.Titles = Titles;
        this.Descriptions = Descriptions;
        this.Images = Images;
    }

    @NonNull
    @NotNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_institution_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListViewHolder holder, int position) {
        holder.Title.setText(Titles[position]);
        holder.Description.setText(Descriptions[position]);
        holder.Image.setImageResource(Images[position]);
    }

    @Override
    public int getItemCount() {
        return Titles.length;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        TextView Title, Description;
        ImageView Image;

        public ListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.InstitutionName);
            Description = itemView.findViewById(R.id.InstitutionLocation);
            Image = itemView.findViewById(R.id.InstitutionImage);
        }
    }
}
