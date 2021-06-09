package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import httpurlconnection.PutData;

public class CreateAccountActivity extends AppCompatActivity {
    EditText mEditTextCreateAccountEmail, mEditTextCreateAccountName, mEditTextCreateAccountPaternalSurname, mEditTextCreateAccountMaternalSurname,  mEditTextCreateAccountDateOfBirth, mEditTextCreateAccountPassword, mEditTextCreateAccountConfirmPassword;
    Button mButtonCreateAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Wiring up
        mEditTextCreateAccountEmail= findViewById(R.id.create_account_email);
        mEditTextCreateAccountName = findViewById(R.id.create_account_name);
        mEditTextCreateAccountPaternalSurname = findViewById(R.id.create_account_paternal_surname);
        mEditTextCreateAccountMaternalSurname = findViewById(R.id.create_account_maternal_surname);
        mEditTextCreateAccountDateOfBirth = findViewById(R.id.create_account_dateOfBirth);
        mEditTextCreateAccountPassword = findViewById(R.id.create_account_password);
        mEditTextCreateAccountConfirmPassword = findViewById(R.id.create_account_confirm_password);
        mButtonCreateAccount = findViewById(R.id.create_account_create);

        //On Click
        mButtonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Variables to catch data
                String email, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, contraseña, confirmPassword;
                email = String.valueOf(mEditTextCreateAccountEmail.getText());
                nombres = String.valueOf(mEditTextCreateAccountName.getText());
                apellido_paterno = String.valueOf(mEditTextCreateAccountPaternalSurname.getText());
                apellido_materno = String.valueOf(mEditTextCreateAccountMaternalSurname.getText());
                fecha_nacimiento = String.valueOf(mEditTextCreateAccountDateOfBirth.getText());
                contraseña = String.valueOf(mEditTextCreateAccountPassword.getText());
                confirmPassword = String.valueOf(mEditTextCreateAccountConfirmPassword.getText());

                if (email.equals("") && nombres.equals("") && apellido_paterno.equals("") && apellido_materno.equals("") && fecha_nacimiento.equals("") && contraseña.equals("")) {
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
                            data[0] = email;
                            data[1] = nombres;
                            data[2] = apellido_paterno;
                            data[3] = apellido_materno;
                            data[4] = fecha_nacimiento;
                            data[5] = contraseña;
                            PutData putData = new PutData("http:// 192.168.1.109/LoginRegister/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });

                }

                else{
                    Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }
          }
        });




    }
}