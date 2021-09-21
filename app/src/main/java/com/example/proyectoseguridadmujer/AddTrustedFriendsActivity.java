package com.example.proyectoseguridadmujer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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
    Button mButtonConfirm;
    EditText mNombre;
    EditText mTel;
    static final int PICK_CONTACT_REQUEST=1;
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
        //mButtonConfirm.findViewById(R.id.add_confirm_button);

        //ver si tiene amigos o no
        //si tiene mandar a la vista de lista de amigs
        //Si no , quedarse aqui
        LoadView();

        mButtonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContact();
            }
        });


    }
    public void selectContact(){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},0);
            }

            Intent selectContactintent= new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            selectContactintent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(selectContactintent,PICK_CONTACT_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int columnaNombre = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int columnaNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String nombre = cursor.getString(columnaNombre);
                    String number = cursor.getString(columnaNumber);

                    mImageViewIcon.setVisibility(View.INVISIBLE);
                    mTextViewLabel.setVisibility(View.INVISIBLE);
                    mButtonAddFriend.setVisibility(View.INVISIBLE);

                    //mButtonConfirm.setVisibility(View.VISIBLE);
                    mNombre.setVisibility(View.VISIBLE);
                    mTel.setVisibility(View.VISIBLE);

                    mNombre.setText(nombre);
                    mTel.setText(number);


                }
            }
        }
    }

    public void LoadView(){
        Glide.with(this).load("https://seguridadmujer.com/web/icon.png").into(mImageViewIcon);
    }
}