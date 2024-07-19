package com.example.myapplication.src;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.opengl.GLES30;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DetailProductActivity;
import com.example.myapplication.R;
import com.example.myapplication.entity.ProductEntity;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductEntity> productList = null;
    private Context context;

    public ProductAdapter(List<ProductEntity> courseList, Context context) {
        this.productList = courseList;
        this.context = context;
    }
    public void updateList(List<ProductEntity> newList) {
        productList = newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);
        return new ProductViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        ProductEntity currentCourse = productList.get(position);
        holder.courseName.setText(currentCourse.getName());

        double price = currentCourse.getPrice();
        DecimalFormat df = new DecimalFormat("0.000");
        String formattedPrice = df.format(price);

        holder.coursePrice.setText(formattedPrice + " đ");
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(currentCourse.getUrlImage()))
                .placeholder(R.drawable.error_image) // optional
                .error(R.drawable.error_image) // optional
                .into(holder.courseImg);
        Log.v("TAGV", currentCourse.toString());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailProductActivity.class);
            intent.putExtra("productName", currentCourse.getName());
            intent.putExtra("productPrice", currentCourse.getPrice());
            intent.putExtra("productImage", currentCourse.getUrlImage());
            intent.putExtra("productDes", currentCourse.getDescription());
            intent.putExtra("productQuantity", String.valueOf(currentCourse.getQuantity()));
            intent.putExtra("productBrand", currentCourse.getBrand());
            intent.putExtra("productYearOfManufacture", String.valueOf(currentCourse.getYearOfManufacture()));

            context.startActivity(intent);
        });

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
