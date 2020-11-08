package com.example.unicondo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Classe_RecyclerViewAdapter extends RecyclerView.Adapter<Classe_RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Classe_Noticias> mNoticias;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mID;
        public TextView mTitulo;
        public TextView mData;
        public TextView mTexto;

        public ViewHolder(View itemView) {
            super(itemView);
            mID = itemView.findViewById(R.id.campoid);
            mTitulo = itemView.findViewById(R.id.campoTitulo);
            mData = itemView.findViewById(R.id.campoData);
            mTexto = itemView.findViewById(R.id.campoTexto);
        }
    }

    public Classe_RecyclerViewAdapter(ArrayList<Classe_Noticias> noticias){
        mNoticias = noticias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_tela_noticias, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Classe_Noticias cursor = mNoticias.get(position);

        holder.mID.setText(cursor.getID());
        holder.mTitulo.setText(cursor.getTitulo());
        holder.mData.setText(cursor.getData());
        holder.mTexto.setText(cursor.getTexto());
    }

    @Override
    public int getItemCount() {
        return mNoticias.size();
    }
}