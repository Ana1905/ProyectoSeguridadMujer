package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import httpurlconnection.PutData;

public class EditContactActivity extends AppCompatActivity {

    EditText mEditTextNombre, mEditTextNumero;
    Button mBotonEliminar, mBotonActualizar;

    String mNombre, mTelefono;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        //wiring up
        mEditTextNombre= findViewById(R.id.nombre);
        mEditTextNumero = findViewById(R.id.telefono);
        mBotonEliminar = findViewById(R.id.boton_eliminar_contacto);
        mBotonActualizar = findViewById(R.id.boton_editar_contacto);

        //Se obtiene el email de la usuaria:
        getCredentialData();

        //Se obtiene la informacion del contacto enviado desde AddTrustedFriendsActivity:
        getBundle();

        //Se ponen los datos en los EditText:
        mEditTextNombre.setText(mNombre);
        mEditTextNumero.setText(mTelefono);

        //onClick del boton para eliminar el contacto:
        mBotonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });

        //onClick del boton para acutalizar el contacto:
        mBotonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });
    }

    //Metodo para hacer el delete:
    void deleteContact(){

        String[] field = new String[2];
        field[0] = "telefono";
        field[1] = "email";

        //Creating array for data
        String[] data = new String[3];
        data[0] = mTelefono;
        data[1] = email;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/deleteContact.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    Toast.makeText(getApplicationContext(), "Se ha eliminado el contacto de confianza", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AddTrustedFriendsActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Metodo para hacer el update del contacto:
    void updateContact(){
        String[] field = new String[4];
        field[0] = "email";
        field[1] = "nombre";
        field[2] = "newtelefono";
        field[3] = "telefono";

        //Creating array for data
        String[] data = new String[4];
        data[0] = email;
        data[1] = mEditTextNombre.getText().toString();
        data[2] = mEditTextNumero.getText().toString();
        data[3] = mTelefono;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/updateContact.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    Toast.makeText(getApplicationContext(), "Se ha actualizado el contacto de confianza", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AddTrustedFriendsActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Metodo para obtener la informacion del bundle enviado desde AddTrustedFriendsActivity.
    void getBundle(){
        mNombre = getIntent().getExtras().getString("Nombre");
        mTelefono = getIntent().getExtras().getString("Numero");
    }

    //Metodo para obtener el correo de la usuaria:
    public void getCredentialData(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }
}