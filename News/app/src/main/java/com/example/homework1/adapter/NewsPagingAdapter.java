package com.example.homework1.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homework1.databinding.ItemArticleBinding;
import com.example.homework1.model.Article;

public class NewsPagingAdapter extends PagingDataAdapter<Article, NewsPagingAdapter.ArticleViewHolder> {

    public NewsPagingAdapter() {
        super(new DiffUtil.ItemCallback<Article>() {
            @Override
            public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
                return oldItem.getTitle() != null && oldItem.getTitle().equals(newItem.getTitle());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArticleBinding binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ArticleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = getItem(position);
        if (article != null) {
            holder.bind(article);
        }
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        private final ItemArticleBinding binding;

        public ArticleViewHolder(ItemArticleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Article article) {
            binding.tvTitle.setText(article.getTitle() != null ? article.getTitle() : "");
            binding.tvDescription.setText(article.getDescription() != null ? article.getDescription() : "");
            binding.tvSource.setText(article.getSource() != null && article.getSource().getName() != null ? article.getSource().getName() : "");
            binding.tvPublishedAt.setText(article.getPublishedAt() != null ? article.getPublishedAt() : "");

            if (article.getUrlToImage() != null) {
                Glide.with(binding.ivArticleImage.getContext())
                        .load(article.getUrlToImage())
                        .into(binding.ivArticleImage);
            }
        }
    }
}