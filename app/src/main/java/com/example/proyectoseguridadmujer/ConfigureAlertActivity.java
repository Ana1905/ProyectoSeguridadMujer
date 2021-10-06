package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import httpurlconnection.PutData;

import static android.view.View.GONE;

public class ConfigureAlertActivity extends AppCompatActivity {
    ImageView mImageViewAdd, mImageViewIconAlert,mImageViewImageMessage;
    Button mButtonSave;
    EditText mEditTextMessage;
    Spinner mSpinnerOptionsAlert;
    TextView mTextViewChooseOption, mTextViewWriteMessage, mTextViewAddImage;
    int optionSelected=0;
    String Mensaje=" ";
    String email = " ";
    String stringImage=" ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_alert);

        //WIRING UP
        mImageViewAdd = findViewById(R.id.ButtonAddImageAlert);
        mImageViewIconAlert = findViewById(R.id.IconTypeAlert);
        mButtonSave = findViewById(R.id.saveAlertButton);
        mEditTextMessage = findViewById(R.id.edit_text_mensaje);
        mSpinnerOptionsAlert = findViewById(R.id.SpinnerAlert);
        mImageViewImageMessage = findViewById(R.id.imageAlert);
        mTextViewChooseOption = findViewById(R.id.TextViewChooseMessageOption);
        mTextViewWriteMessage = findViewById(R.id.TextViewWriteMessage);
        mTextViewAddImage = findViewById(R.id.TextViewAddImageAlert);

        getCredentialData();


        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarImagen();
            }
        });

        mImageViewImageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarImagen();
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });

        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(this,
                R.array.options_alert_message, android.R.layout.simple_spinner_item);

        mSpinnerOptionsAlert.setAdapter(adapterCategoria);
        mSpinnerOptionsAlert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){

                    LoadIcon(position);

                }
                optionSelected=position;
                ChangeView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public  void  Validate(){
        switch (optionSelected){
            case 1:

            case 2:


                if(mEditTextMessage.getText().toString().isEmpty()){
                    Mensaje= mEditTextMessage.getText().toString();
                    Toast.makeText(this,"Por favor escriba el mensaje a enviar",Toast.LENGTH_SHORT).show();
                }
                else{
                    saveAlert();
                }

                break;

            case 3:
               //pendiente
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + optionSelected);
        }
    }

    public void saveAlert() {
        String[] field = new String[4];
        field[0] = "email";
        field[1] = "opcion";
        field[2] = "imagen";
        field[3] = "mensaje";

        //Creating array for data
        String[] data = new String[4];
        data[0] =email;
        data[1] =String.valueOf(optionSelected);
        data[2] =stringImage;
        data[3] =Mensaje;


        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/saveAlert.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                    Toast.makeText(getApplicationContext(), "Se ha configurado la alerta", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void getCredentialData(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public void  eliminarImagen(){
        //mImageViewImageMessage.setImageResource(android.R.color.transparent);
        mImageViewImageMessage.setImageDrawable(null);
        mImageViewImageMessage.setVisibility(GONE);

    }

    public void agregarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            //Guarda la foto en un URI y la coonvierte a Bitmap
            Bitmap bitmap = null;
            Uri path = data.getData();
            mImageViewImageMessage.setVisibility(View.VISIBLE);

            mImageViewImageMessage.setImageURI(path);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Decodifica la foto
            /*
            convertBitmapToFile(bitmap);

            bitmap = BitmapFactory.decodeFile(mFileFoto.toString()); //Decodifica la foto
            ajustar_foto(bitmap);

             */

        }

    }


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

                break;
            case 1:
                mSpinnerOptionsAlert.setVisibility(View.VISIBLE);
                mImageViewIconAlert.setVisibility(View.VISIBLE);
                mEditTextMessage.setVisibility(View.VISIBLE);
                mImageViewImageMessage.setVisibility(GONE);
                mImageViewAdd.setVisibility(View.VISIBLE);
                mButtonSave.setVisibility(View.VISIBLE);
                mTextViewChooseOption.setVisibility(View.VISIBLE);
                mTextViewWriteMessage.setVisibility(View.VISIBLE);
                mTextViewAddImage.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Para eliminar una imágen añadida da click sobre ella",Toast.LENGTH_SHORT).show();
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
                break;
        }
    }


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
}