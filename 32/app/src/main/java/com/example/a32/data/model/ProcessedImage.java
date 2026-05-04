package com.example.a32.data.model;

import java.util.List;

public class ProcessedImage {
    private ImageItem imageItem;
    private List<String> labels;
    private boolean processed;

    public ProcessedImage(ImageItem imageItem, List<String> labels, boolean processed) {
        this.imageItem = imageItem;
        this.labels = labels;
        this.processed = processed;
    }

    public ImageItem getImageItem() {
        return imageItem;
    }

    public void setImageItem(ImageItem imageItem) {
        this.imageItem = imageItem;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
