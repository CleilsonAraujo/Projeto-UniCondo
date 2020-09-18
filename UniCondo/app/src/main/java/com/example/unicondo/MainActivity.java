package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerTipoLogin;
    private Button btnLogar;
    private TextView txtNovaSenha;
    private TextView txtCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerTipoLogin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_tipo_login, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoLogin.setAdapter(adapter);

        btnLogar = (Button) findViewById(R.id.button2);
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirHome();
            }
        });

        txtNovaSenha = (TextView) findViewById(R.id.textView8);
        txtNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRecuperarSenha();
            }
        });

        txtCadastro = (TextView) findViewById(R.id.textView9);
        txtCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastro();
            }
        });
    }

    public void abrirHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void abrirRecuperarSenha(){
        Intent intent = new Intent(this, RecuperarSenha.class);
        startActivity(intent);
    }

    public void abrirCadastro(){
        Intent intent = new Intent(this, Cadastro.class);
        startActivity(intent);
    }
}
