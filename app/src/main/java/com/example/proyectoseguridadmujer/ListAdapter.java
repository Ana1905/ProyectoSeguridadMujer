package com.example.proyectoseguridadmujer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Dialogs.DialogNewPostFragment;
import httpurlconnection.PutData;

import android.app.Fragment;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.PublicationViewHolder>
{
    private Context context;
    private List<ListElement> list = new ArrayList<>();
    private String email;

    public ListAdapter(Context context, List<ListElement> itemList)
    {
        this.context = context;
        this.list =itemList;
    }   


    @Override
    public PublicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_post,null);
        return new PublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PublicationViewHolder holder, final int position) {
        holder.Username.setText(list.get(position).getNombre());
        holder.Category.setText(list.get(position).getNombreCategoria());
        holder.Content.setText(list.get(position).getContenido());
        holder.bind(list.get(position));

        holder.ReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "Reportar publicación", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context.getApplicationContext(), ReportPublicationActivity.class);
                intent.putExtra("Usuario", list.get(position).getNombre());
                intent.putExtra("Categoria", list.get(position).getNombreCategoria());
                intent.putExtra("Contenido", list.get(position).getContenido());
                intent.putExtra("ID", list.get(position).getID_Publicacion());
                intent.putExtra("ID_Usuaria", list.get(position).getID_Usuaria());
                context.startActivity(intent);
            }
        });

        holder.SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "Guardando", Toast.LENGTH_SHORT).show();
                SavePublication(list.get(position).getID_Publicacion(), list.get(position).getID_Usuaria());
            }
        });

        holder.Comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String CommentInput = holder.Comment.getText().toString();
                if (CommentInput.isEmpty())
                {
                    holder.SendComment.setVisibility(View.INVISIBLE);
                    Toast.makeText(context.getApplicationContext(), String.valueOf(position) + " " + list.size(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    holder.SendComment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.SendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "Comentando", Toast.LENGTH_SHORT).show();
                //Toast.makeText(context.getApplicationContext(), String.valueOf(position) + " " + list.size(), Toast.LENGTH_SHORT).show();
                //holder.Category.setText(holder.Comment.getText().toString());
                SendComment(holder.Comment.getText().toString(), list.get(position).getID_Publicacion(), list.get(position).getID_Usuaria());
                holder.Comment.setText("");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context.getApplicationContext(), String.valueOf(position) + " " + list.size(), Toast.LENGTH_SHORT).show();

                Toast.makeText(context.getApplicationContext(), "Mostrar comentarios", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context.getApplicationContext(), CommentListActivity.class);
                intent.putExtra("Usuario", list.get(position).getNombre());
                intent.putExtra("Categoria", list.get(position).getNombreCategoria());
                intent.putExtra("Contenido", list.get(position).getContenido());
                intent.putExtra("ID", list.get(position).getID_Publicacion());
                //intent.putExtra("ID_Usuaria", list.get(position).getID_Usuaria());
                context.startActivity(intent);

                //ShowCommentDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void SendComment (String Comment, int PublicationID, int UserID)
    {
        getCredentialData();
        String[] field = new String[3];
        field[0] = "Contenido";
        field[1] = "ID_Publicacion";
        field[2] = "ID_Usuaria";

        String[] data = new String[3];
        data[0] = Comment;
        data[1] = String.valueOf(PublicationID);
        data[2] = email;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Community/GuardarComentarioPublicacion.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Success")) {
                    Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context.getApplicationContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void SavePublication (int PublicationID, int UserID)
    {
        getCredentialData();
        String[] field = new String[2];
        field[0] = "ID_Publicacion";
        field[1] = "ID_Usuaria";

        String[] data = new String[2];
        data[0] = String.valueOf(PublicationID);
        data[1] = email;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Community/GuardarPublicacionGuardada.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Success")) {
                    Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context.getApplicationContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void ShowCommentDialog ()
    {
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //(AppCompatActivity (context)).getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();

        //DialogFragment dialog = (DialogFragment) DialogFragment.instantiate(context.getApplicationContext(), "Comment");
        //dialog.show(DialogNewPostFragment.getFragmentManager(), "Comentario");

        //DialogNewPostFragment dialogNewPostFragment = new DialogNewPostFragment();
        //dialogNewPostFragment.show(dialogNewPostFragment.getFragmentManager(), "Comment");


    }

    public void getCredentialData()
    {
        SharedPreferences preferences = context.getSharedPreferences("Credencials",context.MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public class PublicationViewHolder extends RecyclerView.ViewHolder
    {
        TextView Username;
        TextView Category;
        TextView Content;
        Button ReportButton;
        Button SaveButton;
        EditText Comment;
        Button SendComment;
        ListElement Data;

        public PublicationViewHolder(View itemView)
        {
            super(itemView);
            Username = itemView.findViewById(R.id.user_name_comunity);
            Category = itemView.findViewById(R.id.post_category_label_comunity);
            Content = itemView.findViewById(R.id.post_content_label_comunity);
            ReportButton = itemView.findViewById(R.id.button_report_post_comunity);
            SaveButton = itemView.findViewById(R.id.button_save_post_comunity);
            Comment = itemView.findViewById(R.id.Add_Comment_Edit_Text);
            SendComment = itemView.findViewById(R.id.Add_Comment_Send_Button);
        }

        public void bind (ListElement listElement)
        {
            Data = listElement;
        }
    }
}
