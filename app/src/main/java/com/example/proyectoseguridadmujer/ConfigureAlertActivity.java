package com.example.proyectoseguridadmujer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import httpurlconnection.PutData;

import static android.view.View.GONE;

public class ConfigureAlertActivity extends AppCompatActivity {
    ImageView mImageViewAdd, mImageViewIconAlert, mImageViewImageMessage;
    Button mButtonSave;
    EditText mEditTextMessage;
    Spinner mSpinnerOptionsAlert, mSpinnerContact;
    TextView mTextViewChooseOption, mTextViewWriteMessage, mTextViewAddImage, mTextViewChooseContact;
    CheckBox mCheckBoxCompartirUbicacion;

    int optionSelected = 0;
    int contactSelected = 0;
    String Mensaje = "";
    String email = "";
    String stringImage = "";
    String imageType = "";
    String idContact = "";
    boolean mostrandoImageView = false;

    ArrayList<Contact> mListaContactos = new ArrayList<Contact>();
    RequestQueue requestQueue;

    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_alert);

        //Wiring Up:
        mImageViewAdd = findViewById(R.id.ButtonAddImageAlert);
        mImageViewIconAlert = findViewById(R.id.IconTypeAlert);
        mButtonSave = findViewById(R.id.saveAlertButton);
        mEditTextMessage = findViewById(R.id.edit_text_mensaje);
        mSpinnerOptionsAlert = findViewById(R.id.SpinnerAlert);
        mImageViewImageMessage = findViewById(R.id.imageAlert);
        mTextViewChooseOption = findViewById(R.id.TextViewChooseMessageOption);
        mTextViewWriteMessage = findViewById(R.id.TextViewWriteMessage);
        mTextViewAddImage = findViewById(R.id.TextViewAddImageAlert);
        mTextViewChooseContact = findViewById(R.id.TextViewChooseContact);
        mSpinnerContact = findViewById(R.id.SpinnerContact);
        mCheckBoxCompartirUbicacion = findViewById(R.id.checkbox_compartir_ubicacion);

        //Se obtiene el email de la usuaria:
        getCredentialData();

        //onClick del boton para agregar la imagen de la alerta:
        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarImagen();
            }
        });

        //onClick del boton para eliminar la imagen de la alerta:
        mImageViewImageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarImagen();
            }
        });

        //onClick del boton para guardar la informacion de la alerta:
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });

        //Spinner para seleccionar el tipo de alerta:
        ArrayAdapter<CharSequence> adapterOptionsAlert = ArrayAdapter.createFromResource(this,
                R.array.options_alert_message, android.R.layout.simple_spinner_item);
        mSpinnerOptionsAlert.setAdapter(adapterOptionsAlert);
        mSpinnerOptionsAlert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    LoadIcon(position);
                }
                optionSelected = position;
                ChangeView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner para seleccionar contacto:
        obtenerContactos("https://seguridadmujer.com/app_movil/Alert/ObtenerContactosConfianza.php?email=" + email);
        mSpinnerContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                contactSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Location:
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        obtenerAlerta("https://seguridadmujer.com/app_movil/Alert/ObtenerAlerta.php?email=" + email);
    }

    //Metodo que verifica las validaciones previas a guardar la informacion de la alerta en la base de datos:
    public void Validate() {
        switch (optionSelected) {
            case 1:
            case 2:
                if (mEditTextMessage.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Por favor escriba el mensaje a enviar", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (mCheckBoxCompartirUbicacion.isChecked()) {

                        if(ActivityCompat.checkSelfPermission(ConfigureAlertActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(ConfigureAlertActivity.this, new String[]{Manifest.permission.SEND_SMS},1);
                        }
                        /*
                        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(@NonNull @NotNull Location location) {

                                if(location != null){

                                    Toast.makeText(getApplicationContext(), location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "XD", Toast.LENGTH_SHORT).show();
                                }

                                Toast.makeText(getApplicationContext(), location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
                            }
                        });
                        */
                        Mensaje= mEditTextMessage.getText().toString();
                    }
                    else{
                        Mensaje= mEditTextMessage.getText().toString();
                    }
                    saveAlert();
                    Intent intent = new Intent(getApplicationContext(), AddTrustedFriendsActivity.class);
                    startActivity(intent);
                }
                break;
            case 3:
                if(contactSelected == 0){
                    Toast.makeText(this,"Por favor seleccione el contacto a llamar",Toast.LENGTH_SHORT).show();
                }
                else{
                    Mensaje = "";
                    idContact = String.valueOf(mListaContactos.get(contactSelected).getID_Contacto());
                    //Toast.makeText(this,idContact,Toast.LENGTH_SHORT).show();
                    saveAlert();
                    Intent intent = new Intent(getApplicationContext(), AddTrustedFriendsActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + optionSelected);
        }
    }

    //Metodo para guardar la informacion de la alerta en la base de datos:
    public void saveAlert() {
        String[] field = new String[6];
        field[0] = "email";
        field[1] = "opcion";
        field[2] = "mensaje";
        field[3] = "imagen";
        field[4] = "tipoimagen";
        field[5] = "contacto";

        //Creating array for data
        String[] data = new String[6];
        data[0] = email;
        data[1] = String.valueOf(optionSelected);
        data[2] = Mensaje;
        data[3] = stringImage;
        data[4] = imageType;
        data[5] = idContact;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/saveAlert.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    Toast.makeText(getApplicationContext(), "Se ha configurado la alerta", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Metodo para seleccionar una imagen de la galeria:
    public void agregarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"), 1);
    }

    //OnActivityResult para procesar la imagen seleccionada:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            //Guarda la foto en un URI y la coonvierte a Bitmap
            Bitmap bitmap = null;
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                //Se obtiene la extension de la imagen:
                ByteArrayOutputStream array = new ByteArrayOutputStream();
                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                imageType = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
                //Toast.makeText(getApplicationContext(), imageType, Toast.LENGTH_SHORT).show();

                if(imageType.equals("jpg")){
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
                    mImageViewImageMessage.setImageURI(uri);
                    mImageViewImageMessage.setVisibility(View.VISIBLE);
                }
                else if(imageType.equals("png")){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, array);
                    mImageViewImageMessage.setImageURI(uri);
                    mImageViewImageMessage.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Por favor introduce un archivo valido", Toast.LENGTH_SHORT).show();
                }

                //Se convierte a string la imagen:
                byte [] imageByte = array.toByteArray();
                stringImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Metodo para eliminar la imagen previamente seleccionada:
    public void  eliminarImagen(){
        //mImageViewImageMessage.setImageResource(android.R.color.transparent);
        mImageViewImageMessage.setImageDrawable(null);
        mImageViewImageMessage.setVisibility(GONE);
        stringImage = "";
        imageType = "";
    }

    private void obtenerContactos(String URL){

        List<String> labels = new ArrayList<String>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        if(i == 0){
                            Contact contact = new Contact();
                            contact.setID_Contacto(0);
                            contact.setNombre("");
                            contact.setNumero("");

                            mListaContactos.add(contact);
                            labels.add("");
                        }
                        Contact contact = new Contact();
                        contact.setID_Contacto(jsonObject.getInt("ID_Contacto"));
                        contact.setNombre(jsonObject.getString("NombreContacto"));
                        contact.setNumero(jsonObject.getString("Telefono"));

                        mListaContactos.add(contact);
                        labels.add(contact.getNombre() + " " + contact.getNumero());

                        //Toast.makeText(getApplicationContext(), mListaContactos.get(i).getNombre() + " " + mListaContactos.get(i).getNumero(), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                cargarAdapterContactos(labels);

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

    private void cargarAdapterContactos(List<String> labels){
        ArrayAdapter<String> adapterContactos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        mSpinnerContact.setAdapter(adapterContactos);
    }

    private void obtenerAlerta(String URL){
        Alert mAlert = new Alert();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        mAlert.setID_Alerta(jsonObject.getInt("ID_Alerta"));
                        mAlert.setTipoAlerta(jsonObject.getInt("TipoAlerta"));

                        if(mAlert.getTipoAlerta() != 3){
                            mAlert.setMensaje(jsonObject.getString("Mensaje"));

                            if(!jsonObject.getString("RutaImagen").isEmpty() && !jsonObject.getString("TipoImagen").isEmpty()){
                                mAlert.setRutaImagen(jsonObject.getString("RutaImagen"));
                                mAlert.setTipoImagen(jsonObject.getString("TipoImagen"));
                            }
                            else{
                                mAlert.setRutaImagen("");
                                mAlert.setTipoImagen("");
                            }
                        }
                        else{
                            mAlert.setID_Contacto(jsonObject.getInt("ID_Contacto"));
                        }

                        //Toast.makeText(getApplicationContext(), mListaContactos.get(i).getNombre() + " " + mListaContactos.get(i).getNumero(), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                if(jsonArray.length() != 0){
                    mostrarAlertaObtenida(mAlert);
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

    private void mostrarAlertaObtenida(Alert mAlert){

        switch (mAlert.getTipoAlerta()){

            case 1:
                LoadIcon(1);
                ChangeView(1);

                mSpinnerOptionsAlert.setSelection(1);
                mEditTextMessage.setText(mAlert.getMensaje());
                if(!mAlert.getRutaImagen().isEmpty() && !mAlert.getTipoImagen().isEmpty()){
                    Glide.with(this).load(mAlert.getRutaImagen()).into(mImageViewImageMessage);
                    mostrandoImageView = true;
                    mImageViewImageMessage.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                LoadIcon(2);
                ChangeView(2);

                mSpinnerOptionsAlert.setSelection(2);
                mEditTextMessage.setText(mAlert.getMensaje());
                break;
            case 3:
                LoadIcon(3);
                ChangeView(3);

                mSpinnerOptionsAlert.setSelection(3);

               // mSpinnerContact.setSelection(3);

                for(int i=0; i<mListaContactos.size(); i++){

                    if(mAlert.getID_Contacto() == mListaContactos.get(i).getID_Contacto()){
                        mSpinnerContact.setSelection(i);
                        Toast.makeText(getApplicationContext(), "Se ha cargado el contacto: " + mListaContactos.get(i).Nombre, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //Metodo para mostrar y esconder los elementos de la vista segun la opcion seleccionada:
    public void ChangeView(int option){
        switch (option){
            case 0:
                mSpinnerOptionsAlert.setVisibility(View.VISIBLE);
                mImageViewIconAlert.setVisibility(View.INVISIBLE);
                mEditTextMessage.setVisibility(View.INVISIBLE);
                mImageViewImageMessage.setVisibility(View.INVISIBLE);
                mImageViewAdd.setVisibility(View.INVISIBLE);
                mButtonSave.setVisibility(View.INVISIBLE);
                mTextViewChooseOption.setVisibility(View.VISIBLE);
                mTextViewWriteMessage.setVisibility(View.INVISIBLE);
                mTextViewAddImage.setVisibility(View.INVISIBLE);
                mTextViewChooseContact.setVisibility(View.INVISIBLE);
                mSpinnerContact.setVisibility(View.INVISIBLE);
                mCheckBoxCompartirUbicacion.setVisibility(View.INVISIBLE);
                stringImage = "";
                imageType = "";
                break;
            case 1:
                mSpinnerOptionsAlert.setVisibility(View.VISIBLE);
                mImageViewIconAlert.setVisibility(View.VISIBLE);
                mEditTextMessage.setVisibility(View.VISIBLE);
                if(!mostrandoImageView){
                    mImageViewImageMessage.setVisibility(GONE);
                }
                mImageViewAdd.setVisibility(View.VISIBLE);
                mButtonSave.setVisibility(View.VISIBLE);
                mTextViewChooseOption.setVisibility(View.VISIBLE);
                mTextViewWriteMessage.setVisibility(View.VISIBLE);
                mTextViewAddImage.setVisibility(View.VISIBLE);
                mTextViewChooseContact.setVisibility(View.GONE);
                mSpinnerContact.setVisibility(View.GONE);
                mCheckBoxCompartirUbicacion.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Para eliminar una imágen añadida da click sobre ella",Toast.LENGTH_SHORT).show();
                stringImage = "";
                imageType = "";
                break;
            case 2:
                mSpinnerOptionsAlert.setVisibility(View.VISIBLE);
                mImageViewIconAlert.setVisibility(View.VISIBLE);
                mEditTextMessage.setVisibility(View.VISIBLE);
                mImageViewImageMessage.setVisibility(GONE);
                mImageViewAdd.setVisibility(GONE);
                mButtonSave.setVisibility(View.VISIBLE);
                mTextViewChooseOption.setVisibility(View.VISIBLE);
                mTextViewWriteMessage.setVisibility(View.VISIBLE);
                mTextViewAddImage.setVisibility(GONE);
                mTextViewChooseContact.setVisibility(View.GONE);
                mSpinnerContact.setVisibility(View.GONE);
                mCheckBoxCompartirUbicacion.setVisibility(View.VISIBLE);
                stringImage = "";
                imageType = "";
                break;
            case 3:
                mSpinnerOptionsAlert.setVisibility(View.VISIBLE);
                mImageViewIconAlert.setVisibility(View.VISIBLE);
                mEditTextMessage.setVisibility(GONE);
                mImageViewImageMessage.setVisibility(GONE);
                mImageViewAdd.setVisibility(GONE);
                mButtonSave.setVisibility(View.VISIBLE);
                mTextViewChooseOption.setVisibility(View.VISIBLE);
                mTextViewWriteMessage.setVisibility(GONE);
                mTextViewAddImage.setVisibility(GONE);
                mTextViewChooseContact.setVisibility(View.VISIBLE);
                mSpinnerContact.setVisibility(View.VISIBLE);
                mCheckBoxCompartirUbicacion.setVisibility(GONE);
                stringImage = "";
                imageType = "";
                break;
        }
    }

    //Metodo para cargar el logo segun la opcion seleccionada:
    public void LoadIcon(int option){
        switch (option){
            case 1:
                Glide.with(this).load("https://seguridadmujer.com/app_movil/Resources/whatssapIcon.png").into(mImageViewIconAlert);
                break;

            case 2:
                Glide.with(this).load("https://seguridadmujer.com/app_movil/Resources/SMSIcon.png").into(mImageViewIconAlert);
                break;

            case 3:
                Glide.with(this).load("https://seguridadmujer.com/app_movil/Resources/callIcon.png").into(mImageViewIconAlert);
                break;
        }
    }

    //Metodo para obtener el email de la usuaria:
    public void getCredentialData(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
        //email = "paulinitax3@gmail.com";
    }

    //onBackPressed:
    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(ConfigureAlertActivity.this);
        startActivity(intent);
        finish();
    }

}