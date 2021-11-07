package Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import httpurlconnection.PutData;

public class DialogNewHelpPostFragment extends DialogFragment {

    boolean categoryIsChosen;
    int ID_Category;
    int Counter = 0;
    EditText mEditTextPostContent;
    Button mButtonClose;
    Button mButtonImage;
    Button mButtonDelete;
    Button mButtonPublish;
    Button mButtonChooseReport;
    ImageView[] mImagePublication;
    Spinner mSpinnerCategory;
    TextView mTextViewName;
    Bitmap bitmap;
    String [] imagesToStrings;
    String [] imagesType;
    private static final int REQUEST_GALERIA = 1;

    String category;
    String post_content;
    String email = "";
    String name = "";
    String contraseña = "";
    private String[] reportes;

    public DialogNewHelpPostFragment () {
        // Required empty public constructor
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        //WiringUp
        View root = inflater.inflate(R.layout.fragment_dialog_new_help_post, container, false);
        mEditTextPostContent = root.findViewById(R.id.Post_Content_New_Helping_Network_Post);
        mButtonClose = root.findViewById(R.id.button_close_HN);
        mButtonImage = root.findViewById(R.id.Button_Add_Image_HN);
        mButtonDelete = root.findViewById(R.id.Button_Delete_Image_HN);
        mButtonDelete.setVisibility(View.INVISIBLE);
        mButtonPublish = root.findViewById(R.id.Button_Publish_HN);
        mButtonChooseReport = root.findViewById(R.id.Button_Show_Reports_HN);
        mTextViewName = root.findViewById(R.id.User_Name_New_Helping_Network_Post);
        reportes = getResources().getStringArray(R.array.Helping_Network_Categories);

        mImagePublication = new ImageView [10];
        mImagePublication [0] = root.findViewById(R.id.New_Helping_Publication_Image_1);
        mImagePublication [1] = root.findViewById(R.id.New_Helping_Publication_Image_2);
        mImagePublication [2] = root.findViewById(R.id.New_Helping_Publication_Image_3);
        mImagePublication [3] = root.findViewById(R.id.New_Helping_Publication_Image_4);
        mImagePublication [4] = root.findViewById(R.id.New_Helping_Publication_Image_5);
        mImagePublication [5] = root.findViewById(R.id.New_Helping_Publication_Image_6);
        mImagePublication [6] = root.findViewById(R.id.New_Helping_Publication_Image_7);
        mImagePublication [7] = root.findViewById(R.id.New_Helping_Publication_Image_8);
        mImagePublication [8] = root.findViewById(R.id.New_Helping_Publication_Image_9);
        mImagePublication [9] = root.findViewById(R.id.New_Helping_Publication_Image_10);

        imagesToStrings = new String[10];
        imagesToStrings [0] = "";
        imagesToStrings [1] = "";
        imagesToStrings [2] = "";
        imagesToStrings [3] = "";
        imagesToStrings [4] = "";
        imagesToStrings [5] = "";
        imagesToStrings [6] = "";
        imagesToStrings [7] = "";
        imagesToStrings [8] = "";
        imagesToStrings [9] = "";

        imagesType = new String[10];
        imagesType [0] = "";
        imagesType [1] = "";
        imagesType [2] = "";
        imagesType [3] = "";
        imagesType [4] = "";
        imagesType [5] = "";
        imagesType [6] = "";
        imagesType [7] = "";
        imagesType [8] = "";
        imagesType [9] = "";

        mButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Closing", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        mSpinnerCategory = root.findViewById(R.id.spinner_category_New_Helping_Network_Post);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Helping_Network_Categories, android.R.layout.simple_spinner_item);
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
                        //Toast.makeText(parent.getContext(), "Apoyo sobre violencia doméstica", Toast.LENGTH_SHORT).show();
                        categoryIsChosen = true;
                        mButtonChooseReport.setVisibility(View.INVISIBLE);
                        category="Apoyo sobre violencia doméstica";
                        ID_Category = 1;
                        break;
                    case 2:
                        //Toast.makeText(parent.getContext(), "Apoyo sobre discriminación", Toast.LENGTH_SHORT).show();
                        categoryIsChosen = true;
                        mButtonChooseReport.setVisibility(View.INVISIBLE);
                        category="Apoyo sobre discriminación";
                        ID_Category = 2;
                        break;
                    case 3:
                        //Toast.makeText(parent.getContext(), "Apoyo sobre abuso sexual", Toast.LENGTH_SHORT).show();
                        categoryIsChosen = true;
                        /*
                        mButtonChooseReport.setVisibility(View.VISIBLE);
                        mButtonChooseReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(parent.getContext(), "Reporte", Toast.LENGTH_SHORT).show();
                                open_reports();
                            }
                        });
                        */
                        category="Apoyo sobre abuso sexual";
                        ID_Category = 3;
                        break;

                    case 4:
                        //Toast.makeText(parent.getContext(), "Apoyo sobre un suceso de acoso", Toast.LENGTH_SHORT).show();
                        categoryIsChosen = true;
                        mButtonChooseReport.setVisibility(View.INVISIBLE);
                        category="Apoyo sobre un suceso de acoso";
                        ID_Category = 4;
                        break;
                    case 5:
                        //Toast.makeText(parent.getContext(), "Apoyo sobre un acontecimiento de abuso infantil", Toast.LENGTH_SHORT).show();
                        categoryIsChosen = true;
                        mButtonChooseReport.setVisibility(View.INVISIBLE);
                        category="Apoyo sobre un acontecimiento de abuso infantil";
                        ID_Category = 5;
                        break;
                    case 6:
                        //Toast.makeText(parent.getContext(), "Apoyo sobre violencia de pareja", Toast.LENGTH_SHORT).show();
                        categoryIsChosen = true;
                        mButtonChooseReport.setVisibility(View.INVISIBLE);
                        category="Apoyo sobre violencia de pareja";
                        ID_Category = 6;
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
            }
        });

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Counter--;
                mButtonImage.setVisibility(View.VISIBLE);
                mImagePublication[Counter].setVisibility(View.INVISIBLE);
                imagesToStrings[Counter] = "";
                imagesType[Counter] = "";
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
                    dismiss();
                }
            }
        });

        getName();
        mTextViewName.setText(name);

        return root;
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

                ContentResolver contentResolver = getContext().getContentResolver();
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                imagesType[Counter] = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

                if (imagesType[Counter] == "jpg")
                {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
                }
                else if (imagesType[Counter] == "png")
                {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, array);
                }
                else
                {
                    Toast.makeText(getActivity(), "Por favor introduce un archivo valido", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(), imagesType[Counter], Toast.LENGTH_SHORT).show();

                byte [] imageByte = array.toByteArray();
                imagesToStrings[Counter] = Base64.encodeToString(imageByte, Base64.DEFAULT);
                //Toast.makeText(getActivity(), String.valueOf(imagesToStrings[Counter]), Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {

            }

            Counter++;
            if (Counter == 10)
            {
                mButtonImage.setVisibility(View.INVISIBLE);
            }
        }
        Toast.makeText(getActivity(), String.valueOf(Counter), Toast.LENGTH_SHORT).show();
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
        post_content = mEditTextPostContent.getText().toString();

        String[] field = new String[23];
        field[0] = "email";
        field[1] = "categoria";
        field[2] = "contenido";
        field[3] = "Imagen1";
        field[4] = "Imagen2";
        field[5] = "Imagen3";
        field[6] = "Imagen4";
        field[7] = "Imagen5";
        field[8] = "Imagen6";
        field[9] = "Imagen7";
        field[10] = "Imagen8";
        field[11] = "Imagen9";
        field[12] = "Imagen10";
        field[13] = "TipoImagen1";
        field[14] = "TipoImagen2";
        field[15] = "TipoImagen3";
        field[16] = "TipoImagen4";
        field[17] = "TipoImagen5";
        field[18] = "TipoImagen6";
        field[19] = "TipoImagen7";
        field[20] = "TipoImagen8";
        field[21] = "TipoImagen9";
        field[22] = "TipoImagen10";

        String[] data = new String[23];
        data[0] = email;
        data[1] = String.valueOf(ID_Category);
        data[2] = post_content;
        data[3] = imagesToStrings[0];
        data[4] = imagesToStrings[1];
        data[5] = imagesToStrings[2];
        data[6] = imagesToStrings[3];
        data[7] = imagesToStrings[4];
        data[8] = imagesToStrings[5];
        data[9] = imagesToStrings[6];
        data[10] = imagesToStrings[7];
        data[11] = imagesToStrings[8];
        data[12] = imagesToStrings[9];
        data[13] = imagesType[0];
        data[14] = imagesType[1];
        data[15] = imagesType[2];
        data[16] = imagesType[3];
        data[17] = imagesType[4];
        data[18] = imagesType[5];
        data[19] = imagesType[6];
        data[20] = imagesType[7];
        data[21] = imagesType[8];
        data[22] = imagesType[9];

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/HelpingNetwork/GuardarPublicacionRedDeApoyo.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Success")) {
                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
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
        PutData putData = new PutData("https://seguridadmujer.com/app_movil/HelpingNetwork/getName.php", "POST", field, data);
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
        builder.setItems(R.array.Helping_Network_Categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), reportes[which], Toast.LENGTH_SHORT).show();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
}
