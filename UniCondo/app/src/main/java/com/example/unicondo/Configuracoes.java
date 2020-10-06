package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.MatrixCursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Configuracoes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        //ignore os comentarios abaixo sobre banco e tals pq foi um ctrl c ctrl v de uma outra aplicação minha
        ListView opcoes = (ListView) findViewById(R.id.configuracoes);
        //adiciona os campos da tabela do banco
        String[] camposTabela = {"_id", "titulo"};
        //adiciona os campos do modelo do listview
        int[] camposModelo = {R.id.campoidConf, R.id.campoTituloConf};

        MatrixCursor matrixCursor= new MatrixCursor(camposTabela);
        startManagingCursor(matrixCursor);

        matrixCursor.addRow(new Object[] {1, "Alterar..."});
        matrixCursor.addRow(new Object[] {2, "Alterar..."});
        matrixCursor.addRow(new Object[] {3, "Alterar..."});

        SimpleCursorAdapter ad = new SimpleCursorAdapter(getApplicationContext(), R.layout.modelo_tela_configuracoes, matrixCursor, camposTabela, camposModelo);
        //habilita a opção de clicar nos contatos da agenda
        opcoes.setAdapter(ad);
    }
}
