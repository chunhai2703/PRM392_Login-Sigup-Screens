package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.entity.ProductEntity;
import com.example.myapplication.src.ApiService;
import com.example.myapplication.src.ProductAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private FirebaseAuth mAuth;
    private ApiService apiService;
    private TextView viewCartButton;
    private List<ProductEntity> listItem = new ArrayList<>();
    private ProductAdapter adapter;

    Button logoutBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        // Initialize views
        logoutBtn = findViewById(R.id.logout_btn);

        // Set up the logout click listener
        logoutBtn.setOnClickListener(v -> {
            Log.d("Logout", "Logout button clicked");
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData(new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<ProductEntity> list) {
                listItem.clear();
                listItem = list;
                adapter = new ProductAdapter(list,HomeActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });

        viewCartButton = findViewById(R.id.cart_button);
        viewCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        SearchView searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v("TAGC", query);
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v("TAGC", newText);
                filterList(newText);
                return true;
            }
        });
        // Set up filter buttons
        Button filterByPriceAsc = findViewById(R.id.filter_btn1);
        filterByPriceAsc.setOnClickListener(v -> sortByPriceAsc());

        Button filterByPriceDesc = findViewById(R.id.filter_btn2);
        filterByPriceDesc.setOnClickListener(v -> sortByPriceDesc());

        Button filterByNameAsc = findViewById(R.id.filter_btn3);
        filterByNameAsc.setOnClickListener(v -> sortByNameAsc());

        Button filterByNameDesc = findViewById(R.id.filter_btn4);
        filterByNameDesc.setOnClickListener(v -> sortByNameDesc());
    }
    private void sortByPriceAsc() {
        Collections.sort(listItem, Comparator.comparingDouble(ProductEntity::getPrice));
        adapter.updateList(listItem);
        adapter.notifyDataSetChanged();
    }

    private void sortByPriceDesc() {
        Collections.sort(listItem, Comparator.comparingDouble(ProductEntity::getPrice).reversed());
        adapter.updateList(listItem);
        adapter.notifyDataSetChanged();
    }

    private void sortByNameAsc() {
        Collections.sort(listItem, Comparator.comparing(ProductEntity::getName));
        adapter.updateList(listItem);
        adapter.notifyDataSetChanged();
    }

    private void sortByNameDesc() {
        Collections.sort(listItem, Comparator.comparing(ProductEntity::getName).reversed());
        adapter.updateList(listItem);
        adapter.notifyDataSetChanged();
    }

    private void filterList(String query) {
        List<ProductEntity> filteredList = new ArrayList<>();
        for (ProductEntity product : listItem) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        adapter.updateList(filteredList);
    }
    private void displaySuccessMessage(String message) {
        // Use a UI framework like Android's Toast or a custom dialog to display the success message
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void displayFailureMessage(String message) {
        // Use a UI framework like Android's Toast or a custom dialog to display the failure message
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
                        String productName = snapshot.child("Name").getValue(String.class);
                        String image = snapshot.child("UrlImage").getValue(String.class);
                        if(image == null){
                            image = "https://dotrinh.com/wp-content/uploads/2019/01/loading_indicator.gif";
                        }
                        String price = snapshot.child("Price").getValue(String.class);
                        double valuePrice = 0;
                        if(price == null){
                            valuePrice = 100;
                        }else{
                            valuePrice = Double.parseDouble(price);
                        }
                        String des = snapshot.child("Description").getValue(String.class);
                        int number = snapshot.child("Quantity").getValue(Integer.class);
                        String brand = snapshot.child("Brand").getValue(String.class);

                        int yearOfManufacture = snapshot.child("YearOfManufacture").getValue(Integer.class);

                        listData.add(new ProductEntity(productName, des, valuePrice, number, brand, yearOfManufacture, "123", image));
                    }
                    listener.onDataLoaded(listData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayFailureMessage(error.getMessage());
            }
        });
    }
    interface OnDataLoadedListener {
        void onDataLoaded(List<ProductEntity> courseList);
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
