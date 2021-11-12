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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import Dialogs.DialogShowInstitution;
import Dialogs.DialogShowTip;

public class InstitutionAdapter2 extends RecyclerView.Adapter<InstitutionAdapter2.InstitutionViewHolder>{

    //Variables globales del adaptador:
    Context context;
    ArrayList<Institutions> mListaInstituciones;

    public InstitutionAdapter2(Context context, ArrayList<Institutions> arrayList){
        this.context = context;
        this.mListaInstituciones = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public InstitutionViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_institution_list, parent, false);
        return new InstitutionAdapter2.InstitutionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InstitutionViewHolder holder, int position) {
        //Asignacion de datos:
        holder.Title.setText(mListaInstituciones.get(position).getNombre());
        holder.Area.setText(mListaInstituciones.get(position).getNombreCategoria());
        Glide.with(context).load(mListaInstituciones.get(position).getRutaImagenPresentacion()).into(holder.Image);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((TestResultsActivity)context).getSupportFragmentManager();
                DialogShowInstitution dialogShowInstitution = new DialogShowInstitution();

                Bundle arguments = new Bundle();
                arguments.putInt("ID_Institucion", mListaInstituciones.get(position).getID_Institucion());
                arguments.putString("Nombre", mListaInstituciones.get(position).getNombre());
                arguments.putString("Descripcion", mListaInstituciones.get(position).getDescripcion());
                arguments.putString("Telefono", mListaInstituciones.get(position).getTelefono());
                arguments.putString("PaginaWeb", mListaInstituciones.get(position).getPaginaWeb());
                arguments.putDouble("Latitud", mListaInstituciones.get(position).getLatitud());
                arguments.putDouble("Longitud", mListaInstituciones.get(position).getLongitud());
                arguments.putString("RutaImagen", mListaInstituciones.get(position).getRutaImagenPresentacion());
                arguments.putString("Area", mListaInstituciones.get(position).getArea());
                arguments.putString("NombreCategoria", mListaInstituciones.get(position).getNombreCategoria());

                dialogShowInstitution.setArguments(arguments);
                //dialogNewHelpPostFragment.show(((MainActivity)context).getSupportFragmentManager(), "tag");

                //dialogFragment.show(Objects.requireNonNull(context.getFragmentManager()),null);
                /*
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.content_main, mFragment_member)
                        .commit();

                 */

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(android.R.id.content, dialogShowInstitution).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListaInstituciones.size();
    }

    public class InstitutionViewHolder extends RecyclerView.ViewHolder{

        TextView Title, Area;
        ImageView Image;

        public InstitutionViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            //Wiring up:
            Title = itemView.findViewById(R.id.InstitutionName);
            Area = itemView.findViewById(R.id.InstitutionArea);
            Image = itemView.findViewById(R.id.InstitutionImage);
        }
    }
}

