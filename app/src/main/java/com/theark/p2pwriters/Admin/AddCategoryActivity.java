package com.theark.p2pwriters.Admin;

// Imports and package declaration here

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theark.p2pwriters.Model.Category;
import com.theark.p2pwriters.R;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText etCategoryName;
    private Button btnSaveCategory;

    private FirebaseFirestore firestore;
    private CollectionReference categoryCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        firestore = FirebaseFirestore.getInstance();
        categoryCollection = firestore.collection("bookCategory");

        etCategoryName = findViewById(R.id.etCategoryName);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);

        btnSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoryToFirestore();
            }
        });
    }

    private void addCategoryToFirestore() {
        String categoryName = etCategoryName.getText().toString();

        if (TextUtils.isEmpty(categoryName)) {
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            return;
        }

        Category category = new Category(categoryName);

        categoryCollection.add(category)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity after successful category addition
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show();
                });
    }
}
