package com.example.homework1;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.homework1.adapter.NewsPagingAdapter;
import com.example.homework1.databinding.ActivityMainBinding;
import com.example.homework1.viewmodel.NewsViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NewsViewModel viewModel;
    private NewsPagingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("News");
        }

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        adapter = new NewsPagingAdapter();

        binding.rvNews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNews.setAdapter(adapter);

        adapter.addLoadStateListener(loadStates -> {
            if (loadStates.getRefresh() instanceof LoadState.Loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
            return null;
        });

        viewModel.getNewsLiveData().observe(this, pagingData -> {
            adapter.submitData(getLifecycle(), pagingData);
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}