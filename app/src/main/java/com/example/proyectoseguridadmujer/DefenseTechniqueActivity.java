package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

public class DefenseTechniqueActivity extends AppCompatActivity
{
    TextView TechniqueTitle, TechniqueContent;
    ImageView TechniqueImage;
    VideoView TechniqueVideo;

    String email = "";

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defense_technique);

        //WiringUp
        TechniqueTitle = findViewById(R.id.TechniqueTitle);
        TechniqueContent = findViewById(R.id.TechniqueContent);
        TechniqueImage = findViewById(R.id.TechniqueImage);
        TechniqueVideo = findViewById(R.id.TechniqueVideo);

        getCredentialData();
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

}
