package com.example.proyectoseguridadmujer.ui.comunity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectoseguridadmujer.R;

public class ComunityFragment extends Fragment {
    private String email="";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_comunity, container, false);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");

        return root;
    }
}