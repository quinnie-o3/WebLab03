package com.example.homework1.api;

import com.example.homework1.model.NewsResponse;
import com.google.common.util.concurrent.ListenableFuture;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("v2/everything")
    ListenableFuture<NewsResponse> getNews(
        @Query("q") String query,
        @Query("apiKey") String apiKey,
        @Query("page") int page,
        @Query("pageSize") int pageSize
    );
}