package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.entity.CartEntity;
import com.example.myapplication.entity.ProductEntity;
import com.example.myapplication.src.CartAdapter;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartView;
    private CartAdapter cartAdapter;
    private List<ProductEntity> cartProducts;
    private TextView totaltxt, delitxt, taxtxt, totalfntxt;

    private void updateCartTotals() {
        double total = 0;
        double deliveryFee = 20.0; // Example delivery fee
        double tax = 2.0; // Example tax

        // Calculate the total price of products
        for (ProductEntity product : cartProducts) {
            total += product.getPrice();
        }

        // Format the amounts
        DecimalFormat df = new DecimalFormat("0.000" + " đ");

        // Set text values for the total amounts
        totaltxt.setText(df.format(total));
        delitxt.setText(df.format(deliveryFee));
        taxtxt.setText(df.format(tax));
        totalfntxt.setText(df.format(total + deliveryFee + tax));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartView = findViewById(R.id.cartView);
        totaltxt = findViewById(R.id.totaltxt);
        delitxt = findViewById(R.id.delitxt);
        taxtxt = findViewById(R.id.taxtxt);
        totalfntxt = findViewById(R.id.totalfntxt);

        // Initialize cart products from CartEntity
        cartProducts = CartEntity.getInstance().getCartProducts();

        if (cartProducts != null && !cartProducts.isEmpty()) {
            // Set up RecyclerView with CartAdapter
            cartAdapter = new CartAdapter(cartProducts);
            cartView.setLayoutManager(new LinearLayoutManager(this));
            cartView.setAdapter(cartAdapter);

            // Update the totals for the cart
            updateCartTotals();
        } else {
            // Handle the case where cart is empty
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
        }
    }
}
