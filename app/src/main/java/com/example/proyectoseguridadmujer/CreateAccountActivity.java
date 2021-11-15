package com.example.proyectoseguridadmujer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

import org.jetbrains.annotations.NotNull;
import java.util.Calendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import httpurlconnection.PutData;
import static android.graphics.Color.GREEN;
public class CreateAccountActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    private DatePickerDialog datePickerDialog;

    EditText mEditTextCreateAccountEmail, mEditTextCreateAccountName, mEditTextCreateAccountPaternalSurname, mEditTextCreateAccountMaternalSurname, mEditTextCreateAccountPassword, mEditTextCreateAccountConfirmPassword;
    Button mButtonCreateAccount, mButtonDateOfBirth, mButtonTermsAndConditions;
    CheckBox mCheckBoxCaptcha, mCheckBoxTermsAndConditions;
    TextView mTextViewGotoLogin, mTextViewDateOfBirth, mTextViewLoading;
    ImageView mImageView, mImageViewLoading;
    GoogleApiClient googleApiClient;

    String mPhoneNumber;
    String correo = "secureapp2021@gmail.com"; //var to save email of a
    String contraseña = "secureappCETI"; //var to save password of account
    Session session; //var to save the email session
    int birthYear; //var to save just the year of users birthdate
    int threadCounter = 0;

    //Put sitekey as a string CAPTCHA
    String SiteKey = "6LesLFEbAAAAAEmJtNkxvnLUJhQKuN2v4SzRbE8f";
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initDatePicker();

        //PHONE
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mPhoneNumber = tMgr.getLine1Number();

        if(mPhoneNumber == null){
            mPhoneNumber = "0123456789";
        }

        //Toast.makeText(getApplicationContext(), mPhoneNumber, Toast.LENGTH_SHORT).show();


        //Wiring up
        mEditTextCreateAccountEmail = findViewById(R.id.create_account_email);
        mEditTextCreateAccountName = findViewById(R.id.create_account_name);
        mEditTextCreateAccountPaternalSurname = findViewById(R.id.create_account_paternal_surname);
        mEditTextCreateAccountMaternalSurname = findViewById(R.id.create_account_maternal_surname);
        mButtonDateOfBirth = findViewById(R.id.create_account_dateOfBirth);
        mEditTextCreateAccountPassword = findViewById(R.id.create_account_password);
        mEditTextCreateAccountConfirmPassword = findViewById(R.id.create_account_confirm_password);
        mButtonCreateAccount = findViewById(R.id.create_account_create);
        mCheckBoxCaptcha = findViewById(R.id.create_account_captcha);
        mCheckBoxTermsAndConditions = findViewById(R.id.create_account_terms);
        mButtonTermsAndConditions = findViewById(R.id.button_create_account_terms);
        mTextViewGotoLogin = findViewById(R.id.TextViewGotoLogin);
        mImageView = findViewById(R.id.imageView);
        mTextViewDateOfBirth = findViewById(R.id.fechaNacimientoLabel);
        mTextViewLoading = findViewById(R.id.waitingLabel);
        mImageViewLoading = findViewById(R.id.imageViewLoading);
        mImageViewLoading.setVisibility(View.INVISIBLE);                //Se esconde el Image View de espera de confirmacion de cuenta.

        //---GOTOLOGIN TEXTVIEW ONCLICK
        mTextViewGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //---CAPTCHA
        //CreateGoogle Api client
        googleApiClient = new GoogleApiClient.Builder(this).addApi(SafetyNet.API).addConnectionCallbacks(CreateAccountActivity.this).build();
        googleApiClient.connect();
        mCheckBoxCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set if condition when checbox checked
                if (mCheckBoxCaptcha.isChecked()) {
                    //mCheckBoxCaptcha.setEnabled(false); //Once it is checked unchecking is not enable
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, SiteKey).setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                        @Override
                        public void onResult(@NonNull @NotNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                            Status status = recaptchaTokenResult.getStatus();
                            if ((status != null) && status.isSuccess()) {
                                //Display Success Message
                                Toast.makeText(getApplicationContext(), "Verificado con exito", Toast.LENGTH_SHORT).show();
                                //Change checkbox text color
                                mCheckBoxCaptcha.setTextColor(GREEN);
                            }
                        }
                    });
                } else {
                    mCheckBoxCaptcha.setChecked(false);
                    mCheckBoxCaptcha.setTextColor(Color.WHITE);
                }
            }
        });


        //----TERMS AND CONTIDIONS
        mButtonTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTermsDialog();
                mButtonTermsAndConditions.setVisibility(View.INVISIBLE);
                mCheckBoxTermsAndConditions.setVisibility(View.VISIBLE);

            }
        });

        mCheckBoxTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCheckBoxTermsAndConditions.isChecked()) {
                    mCheckBoxTermsAndConditions.setEnabled(false); //Once it is checked unchecking is not enable
                }

            }

        });

        mButtonDateOfBirth.setText(getTodaysDate());

        //On Click
        mButtonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //sendVerificationEmail("paulinitax3@gmail.com");
                //Variables to catch data
                String email, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, contraseña, confirmPassword;

                email = String.valueOf(mEditTextCreateAccountEmail.getText()).trim();
                nombres = String.valueOf(mEditTextCreateAccountName.getText()).trim();
                apellido_paterno = String.valueOf(mEditTextCreateAccountPaternalSurname.getText()).trim();
                apellido_materno = String.valueOf(mEditTextCreateAccountMaternalSurname.getText()).trim();
                fecha_nacimiento = String.valueOf(mButtonDateOfBirth.getText()).trim();
                contraseña = String.valueOf(mEditTextCreateAccountPassword.getText()).trim();
                confirmPassword = String.valueOf(mEditTextCreateAccountConfirmPassword.getText()).trim();
                int age = ((getTodaysDateYear()) - birthYear);

                //validations
                if (!email.equals("") && !nombres.equals("") && !apellido_paterno.equals("") && !apellido_materno.equals("") && !fecha_nacimiento.equals("") && !contraseña.equals("") && !confirmPassword.equals("")) {
                    if (age > 12) {
                        if (emailFormatValidation(email)) {
                            if (emailDomainValidation(email).equals("@gmail.com") || emailDomainValidation(email).equals("@outlook.com")) {
                                if (contraseña.equals(confirmPassword)) {
                                    if (mCheckBoxCaptcha.getCurrentTextColor() == GREEN) {
                                        if (mCheckBoxTermsAndConditions.isChecked()) {
                                            //Start ProgressBar first (Set visibility VISIBLE)
                                            Handler handler = new Handler();
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //Starting Write and Read data with URL
                                                    //Creating array for parameters
                                                    String[] field = new String[7];
                                                    field[0] = "email";
                                                    field[1] = "nombres";
                                                    field[2] = "apellido_paterno";
                                                    field[3] = "apellido_materno";
                                                    field[4] = "fecha_nacimiento";
                                                    field[5] = "contraseña";
                                                    field[6] = "telefono";


                                                    //Creating array for data
                                                    String[] data = new String[7];
                                                    data[0] = email;
                                                    data[1] = nombres;
                                                    data[2] = apellido_paterno;
                                                    data[3] = apellido_materno;
                                                    data[4] = fecha_nacimiento;
                                                    data[5] = contraseña;
                                                    data[6] = mPhoneNumber;



                                        /*
                                        //Change ip and port of your computer and xampp
                                        PutData putData = new PutData("http://192.168.56.1:80/LoginRegister/email_validation.php", "POST", field2, data2);
                                        if (putData.startPut()) {
                                            if (putData.onComplete()) {
                                                String validation = putData.getResult();
                                                    if(validation.equals("valid email")){
                                                    Toast.makeText(getApplicationContext(),"bien",Toast.LENGTH_SHORT).show();
                                                    //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    //startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),validation,Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        }

            */
                                                    PutData putData = new PutData("https://seguridadmujer.com/app_movil/LoginRegister/signup.php", "POST", field, data);
                                                    if (putData.startPut()) {
                                                        if (putData.onComplete()) {
                                                            String result = putData.getResult();
                                                            if (result.equals("Sign Up Success")) {
                                                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                                                sendVerificationEmail(email);
                                                                Toast.makeText(getApplicationContext(), "Se ha enviado un link de verificacion a su correo", Toast.LENGTH_SHORT).show();
                                                                mostrarLoadingGif();
                                                                comprobarCorreoVerificado();
                                                                cerrarActivity(email);
                                                            } else {

                                                                if (result.equals("Email in blacklist Sign up Failed")) {
                                                                    OpenBannedEmailDialog();
                                                                } else {

                                                                    if (result.equals("Invalid email Sign up Failed")) {
                                                                        Toast.makeText(getApplicationContext(), "Este correo ya fue utilizado en otra cuemta", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            }


                                                        }
                                                    }
                                                    //End Write and Read data with URL
                                                }
                                            });

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Debemos verificar que no sea un robot", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Los dominios de correo admitidos son gmail y outlook ", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "El email ingresado no corresponde a un formato válido", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No se cumplen los requisitos mínimos de edad", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }


            }

        });

    }

    //DATE PICKER
    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                mButtonDateOfBirth.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        birthYear = year;
        return year + "/" + month + "/" + day; //date format changes here

    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    //EXTRA
    private int getTodaysDateYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    //CAPTCHA
    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //TERMS AND CONDITIONS DIALOG
    public void openTermsDialog() {
        TermsDialog blacklistEailDialog = new TermsDialog();
        blacklistEailDialog.show(getSupportFragmentManager(), "terms and conditions dialog");

    }

    //BANNED EMAIL DIALOG
    public void OpenBannedEmailDialog() {
        BannedEmailDialog blacklistEailDialog = new BannedEmailDialog();
        blacklistEailDialog.show(getSupportFragmentManager(), "banned email dialog");

    }

    //EMAIL VALIDATION
    public boolean emailFormatValidation(String email) {
        // Patrón para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        // El email a validar
        Matcher mather = pattern.matcher(email);

        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }

    public String emailDomainValidation(String email) {
        String domain = email.substring(email.indexOf("@"));
        return domain;

    }

    public boolean sendVerificationEmail(String email) {
        //Si no envia nada desactiva tu antivirus

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        String decode_text="";
        String encode_email="";
        try {

            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {

                    return new PasswordAuthentication(correo, contraseña);
                }
            });

            if (session != null) {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));
                message.setSubject("Verifica tu correo");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); //change for var email
                //message.setContent("Hola, por favor inresa al enlace para verificar tu correo:", "text/html");

                encode_email= cifrar(email);
                message.setContent("Da click en el siguiente link para verificar tu correo: http://seguridadmujer.com/app_movil/LoginRegister/emailVerification.php?email=" + encode_email, "text/html");
                //message.setText("Hola");
                Transport.send(message);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Este metodo esconde el formulario de creacion de cuenta y muestra la "pantalla de espera" de verificacion de cuenta:
    public void mostrarLoadingGif(){
        //Se esconden todos los campos para la creacion de la cuenta:
        mEditTextCreateAccountEmail.setVisibility(View.INVISIBLE);
        mEditTextCreateAccountName.setVisibility(View.INVISIBLE);
        mEditTextCreateAccountPaternalSurname.setVisibility(View.INVISIBLE);
        mEditTextCreateAccountMaternalSurname.setVisibility(View.INVISIBLE);
        mButtonDateOfBirth.setVisibility(View.INVISIBLE);
        mEditTextCreateAccountPassword.setVisibility(View.INVISIBLE);
        mEditTextCreateAccountConfirmPassword.setVisibility(View.INVISIBLE);
        mButtonCreateAccount.setVisibility(View.INVISIBLE);
        mCheckBoxCaptcha.setVisibility(View.INVISIBLE);
        mCheckBoxTermsAndConditions.setVisibility(View.INVISIBLE);
        mButtonTermsAndConditions.setVisibility(View.INVISIBLE);
        mTextViewGotoLogin.setVisibility(View.INVISIBLE);
        mTextViewDateOfBirth.setVisibility(View.INVISIBLE);
        mImageView.setVisibility(View.INVISIBLE);

        //Se muestra el gif de carga:
        mImageViewLoading.setVisibility(View.VISIBLE);
        mTextViewLoading.setVisibility(View.VISIBLE);
        Glide.with(this).load("https://seguridadmujer.com/app_movil/Resources/waiting.gif").into(mImageViewLoading);
    }

    //Este metodo crea un hilo que valida cada 10 segundos si se ha verificado la cuenta y, en dado caso, hacer login automaticamente:
    public void comprobarCorreoVerificado(){
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                ///////// Se ejecuta la query para comprobar si ya se ha verificado la cuenta: /////////
                String email = String.valueOf(mEditTextCreateAccountEmail.getText()).trim();            //Se obtiene el email introducido.
                String contraseña = String.valueOf(mEditTextCreateAccountPassword.getText()).trim();    //Se obtiene la contraseña introducida.

                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "email";

                //Creating array for data
                String[] data = new String[1];
                data[0] = email;

                PutData putData = new PutData("https://seguridadmujer.com/app_movil/LoginRegister/verifyEmailVerification.php", "POST", field, data);
                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();
                        //Si se obtiene que el correo ha sido verificado, se hace login:
                        if(result.equals("Correo verificado")) {
                            Toast.makeText(getApplicationContext(), "El correo ha sido verificado", Toast.LENGTH_LONG).show();
                            //Hace el login:
                            if (!email.equals("") && !contraseña.equals("")) {
                                //Start ProgressBar first (Set visibility VISIBLE)
                                Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Starting Write and Read data with URL
                                        //Creating array for parameters
                                        String[] field = new String[3];
                                        field[0] = "email";
                                        field[1] = "contraseña";
                                        field[2] = "telefono";

                                        //Creating array for data
                                        String[] data = new String[3];
                                        data[0] = email;
                                        data[1] = contraseña;
                                        data[2] = mPhoneNumber;

                                        PutData putData = new PutData("https://seguridadmujer.com/app_movil/LoginRegister/login.php", "POST", field, data);

                                        if (putData.startPut()) {
                                            if (putData.onComplete()) {
                                                String result = putData.getResult();
                                                if (result.equals("Login Success")) {
                                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                                    guardarSesion(email, contraseña);
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    if(result.equals("Missing email verification email or Password wrong")){
                                                        Toast.makeText(getApplicationContext(), "La cuenta aún no ha sido creada pues no se ha verificado la dirección de correo", Toast.LENGTH_LONG).show();
                                                    }
                                                    else{
                                                        if(result.equals("phones not match email or Password wrong")){
                                                            Toast.makeText(getApplicationContext(), "Estás intentando acceder desde un dispositivo que no es el que tenemos registrado, por tu seguridad no te daremos acceso", Toast.LENGTH_LONG).show();
                                                        }
                                                        else{
                                                            Toast.makeText(getApplicationContext(), "Correo o contraseña erróneos", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
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
                        else{
                            //threadCounter < 720
                            if(threadCounter < 6){
                                //threadCounter != 719
                                if(threadCounter != 5){
                                    Toast.makeText(getApplicationContext(), "El correo no ha sido verificado aún, reintentando...", Toast.LENGTH_LONG).show();
                                }
                                handler.postDelayed(this, 10000);
                                threadCounter++;
                            }
                            else{
                                finish();
                            }

                        }
                    }
                }
            }
        }, 10000);
    }

    //Este metodo crea la sesion al hacer login:
    public void guardarSesion(String email, String contraseña){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("password", contraseña);
        editor.commit();
    }

    //Este metodo crea un hilo para redirigir a Login Activity pasada la media hora de espera para la confirmacion de la cuenta:
    public void cerrarActivity(String email){
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {

                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "email";

                //Creating array for data
                String[] data = new String[1];
                data[0] = email;

                PutData putData = new PutData("https://seguridadmujer.com/app_movil/LoginRegister/VerifyAccount.php", "POST", field, data);
                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();
                        if(result.equals("Success")) {
                            Toast.makeText(getApplicationContext(), "No se ha verificado la cuenta, ingrese manualmente cuando esta haya sido verificada.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                }

            }
        }, 60000);
        //1800000);
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

    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(CreateAccountActivity.this);
        startActivity(intent);
        finish();
    }

}