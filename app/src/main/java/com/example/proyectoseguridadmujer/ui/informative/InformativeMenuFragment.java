package com.example.proyectoseguridadmujer.ui.informative;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectoseguridadmujer.R;
import com.example.proyectoseguridadmujer.ui.gallery.GalleryViewModel;

public class InformativeMenuFragment extends Fragment {

    private InformativeMenuViewModel informativeMenuViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        informativeMenuViewModel =
                new ViewModelProvider(this).get(InformativeMenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_informative_menu, container, false);
        final TextView textView = root.findViewById(R.id.text_informative_menu);
        informativeMenuViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}