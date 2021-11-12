package Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectoseguridadmujer.Comment;
import com.example.proyectoseguridadmujer.CommentListAdapter;
import com.example.proyectoseguridadmujer.HelpingNetworkPublication;
import com.example.proyectoseguridadmujer.PublicationExtraImageAdapter;
import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DialogShowHelpPublication extends DialogFragment {

    HelpingNetworkPublication mPublication = new HelpingNetworkPublication();
    ArrayList<Comment> mListaComentarios = new ArrayList<Comment>();
    CommentListAdapter mAdapter;
    PublicationExtraImageAdapter ImageAdapter;

    Button BackButton, EditButton;
    ImageView CommentImage;
    TextView User, Category, Content, ImageLabel, CommentLabel;
    RecyclerView CommentRecycler, ImageRecycler;
    ArrayList<String> mListaImagenes = new ArrayList<String>();

    RequestQueue requestQueue;

    public DialogShowHelpPublication () {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPublication.setID_Publicacion(getArguments().getInt("ID"));
        mPublication.setID_Usuaria(getArguments().getInt("ID_Usuaria"));
        mPublication.setID_Categoria(getArguments().getInt("ID_Cateegoria"));
        mPublication.setRespondida(getArguments().getInt("Respondida"));
        mPublication.setContenido(getArguments().getString("Contenido"));
        mPublication.setFecha(getArguments().getString("Fecha"));
        mPublication.setNombre(getArguments().getString("Nombre"));
        mPublication.setNombreCategoria(getArguments().getString("NombreCategoria"));
        mPublication.setRutaImagen(getArguments().getString("RutaImagen"));
        //Toast.makeText(getActivity(), String.valueOf(ID_Publicacion), Toast.LENGTH_SHORT).show();

        //Toast.makeText(getContext(), String.valueOf(mPublication.getID_Publicacion()), Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.dialog_publication_comment, container, false);

        //WiringUp
        BackButton = root.findViewById(R.id.button_dialog_back);
        ImageLabel = root.findViewById(R.id.PublicationImageLabel);
        CommentLabel = root.findViewById(R.id.PublicationCommentLabel);
        EditButton = root.findViewById(R.id.EditCommentButton);
        EditButton.setVisibility(View.GONE);

        CommentRecycler = root.findViewById(R.id.CommentRecycler);
        //CommentRecycler.setAdapter(commentListAdapter);
        CommentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageRecycler = root.findViewById(R.id.ImageRecycler);
        ImageRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));

        CommentImage = root.findViewById(R.id.CommentRowImage);
        User = root.findViewById(R.id.CommentRowUser);
        Category = root.findViewById(R.id.PostCategory);
        Content = root.findViewById(R.id.CommentRowContent);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setPublicationInfo();

        getCommentList("https://seguridadmujer.com/app_movil/HelpingNetwork/getCommentList.php?ID_Publicacion="+mPublication.getID_Publicacion());

        return root;
    }



    //Metodo para obtener los reportes de acontecimiento de la base de datos aun en su tiempo de vida:
    public void getCommentList(String URL){
        //Vacia la lista:
        if(!mListaComentarios.isEmpty()){
            mListaComentarios.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        Comment comment = new Comment();

                        comment.setComentario(jsonObject.getString("Comentario"));
                        comment.setFecha(jsonObject.getString("Fecha"));

                        //Imagen
                        if(jsonObject.getString("ImagenEspecialista") != null && !jsonObject.getString("ImagenEspecialista").isEmpty()){
                            comment.setRutaImagen(jsonObject.getString("ImagenEspecialista"));
                        }
                        else if(jsonObject.getString("ImagenUsuaria") != null && !jsonObject.getString("ImagenUsuaria").isEmpty()){
                            comment.setRutaImagen(jsonObject.getString("ImagenUsuaria"));
                        }
                        else{
                            comment.setRutaImagen("");
                        }

                        //Nombre
                        if(jsonObject.getString("NombreEspecialista") != null && !jsonObject.getString("NombreEspecialista").isEmpty()){
                            comment.setNombre(jsonObject.getString("NombreEspecialista"));
                        }
                        else if(jsonObject.getString("NombreUsuaria") != null && !jsonObject.getString("NombreUsuaria").isEmpty()){
                            comment.setNombre(jsonObject.getString("NombreUsuaria") + " " + jsonObject.getString("ApellidoPaternoUsuaria") + " " + jsonObject.getString("ApellidoMaternoUsuaria"));
                        }
                        else{
                            comment.setNombre("");
                        }

                        mListaComentarios.add(comment);
                        //Toast.makeText(getContext(), mListaComentarios.get(i).getComentario(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getContext(), comment.getComentario() + ", " + comment.getFecha() + ", " + comment.getNombre() + ", " + comment.getRutaImagen(), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                setAdapter();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

        obtenerImagenes("https://seguridadmujer.com/app_movil/HelpingNetwork/obtenerImagenesAdicionales.php?ID_Publicacion="+mPublication.getID_Publicacion());
    }

    private void setAdapter(){
        mAdapter = new CommentListAdapter(getContext(), mListaComentarios);
        CommentRecycler.setAdapter(mAdapter);
        if (mListaComentarios.isEmpty())
        {
            CommentLabel.setVisibility(View.GONE);
            CommentRecycler.setVisibility(View.GONE);
        }
        else
        {
            CommentLabel.setVisibility(View.VISIBLE);
            CommentRecycler.setVisibility(View.VISIBLE);
        }
    }

    public void obtenerImagenes(String URL){
        //Vacia la lista:
        if(!mListaImagenes.isEmpty()){
            mListaImagenes.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        mListaImagenes.add(jsonObject.getString("RutaImagen"));
                        //Toast.makeText(getApplicationContext(), jsonObject.getString("RutaImagen"), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                mostrarImagenesAdicionales();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void mostrarImagenesAdicionales(){
        if(!mListaImagenes.isEmpty()){
            ImageLabel.setVisibility(View.VISIBLE);
            ImageAdapter = new PublicationExtraImageAdapter(getContext(), mListaImagenes);
            ImageRecycler.setAdapter(ImageAdapter);
            ImageRecycler.setVisibility(View.VISIBLE);
        }
        else{
            ImageLabel.setVisibility(View.GONE);
            ImageRecycler.setVisibility(View.GONE);
        }
    }

    private void setPublicationInfo(){
        User.setText(mPublication.getNombre());
        Category.setText(mPublication.getNombreCategoria());
        Content.setText(mPublication.getContenido());

        if(mPublication.getRutaImagen() != null && !mPublication.getRutaImagen().isEmpty()){
            Glide.with(getContext()).load(mPublication.getRutaImagen()).into(CommentImage);
        }
        else{
            Glide.with(getContext()).load("http://seguridadmujer.com/web/Resources/avatar_woman_people_girl_glasses_icon_159125.png").into(CommentImage);
        }
    }
}