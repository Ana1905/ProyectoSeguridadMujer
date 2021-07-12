package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import httpurlconnection.PutData;

public class BodyDefenseActivity extends AppCompatActivity {

    String email="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_defense);
       // Toast.makeText(getApplicationContext(), "OnCreate", Toast.LENGTH_SHORT).show();
       getCredentialData();

    }


    public void getCredentialData(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }








}