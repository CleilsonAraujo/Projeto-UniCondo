package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        //ignore os comentarios abaixo sobre banco e tals pq foi um ctrl c ctrl v de uma outra aplicação minha
        ListView noticias = (ListView) findViewById(R.id.ListView);
        //adiciona os campos da tabela do banco
        String[] camposTabela = {"_id", "titulo", "data", "texto"};
        //adiciona os campos do modelo do listview
        int[] camposModelo = {R.id.campoid, R.id.campoTitulo, R.id.campoData, R.id.campoTexto};

        MatrixCursor matrixCursor= new MatrixCursor(camposTabela);
        startManagingCursor(matrixCursor);

        matrixCursor.addRow(new Object[] {1, "Notícia 1", "12/12/2020", "Apenas uma noticia ordinária para testar a veracidade funcional de tal aplicação" });

        SimpleCursorAdapter ad = new SimpleCursorAdapter(getApplicationContext(), R.layout.modelo_tela_noticias, matrixCursor, camposTabela, camposModelo);
        //habilita a opção de clicar nos contatos da agenda
        noticias.setAdapter(ad);
*/

        ArrayList<Noticias_Classe> noticias = new ArrayList();
        noticias.add(new Noticias_Classe("1", "Notícia 1", "12/12/2020", "Apenas uma noticia ordinária para testar a veracidade funcional de tal aplicação"));
        noticias.add(new Noticias_Classe("2", "Notícia 2", "12/12/2020", "Apenas uma noticia ordinária para testar a veracidade funcional de tal aplicação"));






        //Aqui é instanciado o Recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(noticias);
        mRecyclerView.setAdapter(mAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                abrirCadastroAcomodacao();
                return true;
            case R.id.help2:
                abrirCadastroNoticias();
                return true;
            case R.id.help3:
                abrirAreasComuns();
                return true;
            case R.id.help4:
                abrirConfiguracoes();
                return true;
            case R.id.help5:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirCadastroAcomodacao(){
        Intent intent = new Intent(this, CadastroAreaComum.class);
        startActivity(intent);
    }

    public void abrirCadastroNoticias(){
        Intent intent = new Intent(this, CadastroNoticias.class);
        startActivity(intent);
    }

    public void abrirAreasComuns(){
        Intent intent = new Intent(this, AgendaAreasComuns.class);
        startActivity(intent);
    }

    public void abrirConfiguracoes(){
        Intent intent = new Intent(this, Configuracoes.class);
        startActivity(intent);
    }
}
