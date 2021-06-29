package com.example.proyectoseguridadmujer.ui.selfdefense;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;

public class SelfDefenseFragment extends Fragment {

    ImageButton mImageButtonBody, mImageButtonWeapon;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_self_defense, container, false);


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
                Intent intent = new Intent(view.getContext(), BodyDefenseFragment.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        mImageButtonWeapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), WeaponDefenseFragment.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}