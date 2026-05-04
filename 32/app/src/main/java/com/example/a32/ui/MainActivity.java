package com.example.a32.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a32.R;
import com.example.a32.data.model.ImageItem;
import com.example.a32.data.model.ProcessedImage;
import com.example.a32.data.repository.ImageRepository;
import com.example.a32.ml.AiCache;
import com.example.a32.ml.ImageProcessor;
import com.example.a32.util.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edtSearch;
    private Button btnSearch;
    private RecyclerView recyclerViewImages;
    private ProgressBar progressBar;
    private TextView tvError;
    private TextView tvEmpty;

    private final ImageRepository repository = new ImageRepository();
    private ImageProcessor imageProcessor;
    private final AiCache aiCache = new AiCache();
    private ImageAdapter adapter;
    private final List<ProcessedImage> allImages = new ArrayList<>();

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String currentQuery = "nature";
    private int totalHits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        tvEmpty = findViewById(R.id.tvEmpty);

        imageProcessor = new ImageProcessor(this);

        adapter = new ImageAdapter(allImages);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewImages.setLayoutManager(layoutManager);
        recyclerViewImages.setAdapter(adapter);

        recyclerViewImages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0 || isLoading || isLastPage) {
                    return;
                }
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 4
                        && firstVisibleItemPosition >= 0) {
                    loadImages();
                }
            }
        });

        btnSearch.setOnClickListener(v -> {
            String keyword = edtSearch.getText().toString();
            currentQuery = normalizeQuery(keyword);
            resetSearch();
            loadImages();
        });

        loadImages();
    }

    private String normalizeQuery(String query) {
        if (query == null) {
            return "nature";
        }
        String trimmed = query.trim().toLowerCase();
        if (trimmed.isEmpty()) {
            return "nature";
        }
        trimmed = trimmed.replaceAll("\\s+", " ");
        if (trimmed.equals("cars")) {
            return "car";
        }
        if (trimmed.equals("dogs")) {
            return "dog";
        }
        return trimmed;
    }

    private void resetSearch() {
        currentPage = 1;
        isLastPage = false;
        allImages.clear();
        adapter.notifyDataSetChanged();
        tvError.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
    }

    private void loadImages() {
        if (isLoading || isLastPage) {
            return;
        }
        isLoading = true;
        showLoading(true);
        repository.searchImages(currentQuery, currentPage, Constants.PAGE_SIZE, new ImageRepository.ImageCallback() {
            @Override
            public void onSuccess(List<ImageItem> images, int totalHitsCount) {
                runOnUiThread(() -> {
                    showLoading(false);
                    isLoading = false;
                    totalHits = totalHitsCount;
                    if (images == null || images.isEmpty()) {
                        if (allImages.isEmpty()) {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                        isLastPage = true;
                        return;
                    }
                    tvError.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.GONE);
                    for (ImageItem imageItem : images) {
                        ProcessedImage processedImage = new ProcessedImage(
                                imageItem,
                                new ArrayList<>(Arrays.asList("Processing...")),
                                false
                        );
                        allImages.add(processedImage);
                        processImageWithCache(processedImage);
                    }
                    adapter.notifyDataSetChanged();
                    if (allImages.size() >= totalHits || images.size() < Constants.PAGE_SIZE) {
                        isLastPage = true;
                    }
                    currentPage++;
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    showLoading(false);
                    isLoading = false;
                    if (allImages.isEmpty()) {
                        tvError.setText(errorMessage);
                        tvError.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void processImageWithCache(ProcessedImage processedImage) {
        ImageItem imageItem = processedImage.getImageItem();
        String key = imageItem != null ? String.valueOf(imageItem.getId()) : null;
        if (key == null || key.trim().isEmpty()) {
            key = imageItem != null ? imageItem.getWebformatURL() : "unknown";
        }
        final String cacheKey = key;
        if (aiCache.contains(cacheKey)) {
            processedImage.setLabels(aiCache.get(cacheKey));
            processedImage.setProcessed(true);
            adapter.notifyDataSetChanged();
            return;
        }

        imageProcessor.processImage(this, imageItem, new ImageProcessor.ImageProcessCallback() {
            @Override
            public void onSuccess(List<String> labels) {
                runOnUiThread(() -> {
                    aiCache.put(cacheKey, labels);
                    processedImage.setLabels(labels);
                    processedImage.setProcessed(true);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    List<String> labels = new ArrayList<>();
                    labels.add("Unknown");
                    aiCache.put(cacheKey, labels);
                    processedImage.setLabels(labels);
                    processedImage.setProcessed(true);
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
