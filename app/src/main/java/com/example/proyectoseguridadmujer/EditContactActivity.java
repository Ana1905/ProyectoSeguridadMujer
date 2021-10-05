package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class EditContactActivity extends AppCompatActivity {
    EditText mEditTextNombre, mEditTextNumero;
    String mNombre, mTelefono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        //wiring up
        mEditTextNombre= findViewById(R.id.nombre);
        mEditTextNumero = findViewById(R.id.telefono);


        getBundle();

        mEditTextNombre.setText(mNombre);
        mEditTextNumero.setText(mTelefono);
    }

    void getBundle(){
        mNombre = getIntent().getExtras().getString("Nombre");
        mTelefono = getIntent().getExtras().getString("Numero");
    }
}