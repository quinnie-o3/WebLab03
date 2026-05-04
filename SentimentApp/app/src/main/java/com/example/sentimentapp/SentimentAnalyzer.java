package com.example.sentimentapp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class SentimentAnalyzer {
    private static final String TAG = "SentimentAnalyzer";
    private Interpreter interpreter;

    public SentimentAnalyzer(Context context) {
        try {
            Interpreter.Options options = new Interpreter.Options();
            interpreter = new Interpreter(loadModelFile(context), options);
        } catch (Exception e) {
            interpreter = null;
            Log.e(TAG, "Cannot load model", e);
        }
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("sentiment_model.tflite");
        try (FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }
    }

    public SentimentResult analyze(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new SentimentResult("Unknown", "Please enter text first.");
        }
        if (interpreter == null) {
            return new SentimentResult("Unknown", "Model file not found or cannot be loaded.");
        }

        try {
            String[] input = new String[]{text};
            float[][] output = new float[1][1];
            interpreter.run(input, output);
            float score = output[0][0];
            String label;
            if (score >= 0.65f) {
                label = "Positive";
            } else if (score <= 0.35f) {
                label = "Negative";
            } else {
                label = "Neutral";
            }
            return new SentimentResult(label, null);
        } catch (Exception e) {
            Log.e(TAG, "Analyze error", e);
            return new SentimentResult("Unknown", "Cannot analyze this text.");
        }
    }
}
