package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

    //Variables globales del adaptador:
    Context context;
    ArrayList<String> mListaImagenes;

    //Constructor del adaptador:
    public ImageAdapter(Context context, ArrayList<String> arrayList){
        this.context = context;
        this.mListaImagenes = arrayList;
    }

    //onCreate de todos los ViewHolder (todas las rows):
    @NonNull
    @NotNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_image, parent, false);
        return new ImageAdapter.ImageViewHolder(view);
    }

    //Se carga la informacion de la row:
    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageViewHolder holder, int position) {

        //Asignacion de datos:
        Glide.with(context).load(mListaImagenes.get(position)).into(holder.imageView);

        //onClick de la cajita:
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, mListaImagenes.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Devuelve la cantidad de rows:
    @Override
    public int getItemCount() {
        return mListaImagenes.size();
    }

    //Vista o row que cargara la RecyclerView
    public class ImageViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public ImageViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);

            //Wiring up:
            imageView = itemView.findViewById(R.id.rowImage);
        }
    }
}
