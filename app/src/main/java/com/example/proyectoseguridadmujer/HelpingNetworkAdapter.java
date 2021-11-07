package com.example.proyectoseguridadmujer;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import httpurlconnection.PutData;

public class HelpingNetworkAdapter extends RecyclerView.Adapter<HelpingNetworkAdapter.HelpPublicationViewHolder> implements Filterable {

    private Context context;
    private List<HelpingNetworkPublication> list = new ArrayList<>();
    private List<HelpingNetworkPublication> FullList = new ArrayList<>();
    private String email;


    public HelpingNetworkAdapter(Context context, List<HelpingNetworkPublication> itemList)
    {
        this.context = context;
        this.list =itemList;
        FullList = list;
    }

    @NonNull
    @NotNull
    @Override
    public HelpPublicationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.help_post_row, null);
        return new HelpPublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HelpPublicationViewHolder holder, int position) {
        holder.Username.setText(list.get(position).getNombre());
        holder.Category.setText(list.get(position).getNombreCategoria());
        holder.Content.setText(list.get(position).getContenido());
        holder.bind(list.get(position));

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "Borrando", Toast.LENGTH_SHORT).show();
                DeletePublication(list.get(position).getID_Publicacion());
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

                /*
                Intent intent = new Intent(context.getApplicationContext(), CommentListActivity.class);
                intent.putExtra("Usuario", list.get(position).getNombre());
                intent.putExtra("Categoria", list.get(position).getNombreCategoria());
                intent.putExtra("Contenido", list.get(position).getContenido());
                intent.putExtra("ID", list.get(position).getID_Publicacion());
                //intent.putExtra("ID_Usuaria", list.get(position).getID_Usuaria());
                context.startActivity(intent);
                //ShowCommentDialog();
                */
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter ()
    {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HelpingNetworkPublication> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(FullList);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (HelpingNetworkPublication publication : FullList)
                {
                    if (String.valueOf(publication.getID_Categoria()).toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(publication);
                    }else if (publication.getContenido().toLowerCase().contains(filterPattern)){
                        filteredList.add(publication);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            list = new ArrayList<HelpingNetworkPublication>();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    void DeletePublication (int PublicationID)
    {
        String[] field = new String[1];
        field[0] = "ID_Publicacion";

        String[] data = new String[1];
        data[0] = String.valueOf(PublicationID);

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/HelpingNetwork/EliminarPublicacion.php", "POST", field, data);
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

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/HelpingNetwork/GuardarComentarioRedDeApoyo.php", "POST", field, data);
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

    public void getCredentialData()
    {
        SharedPreferences preferences = context.getSharedPreferences("Credencials",context.MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public class HelpPublicationViewHolder extends RecyclerView.ViewHolder
    {
        TextView Username;
        TextView Category;
        Button Delete;
        TextView Content;
        EditText Comment;
        Button SendComment;
        HelpingNetworkPublication Data;

        public HelpPublicationViewHolder (View itemView)
        {
            super(itemView);
            Username = itemView.findViewById(R.id.user_name_helping_network);
            Category = itemView.findViewById(R.id.post_category_label_helping_network);
            Delete = itemView.findViewById(R.id.Delete_Button_HN);
            Content = itemView.findViewById(R.id.post_content_label_helping_network);
            Comment = itemView.findViewById(R.id.Add_Comment_Edit_Text_helping_network);
            SendComment = itemView.findViewById(R.id.Add_Comment_Send_Button_helping_network);
        }

        public void bind (HelpingNetworkPublication listElement)
        {
            Data = listElement;
        }
    }
}
