package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private FirebaseAuth mAuth;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        EdgeToEdge.enable(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Connection successful, you can perform operations with the database here
                displaySuccessMessage("Connection successful!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Connection failed, you can handle the error here
                String errorMessage = databaseError.getMessage();
                displayFailureMessage("Connection failed: " + errorMessage);
            }
        });

        DatabaseReference myRef = database.getReference("product");

        myRef.child("abc").setValue("demo")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Kết nối thành công
                        Log.d("Firebase", "Data set successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi
                        Log.e("Firebase", "Failed to set data", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "Data set successfully - OnComplete");
                        } else {
                            Log.e("Firebase", "Failed to set data - OnComplete", task.getException());
                        }
                    }
                });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ProductEntity> courseList = generateDummyCourseList();
        ProductAdapter adapter = new ProductAdapter(courseList);
        recyclerView.setAdapter(adapter);
    }
    private void displaySuccessMessage(String message) {
        // Use a UI framework like Android's Toast or a custom dialog to display the success message
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void displayFailureMessage(String message) {
        // Use a UI framework like Android's Toast or a custom dialog to display the failure message
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    private List<ProductEntity> generateDummyCourseList() {
        List<ProductEntity> courses = new ArrayList<>();
        courses.add(new ProductEntity("C#", "abc", 123,12,"abc",2345,"123","abc"));
        courses.add(new ProductEntity("C#3", "abc", 123,12,"abc",2345,"123","abc"));
        courses.add(new ProductEntity("C4#", "abc", 123,12,"abc",2345,"123","abc"));
        courses.add(new ProductEntity("C6#", "abc", 123,12,"abc",2345,"123","abc"));
        courses.add(new ProductEntity("C7#", "abc", 123,12,"abc",2345,"123","abc"));
        courses.add(new ProductEntity("C8#", "abc", 123,12,"abc",2345,"123","abc"));

        return courses;
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
