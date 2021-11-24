package com.example.proyectoseguridadmujer.ui.help;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoseguridadmujer.HelpingNetworkAdapter;
import com.example.proyectoseguridadmujer.HelpingNetworkPublication;
import com.example.proyectoseguridadmujer.MainActivity;
import com.example.proyectoseguridadmujer.R;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Dialogs.DialogNewHelpPostFragment;

public class HelpingNetworkFragment extends Fragment implements DialogNewHelpPostFragment.ListenerNewHelpPostFragment
{
    private String email="";

    String category = "";

    RecyclerView HelpPublication;
    HelpingNetworkAdapter adapter;
    EditText SearchBar;
    Spinner FilterSpinner;
    List<HelpingNetworkPublication> Publication;
    Button newPost;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_helping_network, container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");

        //WiringUp
        HelpPublication = root.findViewById(R.id.SupportPublicationRecyclerView);
        //HelpPublication.setAdapter(adapter);
        HelpPublication.setLayoutManager(new LinearLayoutManager(getActivity()));

        getHelpPublicationList("https://seguridadmujer.com/app_movil/HelpingNetwork/getHelpPublicationList.php?email="+email);

        SearchBar = root.findViewById(R.id.search_post_support);
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

                adapter.getFilter().filter(SearchBar.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        FilterSpinner = root.findViewById(R.id.FilterSpinner);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.Helping_Network_Categories, android.R.layout.simple_spinner_item);
        FilterSpinner.setAdapter(adapterSpinner);

        FilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        category="";
                        break;
                    case 1:
                        category="Apoyo sobre violencia doméstica";
                        break;
                    case 2:
                        category="Apoyo sobre discriminación";
                        break;
                    case 3:
                        category="Apoyo sobre abuso sexual";
                        break;
                    case 4:
                        category="Apoyo sobre un suceso de acoso";
                        break;
                    case 5:
                        category="Apoyo sobre un acontecimiento de abuso infantil";
                        break;
                    case 6:
                        category="Apoyo sobre violencia de pareja";
                        break;
                }

                if(position != 0){
                    SearchBar.setEnabled(false);
                }
                else{
                    SearchBar.setEnabled(true);
                }

                if(adapter != null){
                    if(position != 0){
                        adapter.getFilter().filter(String.valueOf(position));
                    }
                    else{
                        adapter.getFilter().filter("");
                    }
                }

                //SearchBar.setText(category);
                //TextViewCategory.setText(category);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
        TextViewCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(getContext(), "Cambio", Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter(TextViewCategory.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

         */

        newPost = root.findViewById(R.id.new_support_post_button);
        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Crear nueva publicación", Toast.LENGTH_SHORT).show();
                HelpNewPostDialog();
            }
        });

        return root;
    }

    public void getHelpPublicationList(String Link)
    {
        Publication = new ArrayList<HelpingNetworkPublication>();
        //Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Link, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                String PublicationData = response.toString();
                Gson gson = new Gson();
                HelpingNetworkPublication[] PublicationRegister = gson.fromJson(PublicationData, HelpingNetworkPublication[].class);

                Publication = Arrays.asList(PublicationRegister);
                //Publication = Arrays.asPublicationRegister;
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
        ArrayList<HelpingNetworkPublication> arrayList = new ArrayList<HelpingNetworkPublication>(Publication);
        adapter = new HelpingNetworkAdapter(getActivity(), arrayList);
        HelpPublication.setAdapter(adapter);
    }

    private void HelpNewPostDialog()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogNewHelpPostFragment dialogNewHelpPostFragment = new DialogNewHelpPostFragment();

        dialogNewHelpPostFragment.setListener(HelpingNetworkFragment.this);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.add(android.R.id.content, dialogNewHelpPostFragment).addToBackStack(null).commit();

    }

    public void onBackPressed()
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void returnNewHelpPostData(int result) {
        getHelpPublicationList("https://seguridadmujer.com/app_movil/HelpingNetwork/getHelpPublicationList.php?email="+email);
    }
}