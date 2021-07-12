package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import httpurlconnection.PutData;

public class LoginActivity extends AppCompatActivity {
    EditText mEditTextSignInEmail, mEditTextSignInPassword;
    Button mButtonSignIn,mButtonCreateAccount;
    TextView mTextViewForgotPassword;
    String email, contraseña;
    public static String STRING_PREFERENCES = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Wiring up
        mEditTextSignInEmail = findViewById(R.id.sign_in_email_input);
        mEditTextSignInPassword = findViewById(R.id.sign_in_password_input);
        mButtonSignIn = findViewById(R.id.sign_in_enter_button);
        mButtonCreateAccount = findViewById(R.id.sign_in_create_account_button);
        mTextViewForgotPassword = findViewById(R.id.sign_in_forgot_password_label);

        cargarSession();

        //OnClicks
        mButtonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mTextViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Variables to catch data

                email = String.valueOf(mEditTextSignInEmail.getText());
                contraseña = String.valueOf(mEditTextSignInPassword.getText());


                if (!email.equals("") && !contraseña.equals("")) {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "email";
                            field[1] = "contraseña";


                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = email;
                            data[1] = contraseña;
                            //Change ip and port of your computer and xampp
                            PutData putData = new PutData("http://seguridadmujer.com/app_movil/LoginRegister/login.php", "POST", field, data);

                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();

                                    if (result.equals("Login Success")) {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        guardarSession();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        if (result.equals("Active email or Password wrong")) {
                                            Toast.makeText(getApplicationContext(), "Esta cuenta esta activa en otro dispositivo. Cierre sesión para poder acceder", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            if (result.equals("Missing email verification email or Password wrong")) {
                                                Toast.makeText(getApplicationContext(), "La cuenta aun no ha sido creada pues no se ha verificado la dirección de correo", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void guardarSession(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("password", contraseña);
        editor.commit();

    }


    public void cargarSession(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
        contraseña = preferences.getString("password", "");

        String[] field = new String[2];
        field[0] = "email";
        field[1] = "contraseña";


        //Creating array for data
        String[] data = new String[2];
        data[0] = email;
        data[1] = contraseña;
        //Change ip and port of your computer and xampp
        PutData putData = new PutData("http://seguridadmujer.com/app_movil/LoginRegister/login.php", "POST", field, data);

        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();

                if (result.equals("Login Success")) {
                   // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    //guardarSession();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {


                }
            }
        }



    }



    }