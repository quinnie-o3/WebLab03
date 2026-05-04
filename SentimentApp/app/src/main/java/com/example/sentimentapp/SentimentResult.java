package com.example.sentimentapp;

public class SentimentResult {
    private final String label;
    private final String errorMessage;

    public SentimentResult(String label, String errorMessage) {
        this.label = label;
        this.errorMessage = errorMessage;
    }

    public String getLabel() {
        return label;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
