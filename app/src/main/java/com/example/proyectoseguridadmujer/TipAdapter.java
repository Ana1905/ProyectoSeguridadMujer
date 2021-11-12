package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import Dialogs.DialogShowTip;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder>{

    Context context;
    ArrayList<Tip> mListaTips;

    public TipAdapter(Context context, ArrayList<Tip> arrayList){
        this.context = context;
        this.mListaTips = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_tip, parent, false);
        return new TipAdapter.TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TipViewHolder holder, int position) {

        holder.mTextViewTitulo.setText(mListaTips.get(position).getTitulo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((TestResultsActivity)context).getSupportFragmentManager();
                DialogShowTip dialogShowTip = new DialogShowTip();

                Bundle arguments = new Bundle();
                arguments.putInt("ID_Tip", mListaTips.get(position).getID_Tip());
                arguments.putString("Titulo", mListaTips.get(position).getTitulo());
                arguments.putString("Contenido", mListaTips.get(position).getContenido());
                arguments.putInt("Tipo", mListaTips.get(position).getTipo());

                dialogShowTip.setArguments(arguments);
                //dialogNewHelpPostFragment.show(((MainActivity)context).getSupportFragmentManager(), "tag");

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(android.R.id.content, dialogShowTip).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListaTips.size();
    }

    public class TipViewHolder extends RecyclerView.ViewHolder{

        TextView mTextViewTitulo;

        public TipViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mTextViewTitulo = itemView.findViewById(R.id.TituloTipLista);
        }
    }
}
