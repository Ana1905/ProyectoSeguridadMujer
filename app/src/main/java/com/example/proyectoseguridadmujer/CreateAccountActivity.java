package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import httpurlconnection.PutData;

public class CreateAccountActivity extends AppCompatActivity {
    EditText mEditTextCreateAccountEmail, mEditTextCreateAccountName, mEditTextCreateAccountPaternalSurname, mEditTextCreateAccountMaternalSurname,  mEditTextCreateAccountDateOfBirth, mEditTextCreateAccountPassword, mEditTextCreateAccountConfirmPassword;
    Button mButtonCreateAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        //Start ProgressBar first (Set visibility VISIBLE)
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[6];
                field[0] = "email";
                field[1] = "nombres";
                field[2] = "apellido_paterno";
                field[3] = "apellido_materno";
                field[4] = "fecha_nacimiento";
                field[5] = "contraseña";


                //Creating array for data
                String[] data = new String[6];
                data[0] = "email";
                data[1] = "nombres";
                data[2] = "apellido_paterno";
                data[3] = "apellido_materno";
                data[4] = "fecha_nacimiento";
                data[5] = "contraseña";
                PutData putData = new PutData("https://projects.vishnusivadas.com/AdvancedHttpURLConnection/putDataTest.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        //End ProgressBar (Set visibility to GONE)
                        Log.i("PutData", result);
                    }
                }
                //End Write and Read data with URL
            }
        });

    }
}