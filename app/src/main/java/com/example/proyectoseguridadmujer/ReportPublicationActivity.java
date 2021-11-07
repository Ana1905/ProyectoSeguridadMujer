package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import httpurlconnection.PutData;

public class ReportPublicationActivity extends AppCompatActivity {

    TextView User, Category, Content;
    Button Publish;
    String UserLabel, CategoryLabel, ContentLabel, CommentLabel, email;
    int PublicationID, UserID, CategoryID;
    Spinner Reason;
    EditText Comment;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_publication);

        //Wiring Up
        User = findViewById(R.id.ReportPublicationUser);
        Category = findViewById(R.id.ReportPublicationCategory);
        Content = findViewById(R.id.ReportPublicationContent);

        Reason = findViewById(R.id.ReportPublicationReason);
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this, R.array.ReportCategory, android.R.layout.simple_spinner_item);
        Reason.setAdapter(Adapter);
        Reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryID = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Comment = findViewById(R.id.ReportPublicationComment);
        Comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String CommentInput = Comment.getText().toString();
                Publish.setEnabled(!CommentInput.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Publish = findViewById(R.id.ReportPublicationButton);
        Publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Reportando", Toast.LENGTH_SHORT).show();
                ReportPublication();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        getCredentialData();
        getPublicationInfo ();
        setPublicationInfo ();
    }

    void getPublicationInfo ()
    {
        Bundle bundle = getIntent().getExtras();
        UserLabel = bundle.getString("Usuario");
        CategoryLabel = bundle.getString("Categoria");
        ContentLabel = bundle.getString("Contenido");
        PublicationID = bundle.getInt("ID");
        UserID = bundle.getInt("ID_Usuaria");
    }

    void setPublicationInfo ()
    {
        User.setText(UserLabel);
        Category.setText(CategoryLabel);
        Content.setText(ContentLabel);
    }

    void ReportPublication ()
    {
        CommentLabel = Comment.getText().toString();

        String[] field = new String[5];
        field[0] = "Categoria";
        field[1] = "Descripcion";
        field[2] = "ID_Publicacion";
        field[3] = "ID_UsuariaReportada";
        field[4] = "ID_Usuaria";

        String[] data = new String[5];
        data[0] = String.valueOf(CategoryID);
        data[1] = CommentLabel;
        data[2] = String.valueOf(PublicationID);
        data[3] = String.valueOf(UserID);
        data[4] = String.valueOf(email);

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Community/GuardarReportePublicacion.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Success")) {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = NavUtils.getParentActivityIntent(ReportPublicationActivity.this);
        startActivity(intent);
        finish();
    }
}
