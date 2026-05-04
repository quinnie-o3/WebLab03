package com.example.a32.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.a32.R;
import com.example.a32.data.model.ImageItem;
import com.example.a32.data.model.ProcessedImage;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final List<ProcessedImage> imageList;

    public ImageAdapter(List<ProcessedImage> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ProcessedImage processedImage = imageList.get(position);
        ImageItem item = processedImage.getImageItem();

        Glide.with(holder.imgPhoto.getContext())
                .load(item.getWebformatURL())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.imgPhoto);

        holder.tvTags.setText("Tags: " + safeString(item.getTags()));
        holder.tvUser.setText("By: " + safeString(item.getUser()));

        List<String> labels = processedImage.getLabels();
        String labelText = "AI: ";
        if (labels == null || labels.isEmpty()) {
            labelText += "Processing...";
        } else {
            labelText += TextUtils.join(", ", labels);
        }
        holder.tvLabels.setText(labelText);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    private String safeString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "Unknown";
        }
        return value;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgPhoto;
        final TextView tvTags;
        final TextView tvUser;
        final TextView tvLabels;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            tvTags = itemView.findViewById(R.id.tvTags);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvLabels = itemView.findViewById(R.id.tvLabels);
        }
    }
}
