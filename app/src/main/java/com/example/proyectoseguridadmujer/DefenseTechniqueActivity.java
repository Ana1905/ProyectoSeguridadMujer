package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DefenseTechniqueActivity extends AppCompatActivity
{
    TextView TechniqueTitle, TechniqueContent;
    ImageView TechniqueImage;
    VideoView TechniqueVideo;
    FrameLayout VideoLayout;
    DefenseTechniques defenseTechniques;
    Bundle bundle;
    RecyclerView mRecyclerView;

    String email = "";

    ArrayList<String> mListaImagenes = new ArrayList<String>();

    RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defense_technique);

        //Action Bar Color:
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Action_Bar_Color)));

        //WiringUp
        TechniqueTitle = findViewById(R.id.TechniqueTitle);
        TechniqueContent = findViewById(R.id.TechniqueContent);
        TechniqueImage = findViewById(R.id.TechniqueImage);

        mRecyclerView = findViewById(R.id.ListViewImagenesDefensa);
        //mRecyclerView.setAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        VideoLayout = findViewById(R.id.VideoLayout);
        TechniqueVideo = findViewById(R.id.TechniqueVideo);
        MediaController mediaController = new MediaController(this);
        TechniqueVideo.setMediaController(mediaController);
        mediaController.setAnchorView(VideoLayout);

        bundle = getIntent().getExtras();
        defenseTechniques = bundle.getParcelable("Technique");

        getCredentialData();

        ChangeTechniqueData();

        obtenerImagenes("https://seguridadmujer.com/app_movil/Defensa/obtenerImagenesAdicionales.php?ID_Publicacion="+defenseTechniques.getID_PublicacionDefensa());
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = NavUtils.getParentActivityIntent(DefenseTechniqueActivity.this);
        startActivity(intent);
        finish();
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public void ChangeTechniqueData()
    {
        TechniqueTitle.setText(defenseTechniques.getTitulo());
        Glide.with(this).load(defenseTechniques.getRutaImagenPresentacion()).into(TechniqueImage);
        TechniqueContent.setText(defenseTechniques.getContenido());
        TechniqueVideo.setVideoPath(defenseTechniques.getRutaVideo());
    }

    //Metodo para obtener los reportes de acontecimiento de la base de datos aun en su tiempo de vida:
    public void obtenerImagenes(String URL){
        //Vacia la lista:
        if(!mListaImagenes.isEmpty()){
            mListaImagenes.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        mListaImagenes.add(jsonObject.getString("RutaImagen"));
                        //Toast.makeText(getApplicationContext(), jsonObject.getString("RutaImagen"), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                mostrarImagenesAdicionales();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void mostrarImagenesAdicionales(){
        if(!mListaImagenes.isEmpty()){
            mRecyclerView.setVisibility(View.VISIBLE);
            ImageAdapter imageAdapter = new ImageAdapter(this, mListaImagenes);
            mRecyclerView.setAdapter(imageAdapter);
        }
        else{
            mRecyclerView.setVisibility(View.GONE);
        }
    }
}