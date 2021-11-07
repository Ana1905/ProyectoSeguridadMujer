package com.example.proyectoseguridadmujer.ui.comunity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Dialogs.DialogNewPostFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoseguridadmujer.InstitutionListActivity;
import com.example.proyectoseguridadmujer.ListAdapter;
import com.example.proyectoseguridadmujer.ListElement;
import com.example.proyectoseguridadmujer.R;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

public class ComunityFragment extends Fragment {

    private String email="";

    RecyclerView publicationList;
    ListAdapter PublicationListAdapter;

    List<ListElement> PublicationList;
    Button mButtonNewPost;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.fragment_comunity);

        //WiringUp
        publicationList = getActivity().findViewById(R.id.PublicationRecyclerView);
        //publicationList.setAdapter(PublicationListAdapter);
        publicationList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mButtonNewPost = getActivity().findViewById(R.id.new_post_button);
        mButtonNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Crear nueva publicación", Toast.LENGTH_SHORT).show();
                showNewPostDialog();
            }
        });

        getPublicationList(getString(R.string.Publication_list_url));
      //  init();
    }
    public void getPublicationList (String Link)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Link, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                String PublicationData = response.toString();
                Gson gson = new Gson();
                ListElement[] PublicationRegister = gson.fromJson(PublicationData, ListElement[].class);

                PublicationList = Arrays.asList(PublicationRegister);

                setAdapter();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void setAdapter ()
    {
        PublicationListAdapter = new ListAdapter(getActivity(), PublicationList);
        publicationList.setAdapter(PublicationListAdapter);
    }



    private void init(RecyclerView recyclerView) {
        //creo que aqui tendrias que obtener de la BD
        //PublicationList = new ArrayList<>();
        //PublicationList.add(new ListElement("Cindy", "Acoso","Hola soy cindy y fui acosada sexualmente"));
        //PublicationList.add(new ListElement("Lucy", "Violencia fisica","Hola soy Lucy y fui acosada fisicamente"));
        //PublicationList.add(new ListElement("Cindy", "Acoso","Hola soy cindy y fui acosada sexualmente"));
        //PublicationList.add(new ListElement("Cindy", "Acoso","Hola soy cindy y fui acosada sexualmente"));
        //PublicationList.add(new ListElement("Cindy", "Acoso","Hola soy cindy y fui acosada sexualmente"));
        //ListAdapter listAdapter = new ListAdapter(PublicationList, getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(PublicationListAdapter);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_comunity, container, false);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");
        //RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.PublicationRecyclerView);
        //init(recyclerView);

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
