package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import httpurlconnection.PutData;


public class ForgotPasswordActivity extends AppCompatActivity {
    EditText mEditTextEmailRecovery;
    Button mButtonRecover;

    String correo = "secureapp2021@gmail.com"; //var to save email of a
    String contraseña = "secureappCETI"; //var to save password of account
    Session session; //var to save the email session

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
                String email;
                email = String.valueOf(mEditTextEmailRecovery.getText());
                if (!email.equals("")) {
                    Toast.makeText(getApplicationContext(), "Hemos enviado el link de reestablecimiento a su correo electrónico", Toast.LENGTH_LONG).show();
                    sendRecoveryEmail(email);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Debes ingresar un correo", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    public boolean sendRecoveryEmail(String email){


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");


        try{


            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {

                    return new PasswordAuthentication(correo,contraseña);
                }
            });

            if(session!= null){
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));
                message.setSubject("Recupera tu contraseña");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); //change for var email
                //message.setContent("Hola, por favor inresa al enlace para verificar tu correo:", "text/html");
                message.setContent("Da click en el siguiente link para reestablecer tu contraseña: http://seguridadmujer.com/app_movil/LoginRegister/recoverPassword.html", "text/html");
                Transport.send(message);

            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
