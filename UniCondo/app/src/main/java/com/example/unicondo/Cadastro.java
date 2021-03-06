package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.Spinner;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {
    //private Spinner spinnerTipoLogin;
    private EditText txtNome;
    private EditText txtNomeUsuario;
    private EditText txtEmail;
    private EditText txtSenha;
    private EditText txtConfSenha;
    private Button btnCadastrar;
    //int tipoUsuario;

    private String urlWebService = "https://api-unicondo.leonardo-bezerra.dev/user";

    //StringRequest stringRequest;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        requestQueue = Volley.newRequestQueue(this);

        txtNome = (EditText) findViewById(R.id.editText11);
        txtNomeUsuario = (EditText) findViewById(R.id.editText12);
        txtEmail = (EditText) findViewById(R.id.editText5);
        txtSenha = (EditText) findViewById(R.id.editText4);
        txtConfSenha = (EditText) findViewById(R.id.editText2);

        /*spinnerTipoLogin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_tipo_login, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoLogin.setAdapter(adapter);
        spinnerTipoLogin.setOnItemSelectedListener(this);*/

        btnCadastrar = (Button) findViewById(R.id.button2);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validado = true;
                String tempSenha = txtSenha.getText().toString();
                String tempConfSenha = txtConfSenha.getText().toString();

                if (txtNome.getText().length()==0){
                    txtNome.setError("Campo Nome é Obrigatório");
                    txtNome.requestFocus();
                    validado = false;
                }
                if (txtNomeUsuario.getText().length()==0){
                    txtNomeUsuario.setError("Campo Nome de Usuário é Obrigatório");
                    txtNomeUsuario.requestFocus();
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
                    cadastrarUsuario();
                }
            }
        });
    }

    public void cadastrarUsuario() {
        /*
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlWebService,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("LogCadastro", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isErro = jsonObject.getBoolean("erro");
                            if (isErro) {
                                Toast.makeText(getApplicationContext(), "Cadastro Indisponível, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } catch (Exception e) {

                            Log.v("LogCadastro", e.getMessage());
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LogCadastro", error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("nome", txtNome.getText().toString());
                params.put("email", txtEmail.getText().toString());
                params.put("senha", txtSenha.getText().toString());
                params.put("tipo_usuario", Integer.toString(tipoUsuario));
                return params;
            }
        };
        requestQueue.add(stringRequest);
        */

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", txtNome.getText().toString());
        params.put("username", txtNomeUsuario.getText().toString());
        params.put("email", txtEmail.getText().toString());
        params.put("password", txtSenha.getText().toString());
        params.put("re_password", txtConfSenha.getText().toString());
        //params.put("tipo_usuario", Integer.toString(tipoUsuario));


        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                urlWebService, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("LogCadastro", response.toString());
                        try {
                            //JSONObject jsonObject = new JSONObject(response);
                            //boolean isErro = jsonObject.getBoolean("erro");
                            //if (isErro) {
                                //Toast.makeText(getApplicationContext(), "Cadastro Indisponível, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                            //} else {
                                //Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                                //finish();
                            //}
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
                    com.android.volley.NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        String jsonError = new String(networkResponse.data);
                        Log.v("LogCadastro", jsonError);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(jsonError);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
