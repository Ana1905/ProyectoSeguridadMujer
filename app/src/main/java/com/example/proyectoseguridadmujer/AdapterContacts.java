package com.example.proyectoseguridadmujer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterContacts extends RecyclerView.Adapter<AdapterContacts.ViewHolderContacts> {

    ArrayList<String> ListContacts;

    public AdapterContacts(ArrayList<String> listContacts) {
        this.ListContacts = listContacts;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderContacts onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list,null,false);
        return new ViewHolderContacts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderContacts holder, int position) {
        holder.asignarDatos(ListContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return ListContacts.size();
    }

    public class ViewHolderContacts extends RecyclerView.ViewHolder {

        TextView contacto;
        public ViewHolderContacts(@NonNull @NotNull View itemView) {
            super(itemView);

            //WIRING UP
            contacto= (TextView) itemView.findViewById(R.id.contactoNombre);
        }

        public void asignarDatos(String info) {
            contacto.setText(info);
        }
    }
}
