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

public class SocialMediaAdapter extends RecyclerView.Adapter<SocialMediaAdapter.SocialMediaViewHolder>{

    //Variables globales del adaptador:
    Context context;
    ArrayList<SocialMedia> mListaRedesSociales;

    public SocialMediaAdapter(Context context, ArrayList<SocialMedia> arrayList){
        this.context = context;
        this.mListaRedesSociales = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public SocialMediaViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_social_media, parent, false);
        return new SocialMediaAdapter.SocialMediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SocialMediaViewHolder holder, int position) {
        //Asignacion de datos:
        if(mListaRedesSociales.get(position).getTipoRedSocial().equals("Facebook")){
            Glide.with(context).load("https://seguridadmujer.com/administracion/includes/imagenes/facebook.png").into(holder.mImageView);
        }
        else if(mListaRedesSociales.get(position).getTipoRedSocial().equals("Twitter")){
            Glide.with(context).load("https://seguridadmujer.com/administracion/includes/imagenes/twitter.png").into(holder.mImageView);
        }
        else if(mListaRedesSociales.get(position).getTipoRedSocial().equals("Instagram")){
            Glide.with(context).load("https://seguridadmujer.com/administracion/includes/imagenes/instagramLogo.png").into(holder.mImageView);
        }
        else if(mListaRedesSociales.get(position).getTipoRedSocial().equals("Youtube")){
            Glide.with(context).load("https://seguridadmujer.com/administracion/includes/imagenes/youtube.png").into(holder.mImageView);
        }
        else{
            Glide.with(context).load("https://seguridadmujer.com/administracion/includes/imagenes/generic.png").into(holder.mImageView);
        }

        holder.mTextViewTipo.setText(mListaRedesSociales.get(position).getTipoRedSocial());
        holder.mTextViewNombre.setText(mListaRedesSociales.get(position).getNombreRedSocial());

    }

    @Override
    public int getItemCount() {
        return mListaRedesSociales.size();
    }

    public class SocialMediaViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mTextViewTipo;
        TextView mTextViewNombre;

        public SocialMediaViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            //Wiring up:
            mImageView = itemView.findViewById(R.id.ImagenRedSocial);
            mTextViewTipo = itemView.findViewById(R.id.TipoRedSocial);
            mTextViewNombre = itemView.findViewById(R.id.NombreRedSocial);
        }
    }
}
