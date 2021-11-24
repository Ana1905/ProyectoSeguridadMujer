package com.example.proyectoseguridadmujer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import httpurlconnection.PutData;

public class AddTrustedFriendsActivity extends AppCompatActivity {

    ImageView mImageViewIcon, mImageUserIcon;
    TextView mTextViewLabel;
    Button mButtonAddFriend, mButtonAdd, mButtonConfirm, mButtonPulsera, mButtonConfigurarAlerta;
    EditText mNombre;
    EditText mTel;
    RecyclerView recyclerView;

    String nombre,number;

    ArrayList<Contact> ListContacts=new ArrayList<Contact>();

    RequestQueue requestQueue;
    String email = " ";
    boolean showList=true;
    static final int PICK_CONTACT_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trusted_friends);

        //Wiring up
        mImageViewIcon= findViewById(R.id.imageViewAdd);
        mTextViewLabel=findViewById(R.id.TextViewLabeladd);
        mButtonAddFriend=findViewById(R.id.alert_Add_friend);
        mNombre= findViewById(R.id.nombre);
        mTel= findViewById(R.id.telefono);
        mButtonAdd = findViewById(R.id.alert_Add);
        recyclerView = findViewById(R.id.recyclerViewContacts);
        mButtonConfirm=findViewById(R.id.add_confirm_button);
        mButtonPulsera = findViewById(R.id.button_pulsera);
        mButtonConfigurarAlerta = findViewById(R.id.button_configurar_alerta);
        mImageUserIcon = findViewById(R.id.user_image);

        //Obtiene el email de la usuaria:
        getCredentialData();

        //Obtiene los contactos de confianza:
        checkContacts();

        //
        LoadView();

        //mButtonPulsera.setVisibility(View.INVISIBLE);
        //mButtonConfigurarAlerta.setVisibility(View.INVISIBLE);
        //mButtonConfirm.findViewById(R.id.add_confirm_button);

        //ver si tiene amigos o no
        //si tiene mandar a la vista de lista de amigs
        //Si no , quedarse aqui

        mButtonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContact();
            }
        });

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ListContacts.size()<10){
                    selectContact();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Ha excedido el total de usuarios de confianza.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = mNombre.getText().toString();
                saveContact(nombre,number);
            }
        });

        mButtonPulsera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BandVinculationActivity.class);
                startActivity(intent);
            }
        });

        mButtonConfigurarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfigureAlertActivity.class);
                startActivity(intent);
            }
        });
    }

    public void changeView() {
      //  Toast.makeText(getApplicationContext(),"changeView",Toast.LENGTH_SHORT).show();

        if(ListContacts.isEmpty()){
            mButtonConfigurarAlerta.setVisibility(View.INVISIBLE);
            mButtonAdd.setVisibility(View.INVISIBLE);

            mButtonPulsera.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

            mTextViewLabel.setVisibility(View.VISIBLE);
            mImageViewIcon.setVisibility(View.VISIBLE);
            mButtonAddFriend.setVisibility(View.VISIBLE);

            mImageUserIcon.setVisibility(View.INVISIBLE);
            mNombre.setVisibility(View.INVISIBLE);
            mTel.setVisibility(View.INVISIBLE);
            mButtonConfirm.setVisibility(View.INVISIBLE);
        }
        else{
            mButtonConfigurarAlerta.setVisibility(View.VISIBLE);
            mButtonAdd.setVisibility(View.VISIBLE);

            mButtonPulsera.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            mTextViewLabel.setVisibility(View.INVISIBLE);
            mImageViewIcon.setVisibility(View.INVISIBLE);
            mButtonAddFriend.setVisibility(View.INVISIBLE);

            mImageUserIcon.setVisibility(View.INVISIBLE);
            mNombre.setVisibility(View.INVISIBLE);
            mTel.setVisibility(View.INVISIBLE);
            mButtonConfirm.setVisibility(View.INVISIBLE);

            showListofContacts();
        }
    }

    public void checkContacts() {
        obtenerContactos("https://seguridadmujer.com/app_movil/Consultas/obtenerContactosDeConfianza.php?email=" + email);
    }

    public void obtenerContactos(String URL){
        ListContacts.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                if(jsonArray.length()!=0) {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        try {
                            jsonObject = jsonArray.getJSONObject(i);

                            Contact contact = new Contact();

                            contact.setID_Contacto(jsonObject.getInt("ID_Contacto"));
                            contact.setNombre(jsonObject.getString("NombreContacto"));
                            contact.setNumero(jsonObject.getString("Telefono"));

                            ListContacts.add(contact);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                    changeView();

                }
                else{

                    showList = false;
                    changeView();
                   // Toast.makeText(getApplicationContext(),"No hay contactos",Toast.LENGTH_SHORT).show();

                }
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


    public void showListofContacts(){

        recyclerView= findViewById(R.id.recyclerViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //AdapterContacts adapter= new AdapterContacts(getApplicationContext(), ListContacts);
        //recyclerView.setAdapter(adapter);

        /*
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ListContacts= new ArrayList<Contact>();

        for(int i=0;i<50;i++){
            Contact contact = new Contact("hola" , "2");
            ListContacts.add(contact);
        }

        AdapterContacts adapter= new AdapterContacts(ListContacts);
        recyclerView.setAdapter(adapter);*/


    }

    public void selectContact(){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},0);
            }

            Intent selectContactintent= new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            selectContactintent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(selectContactintent,PICK_CONTACT_REQUEST);
           // mButtonConfirm.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int columnaNombre = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int columnaNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                   nombre = cursor.getString(columnaNombre);
                   number = cursor.getString(columnaNumber);
                   number = number.replaceAll("\\s+","");

                    if(number.startsWith("+521")){
                        //Toast.makeText(getApplicationContext(), "simon", Toast.LENGTH_SHORT).show();
                        number = number.substring(0,3) + number.substring(4);
                    }
                    else{
                        if(!number.startsWith("+52")){
                            number = "+52" + number;
                        }
                    }

                    if(number.length()>13){
                        //number = number.substring(0, 13);

                        Intent intent = new Intent(AddTrustedFriendsActivity.this, WrongNumberFormatAlert.class);
                        startActivity(intent);
                    }
                    else{
                        mImageViewIcon.setVisibility(View.INVISIBLE);
                        mTextViewLabel.setVisibility(View.INVISIBLE);
                        mButtonAddFriend.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        mNombre.setEnabled(true);
                        mButtonConfirm.setVisibility(View.VISIBLE);

                        mImageUserIcon.setVisibility(View.VISIBLE);
                        mNombre.setVisibility(View.VISIBLE);
                        mTel.setVisibility(View.VISIBLE);

                        mNombre.setText(nombre);
                        mTel.setText(number);
                    }
                }
            }
        }
    }

    public void saveContact(String nombreBD, String telefonoBD){
        String[] field = new String[3];
        field[0] = "nombre";
        field[1] = "telefono";
        field[2] = "email";



        //Creating array for data
        String[] data = new String[3];
        data[0] = nombreBD;
        data[1] = telefonoBD;
        data [2] = email;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/saveContact.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    checkContacts();
                    Toast.makeText(getApplicationContext(), "Se ha añadido el contacto de confianza", Toast.LENGTH_SHORT).show();
                    showList = true;
                    mImageUserIcon.setVisibility(View.INVISIBLE);
                    mNombre.setVisibility(View.INVISIBLE);
                    mTel.setVisibility(View.INVISIBLE);
                    mButtonConfirm.setVisibility(View.INVISIBLE);
                    changeView();

                } else {
                    checkContacts();
                    mImageUserIcon.setVisibility(View.INVISIBLE);
                    mNombre.setVisibility(View.INVISIBLE);
                    mTel.setVisibility(View.INVISIBLE);
                    mButtonConfirm.setVisibility(View.INVISIBLE);
                    changeView();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void LoadView(){
        Glide.with(this).load("https://seguridadmujer.com/web/icon.png").into(mImageViewIcon);
    }

    public void getCredentialData(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
        //email = "paulinitax3@gmail.com";
    }

    //onBackPressed:
    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(AddTrustedFriendsActivity.this);
        startActivity(intent);
        finish();
    }

}