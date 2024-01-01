package com.theark.p2pwriters.Admin;
// Your package declaration here

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.theark.p2pwriters.Model.Salesperson;
import com.theark.p2pwriters.R;

public class AddSalespersonActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView photoPlaceholder;
    private TextInputEditText fullNameEditText, phoneNumberEditText, emailAddressEditText;
    private TextInputLayout fullNameInputLayout, phoneNumberInputLayout, emailAddressInputLayout;
    private Button uploadButton;

    private Uri imageUri;

    private FirebaseFirestore firestore;
    private CollectionReference salespersonsCollection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salesperson);

        firestore = FirebaseFirestore.getInstance();
        salespersonsCollection = firestore.collection("salespersons");

        photoPlaceholder = findViewById(R.id.photoPlaceholder);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        emailAddressEditText = findViewById(R.id.emailAddressEditText);
        fullNameInputLayout = findViewById(R.id.fullNameInputLayout);
        phoneNumberInputLayout = findViewById(R.id.phoneNumberInputLayout);
        emailAddressInputLayout = findViewById(R.id.emailAddressInputLayout);
        uploadButton = findViewById(R.id.uploadButton);

        // Set click listener for photo placeholder
        photoPlaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        // Set click listener for Upload button
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadSalespersonInfo();
            }
        });
    }

    // Open file chooser for image selection
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(photoPlaceholder);
        }
    }

    // Upload salesperson information to Firestore
    private void uploadSalespersonInfo() {
        String fullName = fullNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String emailAddress = emailAddressEditText.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(emailAddress) || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        Salesperson salesperson = new Salesperson(fullName, phoneNumber, emailAddress, imageUri.toString());
        salespersonsCollection.add(salesperson)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Salesperson information uploaded successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity after successful upload
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to upload salesperson information", Toast.LENGTH_SHORT).show();
                });
    }
}
