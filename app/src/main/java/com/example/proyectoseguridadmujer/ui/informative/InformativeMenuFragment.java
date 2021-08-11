package com.example.proyectoseguridadmujer.ui.informative;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;

public class InformativeMenuFragment extends Fragment {
    private String email="";
    ImageButton mImageButtonInstitution, mImageButtonTest;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_informative_menu, container, false);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Wiring up
        mImageButtonInstitution = getView().findViewById(R.id.ImageButtonInstitution);
        mImageButtonTest = getView().findViewById(R.id.ImageButtonTest);

        mImageButtonInstitution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "hola", Toast.LENGTH_SHORT).show();
                /*
                Intent intent = new Intent(view.getContext(), InstitutionListFragment.class);
                startActivity(intent);
                getActivity().finish();
                Fragment nuevoFragmento = new InstitutionListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fra)
                transaction.addToBackStack(null);

                // Commit a la transacción
                transaction.commit();*/

            }
        });
/*
        mImageButtonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), TestFragment.class);
                startActivity(intent);
                getActivity().finish();
            }
        });*/
    }
}