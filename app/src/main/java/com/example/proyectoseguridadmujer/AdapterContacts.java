package com.example.proyectoseguridadmujer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterContacts extends RecyclerView.Adapter<AdapterContacts.ViewHolderContacts> {

    ArrayList<Contact> ListContacts;

    public AdapterContacts(ArrayList<Contact> listContacts) {
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
        holder.Nombre.setText(ListContacts.get(position).getNombre());
        holder.Num.setText(ListContacts.get(position).getNumero());
    }

    @Override
    public int getItemCount() {
        return ListContacts.size();
    }

    public class ViewHolderContacts extends RecyclerView.ViewHolder {

        TextView Nombre;
        TextView Num;

        public ViewHolderContacts(@NonNull @NotNull View itemView) {
            super(itemView);

            //WIRING UP
            Nombre= (TextView) itemView.findViewById(R.id.contactoNombre);
            Num= (TextView)  itemView.findViewById(R.id.contactoTel);
        }

        public void asignarDatos(String Name, String Number) {
            Nombre.setText(Name);
            Num.setText(Number);
        }
    }
}
