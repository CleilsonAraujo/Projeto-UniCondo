package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private String sessaoToken, urlWebService;
    private int sessaoTipoUsuario, idUsuario;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(this);

        //tipoUsuario = getIntent().getIntExtra("TIPO_USUARIO", 0);
        sessaoTipoUsuario = getIntent().getIntExtra("TIPO_USUARIO", 0);
        sessaoToken = getIntent().getStringExtra("TOKEN");
        //Toast.makeText(getApplicationContext(), sessaoToken, Toast.LENGTH_LONG).show();
/*
        ArrayList<Classe_Noticias> noticias = new ArrayList();
        noticias.add(new Classe_Noticias("1", "Notícia 1", "12/12/2020", "Apenas um texto de uma noticia para testar a funcionalidade"));
        noticias.add(new Classe_Noticias("2", "Notícia 2", "12/12/2020", "Apenas um texto de uma noticia para testar a funcionalidade"));
        */
        //Aqui é instanciado o Recyclerview
        pegarNoticias();

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (sessaoTipoUsuario == 1) {
            inflater.inflate(R.menu.menu_home, menu);
        } else if (sessaoTipoUsuario == 2) {
            inflater.inflate(R.menu.menu_home2, menu);
        } else if (sessaoTipoUsuario == 3) {
            inflater.inflate(R.menu.menu_home3, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                abrirCadastroAreaComum();
                return true;
            case R.id.help2:
                abrirCadastroNoticias();
                return true;
            case R.id.help3:
                abrirAreasComuns();
                return true;
            /*case R.id.help4:
                abrirConfiguracoes();
                return true;*/
            case R.id.help5:
                finish();
                return true;
            case R.id.help6:
                abrirCadastroCondominio();
                return true;
            case R.id.help7:
                abrirCadastroMoradores();
                return true;
            case R.id.help8:
                abrirGerenciaMoradores();
                return true;
            case R.id.help9:
                abrirGerenciaAgendamentos();
                return true;
            case R.id.help10:
                abrirCadastroContaBancaria();
                return true;
            case R.id.help11:
                abrirConfiguraBoleto();
                return true;
            case R.id.help12:
                abrirGerarBoleto();
                return true;
            case R.id.help13:
                abrirStatusPagamentos();
                return true;
            /*case R.id.help14:
                abrirAgendaVisita();
                return true;
            case R.id.help15:
                abrirGerarBoleto();
                return true;*/
            case R.id.help16:
                abrirVisualizaStatusPagamento();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirCadastroAreaComum(){
        Intent intent = new Intent(this, CadastroAreaComum.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirCadastroNoticias(){
        Intent intent = new Intent(this, CadastroNoticias.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirCadastroCondominio(){
        Intent intent = new Intent(this, CadastroCondominio.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirCadastroMoradores(){
        Intent intent = new Intent(this, CadastroMoradores.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirCadastroContaBancaria(){
        Intent intent = new Intent(this, CadastroContaBancaria.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirConfiguraBoleto(){
        Intent intent = new Intent(this, ConfiguraBoleto.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirGerarBoleto(){
        Intent intent = new Intent(this, GerarBoleto.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirVisualizaStatusPagamento(){
        Intent intent = new Intent(this, VisualizaStatusPagamento.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirAgendaVisita(){
        Intent intent = new Intent(this, AgendaVisita.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirStatusPagamentos(){
        Intent intent;
        if (sessaoTipoUsuario == 3){
            intent = new Intent(this, VisualizaMoradores.class);
        } else {
            intent = new Intent(this, StatusPagamentos.class);
        }
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirGerenciaMoradores(){
        Intent intent;
        if (sessaoTipoUsuario == 3){
            intent = new Intent(this, VisualizaMoradores.class);
        } else {
            intent = new Intent(this, GerenciaMoradores.class);
        }
        intent.putExtra("TOKEN", sessaoToken);
        intent.putExtra("TIPO_USUARIO", sessaoTipoUsuario);
        startActivity(intent);
    }

    public void abrirAreasComuns(){
        Intent intent = new Intent(this, AgendaAreasComuns.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    public void abrirGerenciaAgendamentos(){
        Intent intent = new Intent(this, GerenciaAgendamentos.class);
        intent.putExtra("TOKEN", sessaoToken);
        startActivity(intent);
    }

    /*public void abrirConfiguracoes(){
        Intent intent = new Intent(this, Configuracoes.class);
        startActivity(intent);
    }*/

    public void pegarNoticias() {
        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/news";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                urlWebService, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("LogCadastro", response.toString());
                        ArrayList<Classe_Noticias> noticias = new ArrayList();
                        SimpleDateFormat sdfString = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                            mRecyclerView.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);

                                String[] data = jsonObject.getString("created_at").split(" ");
                                Date frmtData = null;
                                frmtData = sdfDate.parse(data[0]);

                                noticias.add(new Classe_Noticias(String.valueOf(jsonObject.getInt("id")), jsonObject.getString("title"),
                                        sdfString.format(frmtData) + " " + data[1], jsonObject.getString("description")));
                            }

                            mAdapter = new Classe_RecyclerViewAdapter(noticias);
                            mRecyclerView.setAdapter(mAdapter);
                            //Toast.makeText(getApplicationContext(), Integer.toString(idCondominio), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                            Log.v("LogCadastro", e.getMessage());
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if( error instanceof NetworkError) {
                            //handle your network error here.
                            Toast.makeText(getApplicationContext(), "Erro de conexão de internet", Toast.LENGTH_LONG).show();
                        } else if( error instanceof ServerError) {
                            //handle if server error occurs with 5** status code
                            com.android.volley.NetworkResponse networkResponse = error.networkResponse;
                            //if (networkResponse != null && networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            Log.v("LogCadastro", jsonError);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(jsonError);
                                Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if( error instanceof AuthFailureError) {
                            //handle if authFailure occurs.This is generally because of invalid credentials
                            Toast.makeText(getApplicationContext(), "Erro de autenticação", Toast.LENGTH_LONG).show();
                        } else if( error instanceof ParseError) {
                            //handle if the volley is unable to parse the response data.
                            Toast.makeText(getApplicationContext(), "Erro de Parse", Toast.LENGTH_LONG).show();
                        } else if( error instanceof NoConnectionError) {
                            //handle if no connection is occurred
                            Toast.makeText(getApplicationContext(), "Erro de conexão API", Toast.LENGTH_LONG).show();
                        } else if( error instanceof TimeoutError) {
                            //handle if socket time out is occurred.
                            Toast.makeText(getApplicationContext(), "Erro de timeout", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + sessaoToken);
                return headers;
            }
        };
        requestQueue.add(jsonRequest);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        recreate();
    }
}
