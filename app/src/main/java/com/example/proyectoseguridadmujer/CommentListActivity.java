package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import httpurlconnection.PutData;

public class CommentListActivity extends AppCompatActivity
{

    int PublicationID, UserID;
    String email, UserLabel, CategoryLabel, ContentLabel;
    ImageView CommentImage;
    TextView User, Category, Content;
    RecyclerView CommentRecycler;
    List<ListElement> CommentList;

    CommentListAdapter commentListAdapter;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_publication_comment);

        //WiringUp
        CommentRecycler = findViewById(R.id.CommentRecycler);
        //CommentRecycler.setAdapter(commentListAdapter);
        CommentRecycler.setLayoutManager(new LinearLayoutManager(this));

        CommentImage = findViewById(R.id.CommentRowImage);
        User = findViewById(R.id.CommentRowUser);
        Category = findViewById(R.id.PostCategory);
        Content = findViewById(R.id.CommentRowContent);

        getPublicationInfo();
        setPublicationInfo();
        getCredentialData();

        getCommentList(getString(R.string.Comment_list_url));
    }

    public void getCommentList(String Link)
    {
        String[] field = new String[1];
        field[0] = "ID_publicacion";
        String[] data = new String[1];
        data[0] = String.valueOf(PublicationID);

        PutData putData = new PutData(Link, "POST", field, data);
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
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Link, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                String CommentData = response.toString();
                Gson gson = new Gson();
                ListElement[] CommentRegister = gson.fromJson(CommentData, ListElement[].class);

                CommentList = Arrays.asList(CommentRegister);

                setAdapter();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    public void setAdapter ()
    {
        commentListAdapter = new CommentListAdapter(CommentListActivity.this, CommentList);
        CommentRecycler.setAdapter(commentListAdapter);
    }

    void getPublicationInfo ()
    {
        Bundle bundle = getIntent().getExtras();
        UserLabel = bundle.getString("Usuario");
        CategoryLabel = bundle.getString("Categoria");
        ContentLabel = bundle.getString("Contenido");
        PublicationID = bundle.getInt("ID");
        //UserID = bundle.getInt("ID_Usuaria");
    }

    void setPublicationInfo ()
    {
        User.setText(UserLabel);
        Category.setText(CategoryLabel);
        Content.setText(ContentLabel);
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = NavUtils.getParentActivityIntent(CommentListActivity.this);
        startActivity(intent);
        finish();
    }
}