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

public class ConfiguraBoleto extends AppCompatActivity {
    private Spinner spinnerContasBancarias;
    private EditText txtValor, txtJuros, txtMulta, txtDiasVenc;
    private Button btnSalvar;
    int idContaBank;
    boolean primeiraConf = true;
    private String urlWebService, sessaoToken;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configura_boleto);

        requestQueue = Volley.newRequestQueue(this);

        sessaoToken = getIntent().getStringExtra("TOKEN");

        txtValor = (EditText) findViewById(R.id.editText11);
        txtJuros = (EditText) findViewById(R.id.editText12);
        txtMulta = (EditText) findViewById(R.id.editText5);
        txtDiasVenc = (EditText) findViewById(R.id.editText4);

        spinnerContasBancarias = (Spinner) findViewById(R.id.spinner);
        pegarContasBancarias();
        spinnerContasBancarias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String text = adapterView.getItemAtPosition(i).toString();
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                Classe_Condominios cond = (Classe_Condominios) adapterView.getSelectedItem();
                //Toast.makeText(getApplicationContext(), "nome do cond: "+cond.getNome()+"\nid do cond: "+cond.getId(), Toast.LENGTH_LONG).show();
                idContaBank = cond.getId();
                //Toast.makeText(getApplicationContext(), "id do cond: "+idCondominio, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        verificaConfiguracao();

        btnSalvar = (Button) findViewById(R.id.button2);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validado = true;

                if (txtValor.getText().length()==0){
                    txtValor.setError("Campo Valor Base é Obrigatório");
                    txtValor.requestFocus();
                    validado = false;
                }
                if (txtJuros.getText().length()==0){
                    txtJuros.setError("Campo Juros ao dia é Obrigatório");
                    txtJuros.requestFocus();
                    validado = false;
                }
                if (txtMulta.getText().length()==0){
                    txtMulta.setError("Campo Multa por Atraso é Obrigatório");
                    txtMulta.requestFocus();
                    validado = false;
                }
                if (txtDiasVenc.getText().length()==0){
                    txtDiasVenc.setError("Campo Dias até o Vencimento é Obrigatório");
                    txtDiasVenc.requestFocus();
                    validado = false;
                }
                if(validado){
                    salvarConfiguracao();
                }
            }
        });
    }

    public void salvarConfiguracao() {
        Map<String, String> params = new HashMap<>();
        params.put("value", txtValor.getText().toString());
        params.put("interest_per_day", txtJuros.getText().toString());
        params.put("price_fines", txtMulta.getText().toString());
        params.put("due_day", txtDiasVenc.getText().toString());
        params.put("bank_account_id", String.valueOf(idContaBank));

        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/bank-slips";

        int tipoRequest = Request.Method.PATCH;

        if (primeiraConf == true) {
            tipoRequest = Request.Method.POST;
        } else if (primeiraConf == false) {
            tipoRequest = Request.Method.PUT;
            urlWebService = urlWebService + "/1";
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(tipoRequest,
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

    public void pegarContasBancarias() {
        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/bank-accounts";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                urlWebService, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("LogCadastro", response.toString());
                        List<Classe_Condominios> listaDeContas = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                String nomeBanco = "";
                                switch (jsonObject.getInt("bank_id")){
                                    case 1:
                                        nomeBanco = "Banco do Brasil";
                                        break;
                                    case 2:
                                        nomeBanco = "Bradesco";
                                        break;
                                    case 3:
                                        nomeBanco = "Caixa Econômica";
                                        break;
                                    case 4:
                                        nomeBanco = "Banco Itaú-Unibanco";
                                        break;
                                    case 5:
                                        nomeBanco = "Santander";
                                        break;
                                    case 6:
                                        nomeBanco = "Banco Inter";
                                        break;
                                    case 7:
                                        nomeBanco = "NU PAGAMENTOS S.A. (Nubank)";
                                        break;
                                }
                                listaDeContas.add(new Classe_Condominios(jsonObject.getInt("number") + "-" +
                                        jsonObject.getInt("digit") + " - " + nomeBanco, jsonObject.getInt("id")));
                            }
                            ArrayAdapter<Classe_Condominios> adapter2 = new ArrayAdapter<Classe_Condominios>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, listaDeContas);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerContasBancarias.setAdapter(adapter2);
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

    public void verificaConfiguracao() {
        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/bank-slips";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                urlWebService, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("LogCadastro", response.toString());
                        try {
                            if (response.length() > 0){
                                primeiraConf = false;
                                JSONObject jsonObject = response.getJSONObject(0);
                                txtValor.setText(jsonObject.getString("value"));
                                txtJuros.setText(jsonObject.getString("interest_per_day"));
                                txtMulta.setText(jsonObject.getString("price_fines"));
                                txtDiasVenc.setText(jsonObject.getString("due_day"));
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
