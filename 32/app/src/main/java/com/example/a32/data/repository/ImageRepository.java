package com.example.a32.data.repository;

import com.example.a32.data.model.ImageItem;
import com.example.a32.data.model.PixabayResponse;
import com.example.a32.data.remote.RetrofitClient;
import com.example.a32.util.Constants;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageRepository {

    public interface ImageCallback {
        void onSuccess(List<ImageItem> images, int totalHits);
        void onError(String errorMessage);
    }

    public void searchImages(String query, int page, int perPage, ImageCallback callback) {
        Call<PixabayResponse> call = RetrofitClient.getApiService()
                .searchImages(Constants.PIXABAY_API_KEY, query, "photo", page, perPage);
        call.enqueue(new Callback<PixabayResponse>() {
            @Override
            public void onResponse(Call<PixabayResponse> call, Response<PixabayResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getHits(), response.body().getTotalHits());
                } else {
                    callback.onError("API error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PixabayResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
