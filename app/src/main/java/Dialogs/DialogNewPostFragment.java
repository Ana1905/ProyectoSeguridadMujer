package Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Base64;

import httpurlconnection.PutData;


public class DialogNewPostFragment extends DialogFragment {

    LinearLayout mLinearLayoutBarra;
    boolean categoryIsChosen;
    int ID_Category;
    int Counter = 0;
    EditText mEditTextPostContent;
    Button mButtonClose;
    Button mButtonImage;
    Button mButtonDelete;
    Button mButtonPublish;
    Button mButtonChooseReport;
    ImageView [] mImagePublication;
    Spinner mSpinnerCategory;
    TextView mTextiewName;
    Date currentDate;
    Bitmap bitmap;
    String [] imagesToStrings;
    private static final int REQUEST_GALERIA = 1;

    String category;
    String post_content;
    String email = "";
    String name = "";
    String contraseña = "";
    Activity actividad;
    private String[] reportes;

    //IComunicaFragments iComunicaFragments;

    public DialogNewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dialog_new_post, container, false);
        mEditTextPostContent = root.findViewById(R.id.post_content_label_comunity);
        mButtonClose = root.findViewById(R.id.button_close);
        mButtonImage = root.findViewById(R.id.Button_add_image);
        mButtonDelete = root.findViewById(R.id.Button_delete_image);
        mButtonDelete.setVisibility(View.INVISIBLE);
        mButtonPublish = root.findViewById(R.id.Button_publish);
        mButtonChooseReport = root.findViewById(R.id.button_show_reports);
        mTextiewName = root.findViewById(R.id.user_name_comunity);
        reportes = getResources().getStringArray(R.array.categories);

        imagesToStrings = new String[10];

        mImagePublication = new ImageView [10];
        mImagePublication [0] = root.findViewById(R.id.New_publication_image_1);
        mImagePublication [1] = root.findViewById(R.id.New_publication_image_2);
        mImagePublication [2] = root.findViewById(R.id.New_publication_image_3);
        mImagePublication [3] = root.findViewById(R.id.New_publication_image_4);
        mImagePublication [4] = root.findViewById(R.id.New_publication_image_5);
        mImagePublication [5] = root.findViewById(R.id.New_publication_image_6);
        mImagePublication [6] = root.findViewById(R.id.New_publication_image_7);
        mImagePublication [7] = root.findViewById(R.id.New_publication_image_8);
        mImagePublication [8] = root.findViewById(R.id.New_publication_image_9);
        mImagePublication [9] = root.findViewById(R.id.New_publication_image_10);

        mButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Closing", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        mSpinnerCategory = root.findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.categories, android.R.layout.simple_spinner_item);
        mSpinnerCategory.setAdapter(adapter);


        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        //Toast.makeText(parent.getContext(), "Seleccione una categoria", Toast.LENGTH_SHORT).show();
                        categoryIsChosen = false;
                        mButtonChooseReport.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        //Toast.makeText(parent.getContext(), "Contar una experiencia sobre violencia", Toast.LENGTH_SHORT).show();
                        categoryIsChosen = true;
                        mButtonChooseReport.setVisibility(View.INVISIBLE);
                        category="Contar una experiencia sobre violencia";
                        ID_Category = 1;
                        break;
                    case 2:
                        //Toast.makeText(parent.getContext(), "Anuncio importante sobre la seguridad", Toast.LENGTH_SHORT).show();
                        categoryIsChosen = true;
                        mButtonChooseReport.setVisibility(View.INVISIBLE);
                        category="Anuncio importante sobre la seguridad";
                        ID_Category = 2;
                        break;
                    case 3:
                        //Toast.makeText(parent.getContext(), "Reporte de acontecimiento", Toast.LENGTH_SHORT).show();
                        categoryIsChosen = true;
                        mButtonChooseReport.setVisibility(View.VISIBLE);
                        mButtonChooseReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(parent.getContext(), "Reporte", Toast.LENGTH_SHORT).show();
                                open_reports();
                            }
                        });
                        category="Reporte de acontecimiento";
                        ID_Category = 3;
                        break;

                    case 4:
                        categoryIsChosen = true;
                        mButtonChooseReport.setVisibility(View.INVISIBLE);
                        category="otro";
                        ID_Category = 4;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        mButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Counter < 10)
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent, "Selecciona una imagen"), REQUEST_GALERIA);
                }
                else
                {
                    Toast.makeText(getActivity(), "Por que no me estas besando", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Counter--;
                mImagePublication[Counter].setVisibility(View.INVISIBLE);
                if (Counter == 0)
                {
                    mButtonDelete.setVisibility(View.INVISIBLE);
                }
            }
        });

        mButtonPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                post_content = mEditTextPostContent.getText().toString();
                if (post_content.equals("") || categoryIsChosen == false)
                {
                    if (categoryIsChosen == false)
                    {
                        Toast.makeText(getActivity(), "Debe elegir una categoria", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Debe incluir contenido a su publicación", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {

                    Toast.makeText(getActivity(), "publicando", Toast.LENGTH_SHORT).show();
                    SendImage();
                    savePost();
                    dismiss();
                }
            }
        });


        getName();
        mTextiewName.setText(name);

        //cargarDatos();


        return root;
    }

    private void showDialog() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // Create and show the dialog.
        DialogOtherCategory dialogOtherCategory = new DialogOtherCategory();
        // con este tema personalizado evitamos los bordes por defecto
        //DialogOtherCategory dialogOtherCategory = new Dialog(this,R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        //dialogOtherCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        dialogOtherCategory.setCancelable(false);

        dialogOtherCategory.show(ft, "dialog");
        categoryIsChosen = false;


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALERIA)
        {
            Uri uri = data.getData();
            mImagePublication[Counter].setImageURI(uri);
            mImagePublication[Counter].setVisibility(View.VISIBLE);
            mButtonDelete.setVisibility(View.VISIBLE);

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                ByteArrayOutputStream array = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
                byte [] imageByte = array.toByteArray();
                imagesToStrings[Counter] = Base64.getEncoder().encodeToString(imageByte);
            }
            catch (IOException e)
            {

            }

            Counter++;
        }
        Toast.makeText(getActivity(), String.valueOf(requestCode), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        return dialog;
    }


    public void getCredentialData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");
        // getName();
    }

    public void SendImage ()
    {
        /*
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
                    Toast.makeText(getActivity(), "El correo ha sido verificado", Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                                            guardarSesion(email, contraseña);
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            if(result.equals("Missing email verification email or Password wrong")){
                                                Toast.makeText(getActivity(), "La cuenta aún no ha sido creada pues no se ha verificado la dirección de correo", Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                if(result.equals("phones not match email or Password wrong")){
                                                    Toast.makeText(getActivity(), "Estás intentando acceder desde un dispositivo que no es el que tenemos registrado, por tu seguridad no te daremos acceso", Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    Toast.makeText(getActivity(), "Correo o contraseña erróneos", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "El correo no ha sido verificado aún, reintentando...", Toast.LENGTH_LONG).show();
                    handler.postDelayed(this, 10000);
                }
            }
        }
        */
    }

    public void savePost() {
        Toast.makeText(getActivity(), "save Post method", Toast.LENGTH_SHORT).show();

        //Starting Write and Read data with URL
        //Creating array for parameters
        String[] field = new String[3];
        field[0] = "email";
        field[1] = "categoria";
        field[2] = "contenido";


        //Creating array for data
        String[] data = new String[3];
        data[0] = email;
        data[1] = category;
        data[2] = post_content;


        //PutData putData = new PutData("http://seguridadmujer.com/app_movil/Community/getName.php", "POST", field, data);
        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Community/savePost.php", "POST", field, data);
        //  PutData putData = new PutData("http://seguridadmujer.com/app_movil/LoginRegister/login.php", "POST", field, data);

        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                //Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
            }
        }
        //End Write and Read data with URL

    }

    private void getName() {

        getCredentialData();


        //Starting Write and Read data with URL
        //Creating array for parameters
        String[] field = new String[1];
        field[0] = "email";


        //Creating array for data
        String[] data = new String[1];
        data[0] = email;


        //PutData putData = new PutData("http://seguridadmujer.com/app_movil/Community/getName.php", "POST", field, data);
        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Community/getName.php", "POST", field, data);
        //  PutData putData = new PutData("http://seguridadmujer.com/app_movil/LoginRegister/login.php", "POST", field, data);

        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();

                name = result;
                //Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
            }
        }
        //End Write and Read data with URL
    

    }

    public void open_reports(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reportes");
        builder.setItems(R.array.categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), reportes[which], Toast.LENGTH_SHORT).show();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }


}