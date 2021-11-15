package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

public class BodyDefenseActivity extends AppCompatActivity
{
    String email="";

    TextView mTVTitle;

    Button mBotonCambiarLista;

    RecyclerView mRecyclerView;

    List<DefenseTechniques> ListBodyDefense;
    BodyDefenseAdapter bodyDefenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defense);

        //Action Bar Color:
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Action_Bar_Color)));

        //Wiring Up
        mTVTitle = findViewById(R.id.self_defense_title);
        mRecyclerView = findViewById(R.id.DefenseRecyclerView);
        mBotonCambiarLista = findViewById(R.id.change_list_button);

        mRecyclerView.setAdapter(bodyDefenseAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getCredentialData();

        getBodyList(getString(R.string.Body_defense_url));

        mTVTitle.setText(R.string.body_defense_title);
        mBotonCambiarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WeaponDefenseActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = NavUtils.getParentActivityIntent(BodyDefenseActivity.this);
        startActivity(intent);
        finish();
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public void getBodyList(String Link)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Link, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                String BodyDefenseData = response.toString();
                Gson gson = new Gson();
                DefenseTechniques[] BodyDefenseRegister = gson.fromJson(BodyDefenseData, DefenseTechniques[].class);

                ListBodyDefense = Arrays.asList(BodyDefenseRegister);

                bodyDefenseAdapter = new BodyDefenseAdapter(BodyDefenseActivity.this, ListBodyDefense);
                mRecyclerView.setAdapter(bodyDefenseAdapter);
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