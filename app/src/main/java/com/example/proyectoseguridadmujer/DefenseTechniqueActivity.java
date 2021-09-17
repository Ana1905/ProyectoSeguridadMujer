package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.bumptech.glide.Glide;

public class DefenseTechniqueActivity extends AppCompatActivity
{
    TextView TechniqueTitle, TechniqueContent;
    ImageView TechniqueImage;
    VideoView TechniqueVideo;
    FrameLayout VideoLayout;
    DefenseTechniques defenseTechniques;
    Bundle bundle;

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

        VideoLayout = findViewById(R.id.VideoLayout);
        TechniqueVideo = findViewById(R.id.TechniqueVideo);
        MediaController mediaController = new MediaController(this);
        TechniqueVideo.setMediaController(mediaController);
        mediaController.setAnchorView(VideoLayout);

        bundle = getIntent().getExtras();
        defenseTechniques = bundle.getParcelable("Technique");

        getCredentialData();

        ChangeTechniqueData();
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
}
