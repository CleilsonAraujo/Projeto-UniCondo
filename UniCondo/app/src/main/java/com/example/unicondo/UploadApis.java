package com.example.unicondo;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;


public interface UploadApis {
    @Multipart
    @POST("common-areas")
    Call<ResponseBody> uploadImage(@Header("Authorization") String token,
                                  @Part("name") RequestBody nome,
                                  @Part("description") RequestBody descricao,
                                  @Part("condominium_id") RequestBody condID,
                                  @Part MultipartBody.Part arquivo);
}

/*
public interface UploadApis {
    @Multipart
    @POST("common-areas")
    Call<Response> uploadImage(@Header("Authorization") String token,
                               @PartMap Map<String, RequestBody> params);
}
*/
/*
public interface UploadApis {
    @Multipart
    @POST("common-areas")
    Call<RequestBody> uploadImage(@Header("Authorization") String token,
                                  @Part("name") RequestBody nome,
                                  @Part("description") RequestBody descricao,
                                  @Part("condominium_id") RequestBody condID,
                                  @Part("image_file") RequestBody arquivo);
}
 */