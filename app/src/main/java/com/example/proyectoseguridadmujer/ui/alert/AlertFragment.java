package com.example.proyectoseguridadmujer.ui.alert;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectoseguridadmujer.AdapterContacts;
import com.example.proyectoseguridadmujer.AddTrustedFriendsActivity;
import com.example.proyectoseguridadmujer.BandVinculationActivity;
import com.example.proyectoseguridadmujer.Contact;
import com.example.proyectoseguridadmujer.MapsActivity;
import com.example.proyectoseguridadmujer.R;
import com.example.proyectoseguridadmujer.SendAlertTestingActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AlertFragment extends Fragment {
private String email="";
ArrayList<Contact> ListContacts;
RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_alert, container, false);
        /*
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");


        recyclerView= root.findViewById(R.id.recyclerViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListContacts= new ArrayList<Contact>();

        for(int i=0;i<50;i++){
            Contact contact = new Contact("hola" , "2");
            ListContacts.add(contact);
        }

        AdapterContacts adapter= new AdapterContacts(ListContacts);
        recyclerView.setAdapter(adapter);
        */

        return root;

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = new Intent(view.getContext(), AddTrustedFriendsActivity.class);
        startActivity(intent);
        getActivity().finish();

    }
}