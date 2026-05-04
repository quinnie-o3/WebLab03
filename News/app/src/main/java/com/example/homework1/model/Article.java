package com.example.homework1.model;

import com.google.gson.annotations.SerializedName;

public class Article {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("urlToImage")
    private String urlToImage;

    @SerializedName("publishedAt")
    private String publishedAt;

    @SerializedName("source")
    private Source source;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUrlToImage() { return urlToImage; }
    public void setUrlToImage(String urlToImage) { this.urlToImage = urlToImage; }

    public String getPublishedAt() { return publishedAt; }
    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }

    public Source getSource() { return source; }
    public void setSource(Source source) { this.source = source; }
}