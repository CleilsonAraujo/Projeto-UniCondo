package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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

public class CadastroMoradores extends AppCompatActivity {
    private Spinner spinnerTipoMorador, spinnerCondominio;
    private EditText txtNome;
    private EditText txtNomeUsuario;
    private EditText txtEmail;
    private Button btnCadastrar;
    int tipoUsuario, idCondominio;
    private String urlWebService, sessaoToken;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_moradores);

        requestQueue = Volley.newRequestQueue(this);

        sessaoToken = getIntent().getStringExtra("TOKEN");

        txtNome = (EditText) findViewById(R.id.editText11);
        txtNomeUsuario = (EditText) findViewById(R.id.editText12);
        txtEmail = (EditText) findViewById(R.id.editText5);

        spinnerTipoMorador = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_tipo_usuario, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoMorador.setAdapter(adapter);
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

        btnCadastrar = (Button) findViewById(R.id.button2);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validado = true;

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
                if(validado){
                    //pegaIDCondominio();
                    cadastrarMorador();
                }
            }
        });
    }

    public void cadastrarMorador() {
        Map<String, String> params = new HashMap<>();
        params.put("name", txtNome.getText().toString());
        params.put("username", txtNomeUsuario.getText().toString());
        params.put("email", txtEmail.getText().toString());
        params.put("condominium_id", String.valueOf(idCondominio));

        if (tipoUsuario == 1) {
            urlWebService = "https://api-unicondo.leonardo-bezerra.dev/condominiumium-tenants";
        } else if (tipoUsuario == 2) {
            urlWebService = "https://api-unicondo.leonardo-bezerra.dev/condominium-managers";
        }

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
}
