package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectoseguridadmujer.Institutions;
import com.example.proyectoseguridadmujer.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

public class ListContentActivity extends AppCompatActivity implements OnMapReadyCallback
{
    Button mBotonVolver;
    TextView InstitutionName, InstitutionArea, InstitutionDescription, InstitutionPhone, InstitutionPage, redesSocialesLabel;
    ImageView InstitutionImage;
    private GoogleMap mMap;
    Institutions institutions;
    Bundle bundle;

    RecyclerView mRecyclerView;
    RecyclerView mRecyclerViewRedesSociales;

    String email = "";

    ArrayList<String> mListaImagenes = new ArrayList<String>();
    ArrayList<SocialMedia> mListaRedesSociales = new ArrayList<>();

    RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_content);

        //Action Bar Color:
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Action_Bar_Color)));

        //WiringUp
        mBotonVolver = findViewById(R.id.button_dialog_institution_back);
        InstitutionName = findViewById(R.id.InstitutionNameLabel);
        InstitutionImage = findViewById(R.id.InstitutionImageHolder);
        InstitutionArea = findViewById(R.id.InstitutionAreaContent);
        InstitutionDescription = findViewById(R.id.InstitutionDescription);
        InstitutionPhone = findViewById(R.id.InstitutionPhone);
        InstitutionPage = findViewById(R.id.InstitutionPage);
        redesSocialesLabel = findViewById(R.id.redes_sociales_instituciones);

        mRecyclerView = findViewById(R.id.recyclerImagenesInstituciones);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        mRecyclerViewRedesSociales = findViewById(R.id.recyclerRedesSociales);
        mRecyclerViewRedesSociales.setLayoutManager(new LinearLayoutManager(this));

        bundle = getIntent().getExtras();
        institutions = bundle.getParcelable("Institution");

        mBotonVolver.setVisibility(View.GONE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getCredentialData();

        ChangeInstitutionData();

        obtenerImagenes("https://seguridadmujer.com/app_movil/Institucion/obtenerImagenesAdicionales.php?ID_Publicacion="+institutions.getID_Institucion());

        obtenerRedesSociales("https://seguridadmujer.com/app_movil/Institucion/obtenerRedesSociales.php?ID_Publicacion="+institutions.getID_Institucion());
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = NavUtils.getParentActivityIntent(ListContentActivity.this);
        startActivity(intent);
        finish();
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public void ChangeInstitutionData ()
    {
        InstitutionName.setText(institutions.getNombre());
        Glide.with(this).load(institutions.getRutaImagenPresentacion()).into(InstitutionImage);
        InstitutionArea.setText(institutions.getNombreCategoria());
        InstitutionDescription.setText(institutions.getDescripcion());
        InstitutionPhone.setText(institutions.getTelefono());
        InstitutionPage.setText(institutions.getPaginaWeb());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);
        LatLng ubication = new LatLng(institutions.getLatitud(), institutions.getLongitud());
        mMap.addMarker(new MarkerOptions().position(ubication).title("Institucion ubicacion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ubication));
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

    public void obtenerRedesSociales(String URL){
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

                        SocialMedia socialMedia = new SocialMedia();
                        socialMedia.setTipoRedSocial(jsonObject.getString("NombreRedSocial"));
                        socialMedia.setNombreRedSocial(jsonObject.getString("Enlace"));
                        mListaRedesSociales.add(socialMedia);

                        //Toast.makeText(getApplicationContext(), jsonObject.getString("RutaImagen"), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                mostrarRedesSociales();
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

    private void mostrarRedesSociales(){
        if(!mListaRedesSociales.isEmpty()){
            redesSocialesLabel.setVisibility(View.VISIBLE);
            mRecyclerViewRedesSociales.setVisibility(View.VISIBLE);
            ImageAdapter imageAdapter = new ImageAdapter(this, mListaImagenes);
            SocialMediaAdapter socialMediaAdapter = new SocialMediaAdapter(this, mListaRedesSociales);
            mRecyclerViewRedesSociales.setAdapter(socialMediaAdapter);
        }
        else{
            redesSocialesLabel.setVisibility(View.GONE);
            mRecyclerViewRedesSociales.setVisibility(View.GONE);
        }
    }

}