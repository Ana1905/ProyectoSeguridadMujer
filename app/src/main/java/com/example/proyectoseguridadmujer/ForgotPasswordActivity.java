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
                    verifyEmailExistance(email);
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

        String encode_email="";

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
                encode_email= cifrar(email);
                //message.setContent("Hola, por favor inresa al enlace para verificar tu correo:", "text/html");
                message.setContent("Da click en el siguiente link para reestablecer tu contraseña: http://seguridadmujer.com/app_movil/LoginRegister/recoverPassword.php?email=" + encode_email, "text/html");
                Transport.send(message);
                Toast.makeText(getApplicationContext(), "Hemos enviado el link de reestablecimiento a su correo electrónico", Toast.LENGTH_LONG).show();

            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyEmailExistance(String email){
        String[] field = new String[1];
        field[0] = "email";

        //Creating array for data
        String[] data = new String[1];
        data[0] = email;

        PutData putData = new PutData("http://seguridadmujer.com/app_movil/LoginRegister/emailExistanceValidation.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();


                if (result.equals("Email exists")) {
                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    sendRecoveryEmail(email);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();


                } else {

                        if (result.equals("Email doesnt exist")) {
                            Toast.makeText(getApplicationContext(), "No tenemos una cuenta registrada con este correo", Toast.LENGTH_SHORT).show();
                        }


                }


            }
        }
            return true;
    }

    //ENCRIPTAR
    public static String cifrar(String cadenaOriginal) {
        return rotar(cadenaOriginal, 5);
    }

    public static String rotar(String cadenaOriginal, int rotaciones) {
        // En ASCII, la a es 97, b 98, A 65, B 66, etcétera
        final int LONGITUD_ALFABETO = 26, INICIO_MINUSCULAS = 97, INICIO_MAYUSCULAS = 65;
        String cadenaRotada = ""; // La cadena nueva, la que estará rotada
        for (int x = 0; x < cadenaOriginal.length(); x++) {
            char caracterActual = cadenaOriginal.charAt(x);
            // Si no es una letra del alfabeto entonces ponemos el char tal y como está
            // y pasamos a la siguiente iteración
            if (!Character.isLetter(caracterActual)) {
                cadenaRotada += caracterActual;
                continue;
            }

            int codigoAsciiDeCaracterActual = (int) caracterActual;
            boolean esMayuscula = Character.isUpperCase(caracterActual);

            // La posición (1 a 26) que ocupará la letra después de ser rotada
            // El % LONGITUD_ALFABETO se utiliza por si se pasa de 26. Por ejemplo,
            // la "z", al ser rotada una vez da el valor de 27, pero en realidad debería
            // regresar a la letra "a", y con mod hacemos eso ya que 27 % 26 == 1,
            // 28 % 26 == 2, etcétera ;)
            int nuevaPosicionEnAlfabeto = ((codigoAsciiDeCaracterActual
                    - (esMayuscula ? INICIO_MAYUSCULAS : INICIO_MINUSCULAS)) + rotaciones) % LONGITUD_ALFABETO;
            // Arreglar rotaciones negativas
            if (nuevaPosicionEnAlfabeto < 0)
                nuevaPosicionEnAlfabeto += LONGITUD_ALFABETO;
            int nuevaPosicionAscii = (esMayuscula ? INICIO_MAYUSCULAS : INICIO_MINUSCULAS) + nuevaPosicionEnAlfabeto;
            // Convertir el código ASCII numérico a su representación como símbolo o letra y
            // concatenar
            cadenaRotada += Character.toString((char) nuevaPosicionAscii);
        }
        return cadenaRotada;
    }


}
