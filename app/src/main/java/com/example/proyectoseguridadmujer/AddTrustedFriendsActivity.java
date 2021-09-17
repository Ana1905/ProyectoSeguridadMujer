package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



import com.bumptech.glide.Glide;

public class AddTrustedFriendsActivity extends AppCompatActivity {

    ImageView mImageViewIcon;
    TextView mTextViewLabel;
    Button mButtonAddFriend;
    EditText mNombre;
    EditText mTel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trusted_friends);

        //Wiring up
        mImageViewIcon= findViewById(R.id.imageViewAdd);
        mTextViewLabel=findViewById(R.id.TextViewLabeladd);
        mButtonAddFriend=findViewById(R.id.alert_Add_friend);
        mNombre= findViewById(R.id.nombre);
        mTel= findViewById(R.id.telefono);
        LoadView();
    }

    public void LoadView(){



        Glide.with(this).load("https://seguridadmujer.com/web/icon.png").into(mImageViewIcon);
    }
}