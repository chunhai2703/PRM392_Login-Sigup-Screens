package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.entity.ProductEntity;
import com.example.myapplication.src.ProductAdapter;

import java.text.DecimalFormat;
import java.util.List;

public class DetailProductActivity extends AppCompatActivity {
    private TextView productNameTextView, productPriceTextView;
    private ImageView productImageView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        productNameTextView = findViewById(R.id.productName);
        productPriceTextView = findViewById(R.id.productPrice);
        productImageView = findViewById(R.id.productImage);

        String productName = getIntent().getStringExtra("productName");
        double productPrice = getIntent().getDoubleExtra("productPrice", 0);
        String productImage = getIntent().getStringExtra("productImage");


        DecimalFormat df = new DecimalFormat("0.000");
        String formattedPrice = df.format(productPrice);

        productNameTextView.setText(productName);
        productPriceTextView.setText(formattedPrice +" đ");

        // Using Glide to load the image
        Glide.with(this)
                .load(productImage)
                .placeholder(R.drawable.error_image) // optional placeholder
                .error(R.drawable.error_image) // optional error image
                .into(productImageView);
    }
}
