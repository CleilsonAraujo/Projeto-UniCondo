package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecuperarSenha extends AppCompatActivity {
    private EditText txtEmail, txtCodigo, txtSenha, txtConfSenha;
    private TextView lblCodigo, lblSenha, lblConfSenha;
    private Button btnEnviaCodigo, btnAlteraSenha;
    private String urlWebService;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        requestQueue = Volley.newRequestQueue(this);

        txtEmail = (EditText) findViewById(R.id.editText);
        txtCodigo = (EditText) findViewById(R.id.editText2);
        txtSenha = (EditText) findViewById(R.id.editText3);
        txtConfSenha = (EditText) findViewById(R.id.editText4);
        lblCodigo = (TextView) findViewById(R.id.textView2);
        lblSenha = (TextView) findViewById(R.id.textView3);
        lblConfSenha = (TextView) findViewById(R.id.textView4);

        btnEnviaCodigo = (Button) findViewById(R.id.button);
        btnEnviaCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validado = true;

                if (txtEmail.getText().length()==0){
                    txtEmail.setError("Campo E-mail é Obrigatório");
                    txtEmail.requestFocus();
                    validado = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString().trim()).matches()){
                    txtEmail.setError("E-mail informado não é válido");
                    txtEmail.requestFocus();
                    validado = false;
                }

                if(validado){
                    enviarCodigo();
                }
            }
        });

        btnAlteraSenha = (Button) findViewById(R.id.button2);
        btnAlteraSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validado = true;
                String tempSenha = txtSenha.getText().toString();
                String tempConfSenha = txtConfSenha.getText().toString();

                if (txtCodigo.getText().length()==0){
                    txtCodigo.setError("Campo Código é Obrigatório");
                    txtCodigo.requestFocus();
                    validado = false;
                }
                if (txtEmail.getText().length()==0){
                    txtEmail.setError("Campo E-mail é Obrigatório");
                    txtEmail.requestFocus();
                    validado = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString().trim()).matches()){
                    txtEmail.setError("E-mail informado não é válido");
                    txtEmail.requestFocus();
                    validado = false;
                }
                if (txtSenha.getText().length()==0){
                    txtSenha.setError("Campo Senha é Obrigatório");
                    txtSenha.requestFocus();
                    validado = false;
                } else if (!tempSenha.equals(tempConfSenha)){
                    txtConfSenha.setError("Campo Confirmar Senha deve ser igual a Senha");
                    txtConfSenha.requestFocus();
                    validado = false;
                }
                tempSenha = "";
                tempConfSenha = "";
                if(validado){
                    alterarSenha();
                }
            }
        });
    }

    public void enviarCodigo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", txtEmail.getText().toString());

        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/password/recovery";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                urlWebService, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("LogLogin", response.toString());
                        try {
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                            //finish();
                            txtCodigo.setVisibility(View.VISIBLE);
                            txtSenha.setVisibility(View.VISIBLE);
                            txtConfSenha.setVisibility(View.VISIBLE);
                            lblCodigo.setVisibility(View.VISIBLE);
                            lblSenha.setVisibility(View.VISIBLE);
                            lblConfSenha.setVisibility(View.VISIBLE);
                            btnEnviaCodigo.setVisibility(View.GONE);
                            btnAlteraSenha.setVisibility(View.VISIBLE);
                        } catch (Exception e) {

                            Log.v("LogLogin", e.getMessage());
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
                        //Log.e("LogLogin", error.getLocalizedMessage()); //não ta funcionando esse log na API
                    }
                }) {
/*
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }*/
        };
        requestQueue.add(jsonRequest);
    }

    public void alterarSenha() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", txtEmail.getText().toString());
        params.put("code", txtCodigo.getText().toString());
        params.put("password", txtSenha.getText().toString());
        params.put("re_password", txtConfSenha.getText().toString());

        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/password/reset";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                urlWebService, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("LogLogin", response.toString());
                        try {
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                            finish();
                        } catch (Exception e) {

                            Log.v("LogLogin", e.getMessage());
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
                        //Log.e("LogLogin", error.getLocalizedMessage()); //não ta funcionando esse log na API
                    }
                }) {
/*
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }*/
        };
        requestQueue.add(jsonRequest);
    }
}
