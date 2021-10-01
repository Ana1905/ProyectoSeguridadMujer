package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URI;

public class ListContentActivity extends FragmentActivity implements OnMapReadyCallback
{
    TextView InstitutionName, InstitutionArea, InstitutionDescription, InstitutionPhone, InstitutionPage;
    ImageView InstitutionImage;
    private GoogleMap mMap;
    Institutions institutions;
    Bundle bundle;

    String email = "";

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_content);

        //WiringUp
        InstitutionName = findViewById(R.id.InstitutionNameLabel);
        InstitutionImage = findViewById(R.id.InstitutionImageHolder);
        InstitutionArea = findViewById(R.id.InstitutionAreaContent);
        InstitutionDescription = findViewById(R.id.InstitutionDescription);
        InstitutionPhone = findViewById(R.id.InstitutionPhone);
        InstitutionPage = findViewById(R.id.InstitutionPage);

        bundle = getIntent().getExtras();
        institutions = bundle.getParcelable("Institution");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getCredentialData();

        ChangeInstitutionData();
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
        InstitutionArea.setText(institutions.getArea());
        //InstitutionArea.setText("áéíóú");
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
}
