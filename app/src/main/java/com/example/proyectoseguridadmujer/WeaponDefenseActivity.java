package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

public class WeaponDefenseActivity extends AppCompatActivity
{
    String email = "";

    RecyclerView mRecyclerView;

    List<DefenseTechniques> ListWeaponDefense;
    WeaponDefenseAdapter weaponDefenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defense);

        //Wiring Up
        mRecyclerView = findViewById(R.id.DefenseRecyclerView);

        mRecyclerView.setAdapter(weaponDefenseAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getCredentialData();

        getWeaponList(getString(R.string.Weapon_defense_url));
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = NavUtils.getParentActivityIntent(WeaponDefenseActivity.this);
        startActivity(intent);
        finish();
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public void getWeaponList(String Link)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Link, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                String BodyDefenseData = response.toString();
                Gson gson = new Gson();
                DefenseTechniques[] BodyDefenseRegister = gson.fromJson(BodyDefenseData, DefenseTechniques[].class);

                ListWeaponDefense = Arrays.asList(BodyDefenseRegister);

                weaponDefenseAdapter = new WeaponDefenseAdapter(WeaponDefenseActivity.this, ListWeaponDefense);
                mRecyclerView.setAdapter(weaponDefenseAdapter);
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