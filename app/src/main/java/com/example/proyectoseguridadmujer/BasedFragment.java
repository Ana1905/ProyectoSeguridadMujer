package com.example.proyectoseguridadmujer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.proyectoseguridadmujer.R;


public class BasedFragment extends Fragment {

    public void changeFragment (Fragment fragment, int id) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(id, fragment).commit();
    }
}
