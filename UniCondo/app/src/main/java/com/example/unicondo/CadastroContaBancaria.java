package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastroContaBancaria extends AppCompatActivity {
    private Spinner spinnerBanco, spinnerCondominio;
    private EditText txtNumConta, txtNumAgencia;
    private Button btnSalvar;
    int idBanco, idCondominio;
    private String urlWebService, sessaoToken;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_conta_bancaria);

        requestQueue = Volley.newRequestQueue(this);

        sessaoToken = getIntent().getStringExtra("TOKEN");

        txtNumConta = (EditText) findViewById(R.id.editText11);
        txtNumAgencia = (EditText) findViewById(R.id.editText12);

        spinnerBanco = (Spinner) findViewById(R.id.spinner);
        pegarBancos();
        spinnerBanco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String text = adapterView.getItemAtPosition(i).toString();
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                Classe_Condominios banco = (Classe_Condominios) adapterView.getSelectedItem();
                //Toast.makeText(getApplicationContext(), "nome do cond: "+cond.getNome()+"\nid do cond: "+cond.getId(), Toast.LENGTH_LONG).show();
                idBanco = banco.getId();
                //Toast.makeText(getApplicationContext(), "id do cond: "+idCondominio, Toast.LENGTH_LONG).show();
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
                //Toast.makeText(getApplicationContext(), "id do cond: "+idCondominio, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSalvar = (Button) findViewById(R.id.button2);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validado = true;

                //Log.v("Verificação", txtNumConta.getText().toString().substring(0, txtNumConta.getText().toString().length() - 1));
                //Log.v("Verificação", txtNumConta.getText().toString().substring(txtNumConta.getText().toString().length() - 1));

                if (txtNumConta.getText().length()==0){
                    txtNumConta.setError("Campo Numero da Conta é Obrigatório");
                    txtNumConta.requestFocus();
                    validado = false;
                }
                if (txtNumAgencia.getText().length()==0){
                    txtNumAgencia.setError("Campo Agência é Obrigatório");
                    txtNumAgencia.requestFocus();
                    validado = false;
                }
                if(validado){
                    salvarContaBancaria();
                }
            }
        });
    }

    public void salvarContaBancaria() {
        Map<String, String> params = new HashMap<>();
        params.put("number", txtNumConta.getText().toString().substring(0, txtNumConta.getText().toString().length() - 1));
        params.put("digit", txtNumConta.getText().toString().substring(txtNumConta.getText().toString().length() - 1));
        params.put("agency", txtNumAgencia.getText().toString().substring(0, txtNumAgencia.getText().toString().length() - 1));
        params.put("agency_digit", txtNumAgencia.getText().toString().substring(txtNumAgencia.getText().toString().length() - 1));
        params.put("bank_id", String.valueOf(idBanco));
        params.put("condominium_id", String.valueOf(idCondominio));

        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/bank-accounts";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                urlWebService, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("LogCadastro", response.toString());
                        try {
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                            finish();
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

    public void pegarBancos() {
        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/banks";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                urlWebService, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("LogCadastro", response.toString());
                        List<Classe_Condominios> listaDeBancos = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                listaDeBancos.add(new Classe_Condominios(jsonObject.getString("name"), jsonObject.getInt("id")));
                            }
                            ArrayAdapter<Classe_Condominios> adapter3 = new ArrayAdapter<Classe_Condominios>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, listaDeBancos);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerBanco.setAdapter(adapter3);
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
}
