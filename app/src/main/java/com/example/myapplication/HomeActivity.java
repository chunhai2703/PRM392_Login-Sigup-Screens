package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.entity.ProductEntity;
import com.example.myapplication.src.ApiService;
import com.example.myapplication.src.ProductAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private Button homeButton, cartButton, accountButton, historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load data and set adapter after data is fetched
        loadData(productList -> {
            ProductAdapter adapter = new ProductAdapter(productList, HomeActivity.this);
            recyclerView.setAdapter(adapter);
        });

        // Footer buttons setup
        homeButton = findViewById(R.id.home_button);
        cartButton = findViewById(R.id.cart_button);
        accountButton = findViewById(R.id.account_button);
        historyButton = findViewById(R.id.history_button);

        homeButton.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
        });

        cartButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        });

        accountButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AccountActivity.class));
        });

        historyButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
        });
    }

    private void displaySuccessMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void displayFailureMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void loadData(OnDataLoadedListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://books-app-bdc4b-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("product");
        List<ProductEntity> listData = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("HomeActivity", snapshot.toString());

                        String productName = snapshot.child("Name").getValue(String.class);
                        String image = snapshot.child("UrlImage").getValue(String.class);
                        if (image == null) {
                            image = "https://dotrinh.com/wp-content/uploads/2019/01/loading_indicator.gif";
                        }
                        String price = snapshot.child("Price").getValue(String.class);
                        double valuePrice = price == null ? 100 : Double.parseDouble(price);

                        listData.add(new ProductEntity(productName, "abc", valuePrice, 12, "abc", 2345, "123", image));
                    }
                    listener.onDataLoaded(listData);
                } else {
                    displayFailureMessage("No data available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayFailureMessage(error.getMessage());
            }
        });
    }

    interface OnDataLoadedListener {
        void onDataLoaded(List<ProductEntity> productList);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Clear the text when the app is minimized
        if (welcomeTextView != null) {
            welcomeTextView.setText("");
        }
    }
}
