package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizaMoradores extends AppCompatActivity {
    private Spinner spinnerCondominio, spinnerTipoMorador;
    private ListView lstCondominos;
    int idCondominio, tipoUsuario;
    String urlWebService, sessaoToken;

    RequestQueue requestQueue;

    //List<Classe_Moradores> listaDeMoradores = new ArrayList<>();
    Context esseContexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_moradores);

        requestQueue = Volley.newRequestQueue(this);

        sessaoToken = getIntent().getStringExtra("TOKEN");

        lstCondominos = (ListView) findViewById(R.id.listaMoradores);
        lstCondominos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i(TAG, "onItemClick: " +position);
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                Classe_Moradores user = (Classe_Moradores) lstCondominos.getItemAtPosition(position);
                user.setMarcado(!currentCheck);
            }
        });

        spinnerTipoMorador = (Spinner) findViewById(R.id.spinner2);
        spinnerTipoMorador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

                switch(text){
                    case "Condômino":
                        tipoUsuario = 1;
                        break;
                    case "Síndico":
                        tipoUsuario = 2;
                        break;
                }
                listarMoradores();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCondominio = (Spinner) findViewById(R.id.spinner3);
        pegarCondominios();
        spinnerCondominio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String text = adapterView.getItemAtPosition(i).toString();
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                Classe_Condominios cond = (Classe_Condominios) adapterView.getSelectedItem();
                //Toast.makeText(getApplicationContext(), "nome do cond: "+cond.getNome()+"\nid do cond: "+cond.getId(), Toast.LENGTH_LONG).show();
                idCondominio = cond.getId();
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(esseContexto,
                        R.array.lista_tipo_usuario, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTipoMorador.setAdapter(adapter);
                //Toast.makeText(getApplicationContext(), "id do cond: "+idCondominio, Toast.LENGTH_LONG).show();
                //listarMoradores();

                /*List<Classe_Moradores> valores = new ArrayList<>();
                for (int a = 0; a < listaDeMoradores.size(); a++){
                    valores.add(new Classe_Moradores(listaDeMoradores.get(a).getNome(),
                            listaDeMoradores.get(a).getId(), listaDeMoradores.get(a).getIdTipoMorador()));
                    Log.d("debugada monstra", listaDeMoradores.get(a).getNome() +
                            listaDeMoradores.get(a).getId() + listaDeMoradores.get(a).getIdTipoMorador());
                }
                ArrayAdapter<Classe_Moradores> adapter = new ArrayAdapter<Classe_Moradores>(
                        esseContexto, android.R.layout.simple_list_item_multiple_choice, valores);
                //adapter.notifyDataSetChanged();
                lstCondominos.setAdapter(adapter);
                //lstCondominos.requestLayout();
                //((ArrayAdapter) lstCondominos.getAdapter()).notifyDataSetChanged();
                for(int in=0;in< valores.size(); in++ )  {
                    lstCondominos.setItemChecked(in, valores.get(in).isMarcado());
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void pegarCondominios() {
        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/condominiums";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                urlWebService, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("LogCadastro", response.toString());
                        List<Classe_Condominios> listaDeCondominios = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                listaDeCondominios.add(new Classe_Condominios(jsonObject.getString("name"), jsonObject.getInt("id")));
                            }
                            ArrayAdapter<Classe_Condominios> adapter2 = new ArrayAdapter<Classe_Condominios>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, listaDeCondominios);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCondominio.setAdapter(adapter2);
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
                            Toast.makeText(getApplicationContext(), "Erro de servidor", Toast.LENGTH_LONG).show();
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

    public void listarMoradores(){

        if (tipoUsuario == 1){
            urlWebService = "https://api-unicondo.leonardo-bezerra.dev/condominiumium-tenants?condominium_id=" + idCondominio;
        } else if (tipoUsuario == 2){
            urlWebService = "https://api-unicondo.leonardo-bezerra.dev/condominium-managers?condominium_id=" + idCondominio;
        }

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                urlWebService, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("LogCadastro", response.toString());
                        List<Classe_Moradores> listaDeMoradores = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                if (jsonObject.getInt("user_status_id") != 3){
                                    listaDeMoradores.add(new Classe_Moradores(jsonObject.getString("name"),
                                            jsonObject.getInt("id"), jsonObject.getInt("role_id")));
                                }
                            }
                            /* BURRICE MINHA, ISSO AQUI TAVA DENTRO DO FOR, POR ISSO TAVA DANDO PROBLEMA
                            terminou1 = true;
                                //Thread.sleep(1000);
                                if (terminou1 == true && terminou2 == true){
                                    preencherLista();
                                }*/
                            ArrayAdapter<Classe_Moradores> adapter = new ArrayAdapter<Classe_Moradores>(
                                    esseContexto, android.R.layout.simple_list_item_1, listaDeMoradores);
                            //adapter.notifyDataSetChanged();
                            lstCondominos.setAdapter(adapter);
                            //lstCondominos.requestLayout();
                            //((ArrayAdapter) lstCondominos.getAdapter()).notifyDataSetChanged();
                            for(int in=0;in< listaDeMoradores.size(); in++ )  {
                                lstCondominos.setItemChecked(in, listaDeMoradores.get(in).isMarcado());
                            }
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
                            Toast.makeText(getApplicationContext(), "Erro de servidor", Toast.LENGTH_LONG).show();
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
/*
        urlWebService2 = "https://api-unicondo.leonardo-bezerra.dev/condominium-managers?condominium_id=" + idCondominio;

        JsonArrayRequest jsonRequest2 = new JsonArrayRequest(Request.Method.GET,
                urlWebService2, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("LogCadastro2", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                if (idCondominio == jsonObject.getJSONObject("pivot").getInt("condominium_id")){
                                    listaDeMoradores.add(new Classe_Moradores(jsonObject.getString("name"),
                                            jsonObject.getInt("id"), jsonObject.getInt("role_id")));
                                }
                            }
                                MESMA BURRICE QUE EU COMENTEI NO REQUEST DE CIMA
                                terminou2 = true;
                                if (terminou1 == true && terminou2 == true){
                                    preencherLista();
                                }
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
                            Toast.makeText(getApplicationContext(), "Erro de servidor", Toast.LENGTH_LONG).show();
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
        requestQueue.add(jsonRequest2);*/
    }
/*
    public void preencherLista(){
        if (terminou1 == true && terminou2 == true) {
            List<Classe_Moradores> valores = new ArrayList<>();
            for (int a = 0; a < listaDeMoradores.size(); a++){
                valores.add(new Classe_Moradores(listaDeMoradores.get(a).getNome(),
                        listaDeMoradores.get(a).getId(), listaDeMoradores.get(a).getIdTipoMorador()));
                Log.d("debugada monstra", listaDeMoradores.get(a).getNome() +
                        listaDeMoradores.get(a).getId() + listaDeMoradores.get(a).getIdTipoMorador());
            }
            ArrayAdapter<Classe_Moradores> adapter = new ArrayAdapter<Classe_Moradores>(
                    esseContexto, android.R.layout.simple_list_item_multiple_choice, valores);
            //adapter.notifyDataSetChanged();
            lstCondominos.setAdapter(adapter);
            //lstCondominos.requestLayout();
            //((ArrayAdapter) lstCondominos.getAdapter()).notifyDataSetChanged();
            for(int in=0;in< valores.size(); in++ )  {
                lstCondominos.setItemChecked(in, valores.get(in).isMarcado());
            }
            terminou1 = false;
            terminou2 = false;
            listaDeMoradores = new ArrayList<>();
        }
    }
 */
}