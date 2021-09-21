package com.example.proyectoseguridadmujer.ui.alert;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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
import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AlertFragment extends Fragment {
private String email="";
ArrayList<String> ListContacts;
RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_alert, container, false);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");


        return root;

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.findViewById(R.id.recyclerViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListContacts= new ArrayList<String>();

        for(int i=0;i<50;i++){
            ListContacts.add("dato #" + i +"");
        }

        AdapterContacts adapter= new AdapterContacts(ListContacts);
        recyclerView.setAdapter(adapter);


    }
}