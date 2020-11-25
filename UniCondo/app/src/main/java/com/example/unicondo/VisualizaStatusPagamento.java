package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizaStatusPagamento extends AppCompatActivity {
    private EditText txtCodigo;
    private Button btnCopiar;
    private ListView lstBoletos;
    private String urlWebService, sessaoToken;

    RequestQueue requestQueue;

    Context esseContexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_status_pagamento);

        requestQueue = Volley.newRequestQueue(this);

        sessaoToken = getIntent().getStringExtra("TOKEN");

        txtCodigo = (EditText) findViewById(R.id.editText11);
        txtCodigo.setFocusable(false);
        txtCodigo.setEnabled(false);
        txtCodigo.setCursorVisible(false);
        txtCodigo.setKeyListener(null);
        //editText.setBackgroundColor(Color.TRANSPARENT);

        lstBoletos = (ListView) findViewById(R.id.listaBoletos);
        listarBoletos();
        lstBoletos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i(TAG, "onItemClick: " +position);
                Classe_VisualizaStatusPagamento user = (Classe_VisualizaStatusPagamento) lstBoletos.getItemAtPosition(position);
                txtCodigo.setText(user.getCodigo());

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", txtCodigo.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Código copiado para a área de transferência", Toast.LENGTH_LONG).show();
            }
        });

        btnCopiar = (Button) findViewById(R.id.button2);
        btnCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", txtCodigo.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Código copiado para a área de transferência", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void listarBoletos() {
        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/payments";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                urlWebService, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("LogCadastro", response.toString());
                        List<Classe_VisualizaStatusPagamento> listaDeBoletos = new ArrayList<>();
                        SimpleDateFormat sdfString = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);

                                Date frmtData = null;
                                frmtData = sdfDate.parse(jsonObject.getString("due_date"));

                                listaDeBoletos.add(new Classe_VisualizaStatusPagamento(jsonObject.getInt("id"),
                                        jsonObject.getJSONObject("status").getString("name") + "\n" +
                                                "Data de vencimento: " + sdfString.format(frmtData), jsonObject.getString("code")));
                            }
                            ArrayAdapter<Classe_VisualizaStatusPagamento> adapter = new ArrayAdapter<Classe_VisualizaStatusPagamento>(
                                    esseContexto, android.R.layout.simple_list_item_1, listaDeBoletos);
                            //adapter.notifyDataSetChanged();
                            lstBoletos.setAdapter(adapter);
                            /*for(int in=0;in< listaDeMoradores.size(); in++ )  {
                                lstCondominos.setItemChecked(in, listaDeMoradores.get(in).isMarcado());
                            }*/
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
    }
}
