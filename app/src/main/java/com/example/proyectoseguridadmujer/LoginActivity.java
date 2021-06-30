package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                String email, contraseña;
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
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

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

    public void openTermsDialog() {
        TemporaryBannedAccountDialog blacklistEailDialog = new TemporaryBannedAccountDialog();
        blacklistEailDialog.show(getSupportFragmentManager(), "Temporary banned acount dialog");

    }

    }