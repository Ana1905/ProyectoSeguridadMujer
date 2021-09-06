package Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoseguridadmujer.R;
import com.example.proyectoseguridadmujer.TermsDialog;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import httpurlconnection.PutData;


public class DialogNewPostFragment extends DialogFragment {

    LinearLayout mLinearLayoutBarra;
    boolean categoryIsChosen;
    int ID_Category;
    EditText mEditTextPostContent;
    Button mButtonClose;
    Button mButtonPublish;
    Button mButtonChooseReport;
    Spinner mSpinnerCategory;
    TextView mTextiewName;
    Date currentDate;

    String category;
    String post_content;
    String email = "";
    String name = "";
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
        mButtonPublish = root.findViewById(R.id.Button_publish);
        mButtonChooseReport = root.findViewById(R.id.button_show_reports);
        mTextiewName = root.findViewById(R.id.user_name_comunity);
        reportes = getResources().getStringArray(R.array.categories);


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
                        // Toast.makeText(parent.getContext(), "Otro", Toast.LENGTH_SHORT).show();
                        showDialog();
                        mButtonChooseReport.setVisibility(View.INVISIBLE);
                        ID_Category = 4;
                        //  EditText txtApellidos = (EditText)root.findViewById(R.id.EditTextCategory);
                        //  Toast.makeText(getActivity(), "Agregado cliente: " + txtApellidos.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        mButtonPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_content = mEditTextPostContent.getText().toString();
                if (post_content.equals("") || categoryIsChosen == false) {
                    if (categoryIsChosen == false) {
                        Toast.makeText(getActivity(), "Debe elegir una categoria", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Debe incluir contenido a su publicación", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "publicando", Toast.LENGTH_SHORT).show();
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