package com.theark.p2pwriters.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theark.p2pwriters.R;

import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputEditText bookNameEditText, bookCategoryEditText, amountEditText, dateAddedEditText;
    private TextInputLayout bookNameInputLayout, bookCategoryInputLayout, amountInputLayout, dateAddedInputLayout;

    private Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();

        bookNameEditText = findViewById(R.id.bookNameEditText);
        bookCategoryEditText = findViewById(R.id.bookCategoryEditText);
        amountEditText = findViewById(R.id.amountEditText);
        dateAddedEditText = findViewById(R.id.dateAddedEditText);
        ImageView pickImageButton=findViewById(R.id.imgProduct);
        bookNameInputLayout = findViewById(R.id.bookNameInputLayout);
        bookCategoryInputLayout = findViewById(R.id.bookCategoryInputLayout);
        amountInputLayout = findViewById(R.id.amountInputLayout);
        dateAddedInputLayout = findViewById(R.id.dateAddedInputLayout);
        Button uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(v -> {
            // Trigger the upload process when the button is clicked
            uploadImageAndBookInfo();
        });
        // Add your FloatingActionButton setup and other logic as needed

        // Example: Launch image picker when a button is clicked
        // Replace with your actual UI component for choosing an image
        pickImageButton.setOnClickListener(view -> openFileChooser());
    }

    // Example: Handling result from image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            // You can display a preview of the selected image if needed
        }
    }

    // Example: Open file chooser for image selection
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Example: Upload image to Firebase Storage
    private void uploadImageAndBookInfo() {
        if (imageUri != null) {
            // Create a unique filename for the image using the current time
            String imageFileName = "book_image_" + System.currentTimeMillis() + ".jpg";

            // Get a reference to the Firebase Storage location
            StorageReference storageRef = storage.getReference().child("book_images").child(imageFileName);

            // Upload the image to Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image upload successful
                        // Now, get the download URL of the uploaded image
                        storageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    // Now you can save the book information and the image URL to Firestore
                                    saveBookInfo(imageUrl);
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to get image download URL
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Handle image upload failure
                    });
        } else {
            // Image URI is null (user didn't select an image)
            // Handle accordingly
        }
    }

    private void saveBookInfo(String imageUrl) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            String bookName = bookNameEditText.getText().toString().trim();
            String bookCategory = bookCategoryEditText.getText().toString().trim();
            String amount = amountEditText.getText().toString().trim();
            String dateAdded = dateAddedEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(bookName) && !TextUtils.isEmpty(bookCategory)
                    && !TextUtils.isEmpty(amount) && !TextUtils.isEmpty(dateAdded)) {
                // Create a new book object
                Map<String, Object> book = new HashMap<>();
                book.put("bookName", bookName);
                book.put("bookCategory", bookCategory);
                book.put("amount", amount);
                book.put("dateAdded", dateAdded);
                book.put("imageUrl", imageUrl);

                // Save the book information to Firestore
                CollectionReference booksCollection = firestore.collection("books");
                booksCollection.add(book)
                        .addOnSuccessListener(documentReference -> {
                            // Book information added successfully
                            Toast.makeText(AddBookActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure to add book information
                            Toast.makeText(AddBookActivity.this, "Error adding book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Some fields are empty
                // Handle accordingly
            }
        } else {
            // User is not authenticated
            // Handle accordingly
        }
    }
}
