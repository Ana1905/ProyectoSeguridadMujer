package com.example.proyectoseguridadmujer.ui.comunity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Dialogs.DialogNewPostFragment;
import Dialogs.DialogReportPublication;
import Dialogs.DialogShowCommunityPublication;
import Dialogs.DialogUserProfile;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoseguridadmujer.ListAdapter;
import com.example.proyectoseguridadmujer.ListElement;
import com.example.proyectoseguridadmujer.LoginActivity;
import com.example.proyectoseguridadmujer.R;
import com.example.proyectoseguridadmujer.Sancion;
import com.example.proyectoseguridadmujer.SanctionAlert;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ComunityFragment extends Fragment implements DialogNewPostFragment.ListenerNewPostFragment, DialogReportPublication.ListenerReportPublication, DialogShowCommunityPublication.ListenerShowCommunityPublication, DialogUserProfile.ListenerUserProfile {

    private String email="";

    String category = "";

    RecyclerView publicationList;
    ListAdapter PublicationListAdapter;

    EditText SearchBar;
    Spinner FilterSpinner;
    List<ListElement> listElements;
    Button mButtonNewPost;

    ArrayList<Sancion> mListaSanciones = new ArrayList<Sancion>();
    RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_comunity, container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");
        //RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.PublicationRecyclerView);
        //init(recyclerView);

        //WiringUp
        publicationList = root.findViewById(R.id.PublicationRecyclerView);
        publicationList.setAdapter(PublicationListAdapter);
        publicationList.setLayoutManager(new LinearLayoutManager(getActivity()));

        getPublicationList("https://seguridadmujer.com/app_movil/Community/getPublicationList.php?email="+email);

        SearchBar = root.findViewById(R.id.search_post_comunity);
        SearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                if(!SearchBar.getText().equals("")){
                    FilterSpinner.setSelection(0);
                }

                 */
                if(SearchBar.getText().toString().isEmpty()){
                    FilterSpinner.setEnabled(true);
                }
                else{
                    FilterSpinner.setEnabled(false);
                }

                PublicationListAdapter.getFilter().filter(SearchBar.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        FilterSpinner = root.findViewById(R.id.FilterSpinnerCommunity);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.categories, android.R.layout.simple_spinner_item);
        FilterSpinner.setAdapter(adapterSpinner);

        FilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        category="";
                        break;
                    case 1:
                        category="Contar una experiencia sobre violencia";
                        break;
                    case 2:
                        category="Anuncio importante sobre la seguridad";
                        break;
                    case 3:
                        category="Reporte de acontecimiento";
                        break;
                    case 4:
                        category="Otro";
                        break;
                }

                if(position != 0){
                    SearchBar.setEnabled(false);
                }
                else{
                    SearchBar.setEnabled(true);
                }

                if(PublicationListAdapter != null){
                    if(position != 0){
                        PublicationListAdapter.getFilter().filter(String.valueOf(position));
                    }
                    else{
                        PublicationListAdapter.getFilter().filter("");
                    }
                }

                //SearchBar.setText(category);
                //TextViewCategory.setText(category);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mButtonNewPost = root.findViewById(R.id.new_post_button);
        mButtonNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerSanciones("https://seguridadmujer.com/app_movil/Community/ObtenerSanciones.php?email="+email);
            }
        });
        //  init();

        return root;
    }

    public void getPublicationList (String Link)
    {
        listElements = new ArrayList<ListElement>();
        //Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Link, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                String PublicationData = response.toString();
                Gson gson = new Gson();
                ListElement[] PublicationRegister = gson.fromJson(PublicationData, ListElement[].class);

                listElements = Arrays.asList(PublicationRegister);

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
        ArrayList<ListElement> arrayList = new ArrayList<ListElement>(listElements);
        PublicationListAdapter = new ListAdapter(getActivity(), arrayList,ComunityFragment.this, ComunityFragment.this, ComunityFragment.this);
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

    private void showNewPostDialog(){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogNewPostFragment dialogNewPostFragment = new DialogNewPostFragment();

        dialogNewPostFragment.setListener(ComunityFragment.this);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.add(android.R.id.content, dialogNewPostFragment).addToBackStack(null).commit();

    }

    //Metodo que valida si hay un baneo de cuenta:
    private void obtenerSanciones(String URL){

        //Vacia la lista:
        if(!mListaSanciones.isEmpty()){
            mListaSanciones.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        Sancion sancion = new Sancion();
                        sancion.setID(jsonObject.getInt("ID_Baneo"));
                        sancion.setID_Usuaria(jsonObject.getInt("ID_Usuaria"));
                        sancion.setDuracion(jsonObject.getInt("Duracion"));
                        sancion.setFechaInicio(jsonObject.getString("FechaInicio"));
                        sancion.setFechaFin(jsonObject.getString("FechaFin"));
                        sancion.setTipoSancion(jsonObject.getString("Tipo"));

                        mListaSanciones.add(sancion);
                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                if(mListaSanciones.isEmpty()){
                    showNewPostDialog();
                }
                else{
                    //Toast.makeText(getApplicationContext(), "Mamaste", Toast.LENGTH_SHORT).show();

                    String mensajeSancion = "";
                    ArrayList<Integer> listaDuraciones = new ArrayList<Integer>();

                    for(int i=0; i<mListaSanciones.size(); i++){
                        listaDuraciones.add(mListaSanciones.get(i).getDuracion());
                    }

                    for(int i=0; i<mListaSanciones.size(); i++){
                        if(mListaSanciones.get(i).getDuracion() == Collections.max(listaDuraciones)){
                            if(mListaSanciones.get(i).getTipoSancion().equals("Bloquear interaccion completa en modulo de Comunidad")){
                                if(mListaSanciones.get(i).getDuracion() == 876000){
                                    mensajeSancion = "Lo sentimos, la interaccion con el modulo de comunidad se le ha sido bloqueada de forma permanente.";
                                }
                                else{
                                    mensajeSancion = "Lo sentimos, la interaccion con el modulo de comunidad se le ha sido bloqueada, podra volver a acceder el dia "+mListaSanciones.get(i).getFechaFin();
                                }
                            }
                            else if(mListaSanciones.get(i).getTipoSancion().equals("Bloquear funcion de publicacion")){
                                if(mListaSanciones.get(i).getDuracion() == 876000){
                                    mensajeSancion = "Lo sentimos, la funcion para crear publicaciones se le ha sido bloqueada de forma permanente.";
                                }
                                else{
                                    mensajeSancion = "Lo sentimos, la funcion para crear publicaciones se le ha sido bloqueada, podra volver a acceder el dia "+mListaSanciones.get(i).getFechaFin();
                                }
                            }

                        }
                    }

                    Intent intent = new Intent(getActivity(), SanctionAlert.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("mensaje", mensajeSancion);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void returnNewPostData(int result) {
        getPublicationList("https://seguridadmujer.com/app_movil/Community/getPublicationList.php?email="+email);
    }

    @Override
    public void returnDataReportPublication(int result) {
        getPublicationList("https://seguridadmujer.com/app_movil/Community/getPublicationList.php?email="+email);
    }

    @Override
    public void returnDataShowCommunityPublication(int result) {
        getPublicationList("https://seguridadmujer.com/app_movil/Community/getPublicationList.php?email="+email);
    }

    @Override
    public void returnUserProfile(int result) {
        getPublicationList("https://seguridadmujer.com/app_movil/Community/getPublicationList.php?email="+email);
    }
}