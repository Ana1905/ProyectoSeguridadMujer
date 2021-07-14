package com.example.proyectoseguridadmujer.ui.comunity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Dialogs.DialogNewPostFragment;
import com.example.proyectoseguridadmujer.ListAdapter;
import com.example.proyectoseguridadmujer.ListElement;
import com.example.proyectoseguridadmujer.R;

import java.util.ArrayList;
import java.util.List;

public class ComunityFragment extends Fragment {
    private String email="";
    List<ListElement> elements;
    Button mButtonNewPost;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  init();
    }

    private void init(RecyclerView recyclerView) {
        //creo que aqui tendrias que obtener de la BD
        elements = new ArrayList<>();
        elements.add(new ListElement("Cindy", "Acoso","Hola soy cindy y fui acosada sexualmente"));
        elements.add(new ListElement("Lucy", "Violencia fisica","Hola soy Lucy y fui acosada fisicamente"));
        elements.add(new ListElement("Cindy", "Acoso","Hola soy cindy y fui acosada sexualmente"));
        elements.add(new ListElement("Cindy", "Acoso","Hola soy cindy y fui acosada sexualmente"));
        elements.add(new ListElement("Cindy", "Acoso","Hola soy cindy y fui acosada sexualmente"));
        ListAdapter listAdapter = new ListAdapter(elements, getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_comunity, container, false);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        init(recyclerView);

        mButtonNewPost = (Button) root.findViewById(R.id.new_post_button);
        mButtonNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Crear nueva publicación", Toast.LENGTH_SHORT).show();
                showNewPostDialog();
            }
        });

        return root;
    }

    private void showNewPostDialog(){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogNewPostFragment dialogNewPostFragment = new DialogNewPostFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.add(android.R.id.content, dialogNewPostFragment).addToBackStack(null).commit();


    }







}
