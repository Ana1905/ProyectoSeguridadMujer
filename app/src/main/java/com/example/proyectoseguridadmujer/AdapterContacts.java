package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoseguridadmujer.ui.alert.AlertFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import Dialogs.DialogEditContact;
import Dialogs.DialogUserProfile;

public class AdapterContacts extends RecyclerView.Adapter<AdapterContacts.ViewHolderContacts> {

    private Context context;
    ArrayList<Contact> ListContacts;
    DialogEditContact.Listener fragment;

    public AdapterContacts(Context context, ArrayList<Contact> listContacts, DialogEditContact.Listener fragment) {
        this.ListContacts = listContacts;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderContacts onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list,parent,false);
        return new ViewHolderContacts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderContacts holder, int position) {
        holder.Nombre.setText(ListContacts.get(position).getNombre());
        holder.Num.setText(ListContacts.get(position).getNumero());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), holder.Nombre.getText(), Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                DialogEditContact dialogEditContact = new DialogEditContact();

                Bundle bundle = new Bundle();
                bundle.putInt("ID", ListContacts.get(position).ID_Contacto);
                bundle.putString("Nombre", ListContacts.get(position).Nombre);
                bundle.putString("Numero", ListContacts.get(position).Numero);

                dialogEditContact.setArguments(bundle);
                dialogEditContact.setListener(fragment);
                //dialogEditContact.setTargetFragment(fragment.frag("AlertFragment"), 1);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(android.R.id.content, dialogEditContact).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListContacts.size();
    }

    public class ViewHolderContacts extends RecyclerView.ViewHolder implements View.OnClickListener{

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

        @Override
        public void onClick(View v) {

        }

    }

}
