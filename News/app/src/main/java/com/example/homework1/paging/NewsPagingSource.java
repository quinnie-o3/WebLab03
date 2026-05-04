package com.example.homework1.paging;

import androidx.annotation.NonNull;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.LoadResult;
import androidx.paging.PagingState;

import com.example.homework1.api.NewsApiService;
import com.example.homework1.model.Article;
import com.example.homework1.model.NewsResponse;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsPagingSource extends ListenableFuturePagingSource<Integer, Article> {

    private final NewsApiService apiService;
    private final String query;
    private final String apiKey;
    private final ListeningExecutorService executorService;

    public NewsPagingSource(NewsApiService apiService, String query, String apiKey) {
        this.apiService = apiService;
        this.query = query;
        this.apiKey = apiKey;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        this.executorService = MoreExecutors.listeningDecorator(executor);
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, Article>> loadFuture(@NonNull LoadParams<Integer> params) {
        Integer page = params.getKey();
        if (page == null) {
            page = 1;
        }

        return Futures.transform(apiService.getNews(query, apiKey, page, params.getLoadSize()), (NewsResponse response) -> {
            if (response == null || response.getArticles() == null) {
                return new LoadResult.Error<>(new Exception("Invalid response"));
            }
            List<Article> articles = response.getArticles();
            Integer prevKey = page == 1 ? null : page - 1;
            Integer nextKey = articles.isEmpty() ? null : page + 1;
            return new LoadResult.Page<>(articles, prevKey, nextKey);
        }, executorService);
    }

    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Article> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }
        LoadResult.Page<Integer, Article> anchorPage = pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }
        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }
        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }
        return null;
    }
}