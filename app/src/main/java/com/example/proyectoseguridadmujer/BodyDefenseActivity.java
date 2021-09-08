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

import Dialogs.DialogNewPostFragment;

public class BodyDefenseActivity extends AppCompatActivity {

    String email="";
    Button mButton;

    RecyclerView mRecyclerView;

    String Titles[], Descriptions[];
    int Images[] = {R.drawable.building, R.drawable.icon_test, R.drawable.instagram_profile_image, R.drawable.logo_app,
    R.drawable.logo_transparent, R.drawable.selfdefense_icon1, R.drawable.selfdefense_icon2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_defense);

        //Wiring Up
        Titles = getResources().getStringArray(R.array.Test_values_Recycler_view);
        Descriptions = getResources().getStringArray(R.array.Test_values_Descriptions_Recycler_view);
        mRecyclerView = findViewById(R.id.bodyDefenseRecyclerView);

        BodyDefenseAdapter bodyDefenseAdapter = new BodyDefenseAdapter(this, Titles, Descriptions, Images);

        mRecyclerView.setAdapter(bodyDefenseAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

       // Toast.makeText(getApplicationContext(), "OnCreate", Toast.LENGTH_SHORT).show();
        getCredentialData();


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

}