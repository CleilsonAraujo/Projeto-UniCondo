package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Cadastro extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerTipoLogin;
    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private EditText txtConfSenha;
    private Button btnCadastrar;
    int tipoUsuario;

    private String urlWebService = "http://192.168.0.10/unicondo/cadastraUsuarios.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        requestQueue = Volley.newRequestQueue(this);

        txtNome = (EditText) findViewById(R.id.editText11);
        txtEmail = (EditText) findViewById(R.id.editText5);
        txtSenha = (EditText) findViewById(R.id.editText4);
        txtConfSenha = (EditText) findViewById(R.id.editText2);

        spinnerTipoLogin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_tipo_login, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoLogin.setAdapter(adapter);
        spinnerTipoLogin.setOnItemSelectedListener(this);

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

    public void cadastrarUsuario() {
        stringRequest = new StringRequest(Request.Method.POST, urlWebService,
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
    }
}
