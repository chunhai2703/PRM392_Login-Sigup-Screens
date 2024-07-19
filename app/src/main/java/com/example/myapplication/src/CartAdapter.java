package com.example.myapplication.src;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entity.ProductEntity;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<ProductEntity> cartProducts;

    public CartAdapter(List<ProductEntity> cartProducts) {
        this.cartProducts = cartProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductEntity product = cartProducts.get(position);
        holder.productNameTextView.setText(product.getName());
        holder.productCategoryTextView.setText(product.getCategory());

        double price = product.getPrice();
        DecimalFormat df = new DecimalFormat("0.000"); // Two decimal places
        String formattedPrice = df.format(price) + " đ";

        holder.productPriceTextView.setText(formattedPrice);
        Glide.with(holder.itemView.getContext())
                .load(product.getUrlImage())
                .placeholder(R.drawable.error_image) // Optional placeholder image
                .error(R.drawable.error_image) // Optional error image
                .into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productCategoryTextView;
        TextView productPriceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productCategoryTextView = itemView.findViewById(R.id.productCategoryTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
        }
    }
}
