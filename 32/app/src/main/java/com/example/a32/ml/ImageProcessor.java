package com.example.a32.ml;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.example.a32.data.model.ImageItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageProcessor {
    private final Context context;

    public ImageProcessor(Context context) {
        this.context = context.getApplicationContext();
    }

    public interface ImageProcessCallback {
        void onSuccess(List<String> labels);
        void onError(String errorMessage);
    }

    public void processImage(Context context, ImageItem imageItem, ImageProcessCallback callback) {
        String imageUrl = imageItem != null ? imageItem.getWebformatURL() : null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                mainHandler.post(() -> callback.onSuccess(Collections.singletonList("Unknown")));
                executor.shutdown();
                return;
            }

            try {
                FutureTarget<Bitmap> futureTarget = Glide.with(this.context)
                        .asBitmap()
                        .load(imageUrl)
                        .submit();
                Bitmap bitmap = futureTarget.get(15, TimeUnit.SECONDS);
                Glide.with(this.context).clear(futureTarget);

                if (bitmap == null) {
                    mainHandler.post(() -> callback.onSuccess(Collections.singletonList("Unknown")));
                    executor.shutdown();
                    return;
                }

                InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
                ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
                labeler.process(inputImage)
                        .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                            @Override
                            public void onSuccess(List<ImageLabel> imageLabels) {
                                List<ImageLabel> filtered = new ArrayList<>();
                                for (ImageLabel label : imageLabels) {
                                    if (label.getConfidence() >= 0.5f) {
                                        filtered.add(label);
                                    }
                                }
                                filtered.sort((first, second) -> Float.compare(second.getConfidence(), first.getConfidence()));
                                List<String> labels = new ArrayList<>();
                                for (int i = 0; i < filtered.size() && i < 3; i++) {
                                    labels.add(filtered.get(i).getText());
                                }
                                final List<String> resultLabels;
                                if (labels.isEmpty()) {
                                    resultLabels = Collections.singletonList("Unknown");
                                } else {
                                    resultLabels = new ArrayList<>(labels);
                                }
                                mainHandler.post(() -> callback.onSuccess(resultLabels));
                                executor.shutdown();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mainHandler.post(() -> callback.onSuccess(Collections.singletonList("Unknown")));
                                executor.shutdown();
                            }
                        });
            } catch (Exception e) {
                mainHandler.post(() -> callback.onSuccess(Collections.singletonList("Unknown")));
                executor.shutdown();
            }
        });
    }
}
