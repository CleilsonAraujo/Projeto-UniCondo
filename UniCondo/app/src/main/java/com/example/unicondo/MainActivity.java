package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerTipoLogin;
    private Button btnLogar;
    private TextView lblNovaSenha;
    private TextView lblCadastro;
    private EditText txtEmail;
    private EditText txtSenha;
    int tipoUsuario;

    private String urlWebService = "http://192.168.0.10/unicondo/getUsuarios.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        spinnerTipoLogin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                        R.array.lista_tipo_login, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoLogin.setAdapter(adapter);
        spinnerTipoLogin.setOnItemSelectedListener(this);

        txtEmail = (EditText) findViewById(R.id.editText5);
        txtSenha = (EditText) findViewById(R.id.editText4);

        btnLogar = (Button) findViewById(R.id.button2);
        btnLogar.setOnClickListener(new View.OnClickListener() {
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
        intent.putExtra("SESSION_ID", tipoUsuario);
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
        stringRequest = new StringRequest(Request.Method.POST, urlWebService,
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
        requestQueue.add(stringRequest);
    }

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
            case "Administrador":
                tipoUsuario = 3;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
