package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.bumptech.glide.Glide;

public class ListContentActivity extends AppCompatActivity
{
    TextView InstitutionName, InstitutionArea, InstitutionDescription, InstitutionPhone, InstitutionPage;
    ImageView InstitutionImage;
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
        InstitutionDescription.setText(institutions.getDescripcion());
        InstitutionPhone.setText(institutions.getTelefono());
        InstitutionPage.setText(institutions.getPaginaWeb());
    }
}
