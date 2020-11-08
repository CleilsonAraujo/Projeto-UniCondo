package com.example.unicondo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {
    private Context context;
    String token, BASE_URL, nomeArea, descArea, condID;
    File imgArea;

    public RetrofitRequest(Context context, String token, String BASE_URL, String nomeArea, String descArea, String condID, File imgArea) {
        this.context = context;
        this.token = token;
        this.BASE_URL = BASE_URL;
        this.nomeArea = nomeArea;
        this.descArea = descArea;
        this.condID = condID;
        this.imgArea = imgArea;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public String getNomeArea() {
        return nomeArea;
    }

    public void setNomeArea(String nomeArea) {
        this.nomeArea = nomeArea;
    }

    public String getDescArea() {
        return descArea;
    }

    public void setDescArea(String descArea) {
        this.descArea = descArea;
    }

    public String getCondID() {
        return condID;
    }

    public void setCondID(String condID) {
        this.condID = condID;
    }

    public File getImgArea() {
        return imgArea;
    }

    public void setImgArea(File imgArea) {
        this.imgArea = imgArea;
    }

    public void cadastraAreaComum(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();;

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imgArea);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("image_file", imgArea.getName(), requestBody);

        RequestBody nome = RequestBody.create(MediaType.parse("text/plain"), nomeArea);
        RequestBody descricao = RequestBody.create(MediaType.parse("text/plain"), descArea);
        RequestBody condominioID = RequestBody.create(MediaType.parse("text/plain"), condID);

        UploadApis uploadApis = retrofit.create(UploadApis.class);
        Call call = uploadApis.uploadImage(token, nome, descricao, condominioID, parts);
            call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    //JSONObject obj = new JSONObject(response.toString());
                    Log.e("response", response.toString());
                    Log.e("response", call.toString());
                    //Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, String.valueOf(e.getMessage()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(context, "Algo deu errado", Toast.LENGTH_LONG).show();
            }
        });
    }
}
