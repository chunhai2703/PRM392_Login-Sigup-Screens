package com.example.myapplication.src;

import android.net.Uri;
import android.opengl.GLES30;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entity.ProductEntity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductEntity> productList = null;

    public ProductAdapter(List<ProductEntity> courseList) {
        this.productList = courseList;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        ProductEntity currentCourse = productList.get(position);
        holder.courseName.setText(currentCourse.getName());
        holder.coursePrice.setText(String.valueOf(currentCourse.getPrice()) + "$");
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(currentCourse.getUrlImage()))
                .placeholder(R.drawable.error_image) // optional
                .error(R.drawable.error_image) // optional
                .into(holder.courseImg);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName;
        public ImageView courseImg;
        public TextView coursePrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
            courseImg = itemView.findViewById(R.id.courseImg);
            coursePrice = itemView.findViewById(R.id.coursePrice);
        }
    }
}
