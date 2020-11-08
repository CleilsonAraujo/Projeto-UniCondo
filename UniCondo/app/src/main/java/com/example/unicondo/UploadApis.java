package com.example.unicondo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApis {
    @Multipart
    @POST("common-areas")
    Call<RequestBody> uploadImage(@Header("Authorization") String token,
                                  @Part("name") RequestBody nome,
                                  @Part("description") RequestBody descricao,
                                  @Part("condominium_id") RequestBody condID,
                                  @Part MultipartBody.Part arquivo);
}