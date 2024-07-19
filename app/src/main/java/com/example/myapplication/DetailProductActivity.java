package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.entity.CartEntity;
import com.example.myapplication.entity.ProductEntity;

import java.text.DecimalFormat;

public class DetailProductActivity extends AppCompatActivity {
    private TextView productNameTextView, productPriceTextView;
    private ImageView productImageView;
    private Button addToCartButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        productNameTextView = findViewById(R.id.productName);
        productPriceTextView = findViewById(R.id.productPrice);
        productImageView = findViewById(R.id.productImage);
        addToCartButton = findViewById(R.id.addToCartButton);

        String productName = getIntent().getStringExtra("productName");
        double productPrice = getIntent().getDoubleExtra("productPrice", 0);
        String productImage = getIntent().getStringExtra("productImage");
        String productId = getIntent().getStringExtra("productId"); // Assuming product ID is passed

        DecimalFormat df = new DecimalFormat("0.000");
        String formattedPrice = df.format(productPrice);

        productNameTextView.setText(productName);
        productPriceTextView.setText(formattedPrice + " đ");

        // Using Glide to load the image
        Glide.with(this)
                .load(productImage)
                .placeholder(R.drawable.error_image) // optional placeholder
                .error(R.drawable.error_image) // optional error image
                .into(productImageView);

        // Add to Cart button functionality
        addToCartButton.setOnClickListener(v -> {
            ProductEntity product = new ProductEntity();
            product.setName(productName);
            product.setPrice(productPrice);
            product.setUrlImage(productImage);
            product.setId(productId); // Set the product ID
            CartEntity.getInstance().addProduct(product);
            // Optionally show a confirmation message
            Toast.makeText(DetailProductActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
        });
    }
}
