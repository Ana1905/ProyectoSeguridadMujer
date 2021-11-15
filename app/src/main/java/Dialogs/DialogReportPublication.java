package Dialogs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;
import com.example.proyectoseguridadmujer.MainActivity;
import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;

import httpurlconnection.PutData;

public class DialogReportPublication extends DialogFragment {
    TextView User, Category, Content;
    Button Publish, Back;
    String UserLabel, CategoryLabel, ContentLabel, CommentLabel, email;
    int PublicationID, UserID, CategoryID;
    Spinner Reason;
    EditText Comment;

    RequestQueue requestQueue;

    public DialogReportPublication () {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserLabel = getArguments().getString("Usuario");
        CategoryLabel = getArguments().getString("Categoria");
        ContentLabel = getArguments().getString("Contenido");
        PublicationID = getArguments().getInt("ID");
        UserID = getArguments().getInt("ID_Usuaria");
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_report_publication, container, false);

        //WiringUp
        User = root.findViewById(R.id.ReportPublicationUser);
        Category = root.findViewById(R.id.ReportPublicationCategory);
        Content = root.findViewById(R.id.ReportPublicationContent);

        Back = root.findViewById(R.id.ReportBackButton);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Reason = root.findViewById(R.id.ReportPublicationReason);
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ReportCategory, android.R.layout.simple_spinner_item);
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

        Comment = root.findViewById(R.id.ReportPublicationComment);
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


        Publish = root.findViewById(R.id.ReportPublicationButton);
        Publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Reportando", Toast.LENGTH_SHORT).show();
                ReportPublication();
                dismiss();
            }
        });

        getCredentialData();
        setPublicationInfo ();

        return root;
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
                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = getContext().getSharedPreferences("Credencials",getContext().MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    void setPublicationInfo ()
    {
        User.setText(UserLabel);
        Category.setText(CategoryLabel);
        Content.setText(ContentLabel);
    }
}