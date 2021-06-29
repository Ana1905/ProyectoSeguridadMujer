package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class ForgotPasswordActivity extends AppCompatActivity {
    EditText mEditTextEmailRecovery;
    Button mButtonRecover;

    private Properties emailProperties;
    private Session mailSession;
    private MimeMessage emailMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);




        //Wiring up
        mEditTextEmailRecovery = findViewById(R.id.forgot_password_email);
        mButtonRecover = findViewById(R.id.forgot_password_recovery_button);

        //Onclicks
        mButtonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hemos enviado el link de reestablecimiento a su correo electrónico", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


}
