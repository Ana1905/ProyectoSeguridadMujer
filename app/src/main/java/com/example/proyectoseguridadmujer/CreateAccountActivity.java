package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import httpurlconnection.PutData;

public class CreateAccountActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;

    EditText mEditTextCreateAccountEmail, mEditTextCreateAccountName, mEditTextCreateAccountPaternalSurname, mEditTextCreateAccountMaternalSurname,  mEditTextCreateAccountPassword, mEditTextCreateAccountConfirmPassword;
    Button mButtonCreateAccount,  mButtonDateOfBirth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initDatePicker();

        //Wiring up
        mEditTextCreateAccountEmail= findViewById(R.id.create_account_email);
        mEditTextCreateAccountName = findViewById(R.id.create_account_name);
        mEditTextCreateAccountPaternalSurname = findViewById(R.id.create_account_paternal_surname);
        mEditTextCreateAccountMaternalSurname = findViewById(R.id.create_account_maternal_surname);
        mButtonDateOfBirth = findViewById(R.id.create_account_dateOfBirth);
        mEditTextCreateAccountPassword = findViewById(R.id.create_account_password);
        mEditTextCreateAccountConfirmPassword = findViewById(R.id.create_account_confirm_password);
        mButtonCreateAccount = findViewById(R.id.create_account_create);
        mButtonDateOfBirth.setText(getTodaysDate());

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
                fecha_nacimiento = String.valueOf(mButtonDateOfBirth.getText());
                contraseña = String.valueOf(mEditTextCreateAccountPassword.getText());
                confirmPassword = String.valueOf(mEditTextCreateAccountConfirmPassword.getText());

                if (!email.equals("") && !nombres.equals("") && !apellido_paterno.equals("") && !apellido_materno.equals("") && !fecha_nacimiento.equals("") && !contraseña.equals("")) {
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

                            String[] data2 = new String[1];
                            data[0] = email;

                            String[] field2 = new String[1];
                            field[0] = "email";

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






                            PutData putData = new PutData("http://192.168.56.1:80/LoginRegister/signup.php", "POST", field, data);


                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")){
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

    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString( day, month, year);
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
        return month + "/" +  day + "/" + year ;
    }

    public void openDatePicker(View view) {
            datePickerDialog.show();
    }
}