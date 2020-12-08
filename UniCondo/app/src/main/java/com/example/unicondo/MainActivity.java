package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
//import android.util.Patterns;
import android.view.View;
//import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.Spinner;
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
//import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //private Spinner spinnerTipoLogin;
    private Button btnLogar;
    private TextView lblNovaSenha;
    private TextView lblCadastro;
    private EditText txtEmail;
    private EditText txtSenha;
    private String token, tipoToken, nome, nomeDeUsuario, email;
    private int tempoToken, tipoUsuario;

    private String urlWebService = "https://api-unicondo.leonardo-bezerra.dev/auth/login";

    //StringRequest stringRequest;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        /*spinnerTipoLogin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                        R.array.lista_tipo_login, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoLogin.setAdapter(adapter);*/

        txtEmail = (EditText) findViewById(R.id.editText5);
        txtSenha = (EditText) findViewById(R.id.editText4);

        btnLogar = (Button) findViewById(R.id.button2);
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validado = true;

                if (txtEmail.getText().length()==0){
                    txtEmail.setError("Campo Nome de Usuário é Obrigatório");
                    txtEmail.requestFocus();
                    validado = false;
                } /*else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString().trim()).matches()){
                    txtEmail.setError("E-mail informado não é válido");
                    txtEmail.requestFocus();
                    validado = false;
                }*/
                if (txtSenha.getText().length()==0){
                    txtSenha.setError("Campo Senha é Obrigatório");
                    txtSenha.requestFocus();
                    validado = false;
                }
                if(validado){
                    validarLogin();
                }
            }
        });

        lblNovaSenha = (TextView) findViewById(R.id.textView8);
        lblNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRecuperarSenha();
            }
        });

        lblCadastro = (TextView) findViewById(R.id.textView9);
        lblCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastro();
            }
        });
    }

    public void abrirHome(){
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("TOKEN", token);
        intent.putExtra("TEMPO_TOKEN", tempoToken);
        intent.putExtra("TIPO_TOKEN", tipoToken);
        intent.putExtra("NOME", nome);
        intent.putExtra("NOME_DE_USUARIO", nomeDeUsuario);
        intent.putExtra("EMAIL", email);
        intent.putExtra("TIPO_USUARIO", tipoUsuario);
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

    public void validarLogin() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", txtEmail.getText().toString());
        params.put("password", txtSenha.getText().toString());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                urlWebService, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("LogLogin", response.toString());
                        try {
                            //JSONObject jsonObject = new JSONObject(response);
                            //boolean isErro = jsonObject.getBoolean("erro");
                            //if (isErro) {
                            //Toast.makeText(getApplicationContext(), "Cadastro Indisponível, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                            //} else {
                            //Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                            //finish();
                            //}
                            //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                            token = response.getString("access_token");
                            tempoToken = response.getInt("expires_in");
                            tipoToken = response.getString("token_type");
                            nome = response.getJSONObject("user").getString("name");
                            nomeDeUsuario = response.getJSONObject("user").getString("username");
                            email = response.getJSONObject("user").getString("email");
                            tipoUsuario = response.getJSONObject("user").getJSONObject("role").getInt("id");
                            /*Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), nomeDeUsuario, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), Integer.toString(tipoUsuario), Toast.LENGTH_LONG).show();*/
                            abrirHome();
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

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonRequest);

        /*stringRequest = new StringRequest(Request.Method.POST, urlWebService,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.v("LogLogin", response);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean isErro = jsonObject.getBoolean("erro");
                                            if (isErro) {
                                                Toast.makeText(getApplicationContext(), "Usuário não cadastrado!", Toast.LENGTH_LONG).show();
                                            } else {
                                                abrirHome();
                                            }
                                        } catch (Exception e) {

                                            Log.v("LogLogin", e.getMessage());
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("LogLogin", error.getMessage());
                                    }
                                }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", txtEmail.getText().toString());
                params.put("senha", txtSenha.getText().toString());
                params.put("tipo_usuario", Integer.toString(tipoUsuario));
                return params;
            }
        };
        requestQueue.add(stringRequest);*/
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        txtEmail.setText("");
        txtSenha.setText("");
    }
}
