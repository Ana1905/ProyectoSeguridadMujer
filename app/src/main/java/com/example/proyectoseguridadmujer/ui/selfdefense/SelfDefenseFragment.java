package com.example.proyectoseguridadmujer.ui.selfdefense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoseguridadmujer.BodyDefenseActivity;
import com.example.proyectoseguridadmujer.R;
import com.example.proyectoseguridadmujer.WeaponDefenseActivity;

import org.jetbrains.annotations.NotNull;

public class SelfDefenseFragment extends Fragment {
    private String email="";
    ImageButton mImageButtonBody, mImageButtonWeapon;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_self_defense, container, false);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Wiring up
        mImageButtonBody = getView().findViewById(R.id.ImageButtonBody);
        mImageButtonWeapon = getView().findViewById(R.id.ImageButtonWeapon);

        mImageButtonBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), BodyDefenseActivity.class);
              //  intent.putExtra("Sendemail", email);
                startActivity(intent);
                getActivity().finish();
            }
        });

        mImageButtonWeapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), WeaponDefenseActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}