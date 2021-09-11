package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import Dialogs.DialogNewPostFragment;

public class BodyDefenseActivity extends AppCompatActivity {

    String email="";
    Button mButton;

    RecyclerView mRecyclerView;

    String Titles[], Descriptions[];
    int Images[] = {R.drawable.building, R.drawable.icon_test, R.drawable.instagram_profile_image, R.drawable.logo_app,
    R.drawable.logo_transparent, R.drawable.selfdefense_icon1, R.drawable.selfdefense_icon2};

    List<BodyDefenseTechniques> ListBodyDefense;
    BodyDefenseAdapter bodyDefenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_defense);

        //Wiring Up
        Titles = getResources().getStringArray(R.array.Test_values_Recycler_view);
        Descriptions = getResources().getStringArray(R.array.Test_values_Descriptions_Recycler_view);
        mRecyclerView = findViewById(R.id.bodyDefenseRecyclerView);

        bodyDefenseAdapter = new BodyDefenseAdapter(this, Titles, Descriptions, Images);

        mRecyclerView.setAdapter(bodyDefenseAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

       // Toast.makeText(getApplicationContext(), "OnCreate", Toast.LENGTH_SHORT).show();
        getCredentialData();

        getBodyList(getString(R.string.Body_defense_url));

        //getBodyDefenseList();


        /*
        mButton=findViewById(R.id.test);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewPostFragment dialogNewPostFragment = new DialogNewPostFragment();
                dialogNewPostFragment.show(getSupportFragmentManager(),"Nueva publicacion");
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(BodyDefenseActivity.this);
        startActivity(intent);
        finish();
    }

    public void getCredentialData(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public void getBodyList(String Link)
    {
        /*
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, Link, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Huevos cocidos");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        */



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Link, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {

                String Nepe = response.toString();
                Gson gson = new Gson();
                BodyDefenseTechniques[] Nepasio = gson.fromJson(Nepe, BodyDefenseTechniques[].class);
                JSONObject jsonObject = null;

                /*
                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        String Id = jsonObject.getString("IdTecnica");
                        String Name = jsonObject.getString("Titulo");
                        String Group = jsonObject.getString("Seccion");
                        String TgetString("Comentario");
                        int Image = jsonObject.getInt("Imageext = jsonObject.n");
                        String Video = jsonObject.getString("Video");

                        ListBodyDefense.add(new BodyDefenseTechniques(Id, Name, Group, Text, Image, Video));

                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        //e.printStackTrace();
                    }
                }
                */
                ListBodyDefense = Arrays.asList(Nepasio);

                bodyDefenseAdapter = new BodyDefenseAdapter(BodyDefenseActivity.this, ListBodyDefense, Images);
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

    public void getBodyDefenseList()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.Body_defense_url),
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("BodyDefense");

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            ListBodyDefense.add
                            (
                                new BodyDefenseTechniques
                                (
                                    jsonObject1.getInt("idTecnica"),
                                    jsonObject1.getString("Titulo"),
                                    jsonObject1.getString("Seccion"),
                                    jsonObject1.getString("Comentario"),
                                    jsonObject1.getInt("Imagen"),
                                    jsonObject1.getString("Video")
                                )
                            );
                        }

                        bodyDefenseAdapter = new BodyDefenseAdapter(BodyDefenseActivity.this, ListBodyDefense, Images);
                        mRecyclerView.setAdapter(bodyDefenseAdapter);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            },

            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    error.printStackTrace();
                }
            }
        );

        requestQueue.add(stringRequest);
    }
}