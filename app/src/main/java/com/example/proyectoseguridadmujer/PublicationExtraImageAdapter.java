package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PublicationExtraImageAdapter extends RecyclerView.Adapter<PublicationExtraImageAdapter.ImageViewHolder> {

    Context context;
    ArrayList<String> list = new ArrayList<>();

    public PublicationExtraImageAdapter(Context context, ArrayList<String> Link)
    {
        this.context = context;
        this.list = Link;
    }

    @NonNull
    @NotNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_image_publication, parent, false);
        return new PublicationExtraImageAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageViewHolder holder, int position) {
        Glide.with(context).load(list.get(position)).into(holder.ExtraImage);
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ExtraImage;
        String Source;

        public ImageViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            ExtraImage = itemView.findViewById(R.id.NoCircularImageView);
        }

        public void bind (String Link)
        {
            Source = Link;
        }
    }
}