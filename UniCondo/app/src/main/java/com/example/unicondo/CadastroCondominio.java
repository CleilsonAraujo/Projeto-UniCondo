package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class CadastroCondominio extends AppCompatActivity {
    private EditText txtNome;
    private EditText txtCNPJ;
    private EditText txtCEP;
    private EditText txtEndereco;
    private String urlWebService, sessaoToken;
    private Button btnCadastrar;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_condominio);

        sessaoToken = getIntent().getStringExtra("TOKEN");
        //Toast.makeText(getApplicationContext(), sessaoToken, Toast.LENGTH_LONG).show();

        requestQueue = Volley.newRequestQueue(this);

        txtNome = (EditText) findViewById(R.id.editText11);
        txtCNPJ = (EditText) findViewById(R.id.editText12);
        txtCEP = (EditText) findViewById(R.id.editText5);
        txtEndereco = (EditText) findViewById(R.id.editText4);

        txtCEP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && txtCEP.getText().length()!=0) {
                    verificarCEP();
                }
            }
        });

        btnCadastrar = (Button) findViewById(R.id.button2);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validado = true;

                if (txtNome.getText().length()==0){
                    txtNome.setError("Campo Nome do condomínio é Obrigatório");
                    txtNome.requestFocus();
                    validado = false;
                }
                if (txtCNPJ.getText().length()==0){
                    txtCNPJ.setError("Campo CNPJ é Obrigatório");
                    txtCNPJ.requestFocus();
                    validado = false;
                } else if (!validaCNPJ(txtCNPJ.getText().toString())){
                    txtCNPJ.setError("CNPJ informado não é válido");
                    txtCNPJ.requestFocus();
                    validado = false;
                }
                if (txtCEP.getText().length()==0){
                    txtCEP.setError("Campo CEP é Obrigatório");
                    txtCEP.requestFocus();
                    validado = false;
                }
                if (txtEndereco.getText().length()==0){
                    txtEndereco.setError("Campo endereço é Obrigatório");
                    txtEndereco.requestFocus();
                    validado = false;
                }
                if(validado){
                    cadastrarCondominio();
                }
            }
        });
    }

    public boolean validaCNPJ(String CNPJ){
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
                CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
                CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
                CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
                CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
                (CNPJ.length() != 14))
            return(false);

        char dig13, dig14;
        int sm, i, r, num, peso;

// "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
// Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i=11; i>=0; i--) {
// converte o i-ésimo caractere do CNPJ em um número:
// por exemplo, transforma o caractere '0' no inteiro 0
// (48 eh a posição de '0' na tabela ASCII)
                num = (int)(CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else dig13 = (char)((11-r) + 48);

// Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i=12; i>=0; i--) {
                num = (int)(CNPJ.charAt(i)- 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else dig14 = (char)((11-r) + 48);

// Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }

    public void cadastrarCondominio() {
        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/condominiums";

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", txtNome.getText().toString());
        params.put("cnpj", txtCNPJ.getText().toString());
        params.put("cep", txtCEP.getText().toString());
        params.put("address", txtEndereco.getText().toString());

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
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + sessaoToken);
                return headers;
            }
        };
        requestQueue.add(jsonRequest);
    }

    public void verificarCEP() {
        urlWebService = "https://viacep.com.br/ws/" + txtCEP.getText().toString() + "/json/";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                urlWebService, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("LogCadastro", response.toString());
                        try {
                            String endereco = response.getString("logradouro") + ", " + response.getString("bairro") + ", " + response.getString("localidade") + ", " + response.getString("uf");
                            txtEndereco.setText(endereco);
                            //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
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
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonRequest);
    }
}
