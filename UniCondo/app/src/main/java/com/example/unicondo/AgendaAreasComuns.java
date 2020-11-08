package com.example.unicondo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaAreasComuns extends AppCompatActivity {
    private Spinner spinnerCondominio, spinnerAreasComuns;
    private TextView lblData, lblHoraInicio, lblHoraFim;
    private Button btnAgendar;
    private ImageView imgArea;
    int idCondominio, idArea;
    String urlWebService, sessaoToken, imageUrl;
    String data = "", horaInicio = "", horaFim = "";

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_areas_comuns);

        requestQueue = Volley.newRequestQueue(this);

        sessaoToken = getIntent().getStringExtra("TOKEN");

        imgArea = (ImageView) findViewById(R.id.imageView2);
        /*String imageUrl = "https://unicondo.s3.sa-east-1.amazonaws.com/images/5f9df965ea56d.jpg?" +
                "X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=" +
                "AKIAYBC22B6B7KNPDQUL%2F20201031%2Fsa-east-1%2Fs3%2Faws4_request&X-Amz-Date=" +
                "20201031T235632Z&X-Amz-SignedHeaders=host&X-Amz-Expires=259200&X-Amz-Signature=" +
                "c5730ea430a0d17af2f536826af2e35025100d13d5b3ea814497689f6bedddd6";
        try {

            Picasso.with(this).load(imageUrl).into(imgArea);

        } catch (Exception e){
            Log.e("LogImagem", e.getMessage());
        }*/

        lblData = (TextView) findViewById(R.id.textView4);
        lblData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AgendaAreasComuns.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                                mes = mes+1;
                                Log.d("ConfData", "Data informada: " + dia + "/" + mes + "/" + ano);

                                data = dia + "/" + mes + "/" + ano;
                                lblData.setText("Data selecionada: " + data);
                                lblData.setError(null);
                            }
                        }, ano, mes, dia);
                dialog.show();
            }
        });

        lblHoraInicio = (TextView) findViewById(R.id.textView12);
        lblHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hora = cal.get(Calendar.HOUR);
                int min = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(AgendaAreasComuns.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hora, int min) {
                                Log.d("ConfHora", "Hora informada: " + hora + ":" + min);

                                horaInicio = hora + ":" + min;
                                lblHoraInicio.setText("Hora de Início: " + horaInicio);
                                lblHoraInicio.setError(null);
                            }
                        }, hora, min, true);
                dialog.show();
            }
        });

        lblHoraFim = (TextView) findViewById(R.id.textView13);
        lblHoraFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hora = cal.get(Calendar.HOUR);
                int min = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(AgendaAreasComuns.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hora, int min) {
                        Log.d("ConfHora", "Hora informada: " + hora + ":" + min);

                        horaFim = hora + ":" + min;
                        lblHoraFim.setText("Hora de Término: " + horaFim);
                        lblHoraFim.setError(null);
                    }
                }, hora, min, true);
                dialog.show();
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
                pegarAreasComuns();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerAreasComuns = (Spinner) findViewById(R.id.spinner2);
        spinnerAreasComuns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                Classe_AreasComuns areas = (Classe_AreasComuns) adapterView.getSelectedItem();
                //Toast.makeText(getApplicationContext(), "nome do cond: "+cond.getNome()+"\nid do cond: "+cond.getId(), Toast.LENGTH_LONG).show();
                idArea = areas.getId();
                imageUrl = areas.getImgURL();
                Picasso.with(getApplicationContext()).load(imageUrl).into(imgArea);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAgendar = (Button) findViewById(R.id.button3);
        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validado = true;

                if (data.length()==0){
                    //Toast.makeText(getApplicationContext(), "Por favor, selecione uma data", Toast.LENGTH_LONG).show();
                    lblData.setError("");
                    validado = false;
                }
                if (horaInicio.length()==0){
                    //Toast.makeText(getApplicationContext(), "Por favor, selecione uma hora de inicio", Toast.LENGTH_LONG).show();
                    lblHoraInicio.setError("");
                    validado = false;
                }
                if (horaFim.length()==0){
                    //Toast.makeText(getApplicationContext(), "Por favor, selecione uma hora de termino", Toast.LENGTH_LONG).show();
                    lblHoraFim.setError("");
                    validado = false;
                }
                if(validado){
                    String[] parts = horaInicio.split(":");
                    Calendar cal1 = Calendar.getInstance();
                    cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                    cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

                    parts = horaFim.split(":");
                    Calendar cal2 = Calendar.getInstance();
                    cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                    cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

// Add 1 day because you mean 00:16:23 the next day
                    //cal2.add(Calendar.DATE, 1);

                    if (cal1.before(cal2)) {
                        cadastrarAgendamento();
                    } else {
                        Toast.makeText(getApplicationContext(), "Hora de Inicio não pode ser maior que a Hora de Término", Toast.LENGTH_LONG).show();
                        lblHoraInicio.setError("");
                        lblHoraFim.setError("");
                    }
                }
            }
        });
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

    public void pegarAreasComuns() {
        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/common-areas";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                urlWebService, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("LogCadastro", response.toString());
                        List<Classe_AreasComuns> listaDeAreasComuns = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                if (jsonObject.getInt("condominium_id") == idCondominio){
                                    listaDeAreasComuns.add(new Classe_AreasComuns(jsonObject.getInt("id"),
                                            jsonObject.getString("name"), jsonObject.getString("image_url")));
                                }
                            }
                            ArrayAdapter<Classe_AreasComuns> adapter2 = new ArrayAdapter<Classe_AreasComuns>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, listaDeAreasComuns);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerAreasComuns.setAdapter(adapter2);
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

    public void cadastrarAgendamento() {
        Map<String, String> params = new HashMap<>();
        params.put("date_start", data + " " + horaInicio);
        params.put("date_end", data + " " + horaFim);
        params.put("common_area_id", String.valueOf(idArea));

        urlWebService = "https://api-unicondo.leonardo-bezerra.dev/common-area-schedulings";

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
}
