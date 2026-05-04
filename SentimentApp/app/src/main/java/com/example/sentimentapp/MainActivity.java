package com.example.sentimentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private SentimentAnalyzer analyzer;
    private EditText edtInput;
    private TextView tvResult;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        analyzer = new SentimentAnalyzer(this);
        edtInput = findViewById(R.id.edtInput);
        tvResult = findViewById(R.id.tvResult);
        tvError = findViewById(R.id.tvError);
        Button btnAnalyze = findViewById(R.id.btnAnalyze);

        btnAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyzeText();
            }
        });
    }

    private void analyzeText() {
        String text = edtInput.getText().toString();
        SentimentResult result = analyzer.analyze(text);
        tvResult.setText("Sentiment: " + result.getLabel());

        if (result.getErrorMessage() != null) {
            tvError.setText(result.getErrorMessage());
            tvError.setVisibility(View.VISIBLE);
        } else {
            tvError.setVisibility(View.GONE);
        }

        int colorRes;
        switch (result.getLabel()) {
            case "Positive":
                colorRes = android.R.color.holo_green_dark;
                break;
            case "Negative":
                colorRes = android.R.color.holo_red_dark;
                break;
            case "Neutral":
                colorRes = android.R.color.darker_gray;
                break;
            default:
                colorRes = android.R.color.darker_gray;
                break;
        }
        tvResult.setTextColor(ContextCompat.getColor(this, colorRes));
    }
}
