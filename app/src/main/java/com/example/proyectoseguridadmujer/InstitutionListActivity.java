package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

public class InstitutionListActivity extends AppCompatActivity {

    String email = "";

    Button mBotonCambiarLista;

    RecyclerView institutionList;

    List<Institutions> InstitutionList;
    InstitutionListAdapter institutionListAdapter;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_list);

        //WiringUp
        institutionList = findViewById(R.id.InstitutionList);
        mBotonCambiarLista = findViewById(R.id.change_list_button_institution);

        institutionList.setAdapter(institutionListAdapter);
        institutionList.setLayoutManager(new LinearLayoutManager(this));

        getCredentialData();

        getInstitutionList(getString(R.string.Institution_list_url));

        mBotonCambiarLista.setText(R.string.indentificar_institucion);
        mBotonCambiarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = NavUtils.getParentActivityIntent(InstitutionListActivity.this);
        startActivity(intent);
        finish();
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public void getInstitutionList(String Link)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Link, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                String InstitutionData = response.toString();
                Gson gson = new Gson();
                Institutions[] InstitutionsRegister = gson.fromJson(InstitutionData, Institutions[].class);

                InstitutionList = Arrays.asList(InstitutionsRegister);

                institutionListAdapter = new InstitutionListAdapter(InstitutionListActivity.this, InstitutionList);
                institutionList.setAdapter(institutionListAdapter);
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


}