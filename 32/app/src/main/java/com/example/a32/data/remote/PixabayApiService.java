package com.example.a32.data.remote;

import com.example.a32.data.model.PixabayResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayApiService {
    @GET("api/")
    Call<PixabayResponse> searchImages(
            @Query("key") String apiKey,
            @Query("q") String query,
            @Query("image_type") String imageType,
            @Query("page") int page,
            @Query("per_page") int perPage
    );
}
