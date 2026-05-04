package com.example.a32.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PixabayResponse {
    @SerializedName("total")
    private int total;
    @SerializedName("totalHits")
    private int totalHits;
    @SerializedName("hits")
    private List<ImageItem> hits;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public List<ImageItem> getHits() {
        return hits;
    }

    public void setHits(List<ImageItem> hits) {
        this.hits = hits;
    }
}
