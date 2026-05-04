package com.example.homework1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.example.homework1.api.NewsApiService;
import com.example.homework1.model.Article;
import com.example.homework1.paging.NewsPagingSource;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

public class NewsViewModel extends ViewModel {

    private final LiveData<PagingData<Article>> newsLiveData;

    public NewsViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .build();

        NewsApiService apiService = retrofit.create(NewsApiService.class);

        Pager<Integer, Article> pager = new Pager<>(
                new PagingConfig(20, 5, false, 20, PagingConfig.MAX_SIZE_UNBOUNDED),
                () -> new NewsPagingSource(apiService, "covid", BuildConfig.API_KEY)
        );

        newsLiveData = PagingLiveData.getLiveData(pager);
    }

    public LiveData<PagingData<Article>> getNewsLiveData() {
        return newsLiveData;
    }
}