package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Dialogs.DialogNewPostFragment;

public class BodyDefenseActivity extends AppCompatActivity {

    String email="";
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_defense);
       // Toast.makeText(getApplicationContext(), "OnCreate", Toast.LENGTH_SHORT).show();
        getCredentialData();
        mButton=findViewById(R.id.test);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewPostFragment dialogNewPostFragment = new DialogNewPostFragment();
                dialogNewPostFragment.show(getSupportFragmentManager(),"Nueva publicacion");
            }
        });
    }


    public void getCredentialData(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }








}