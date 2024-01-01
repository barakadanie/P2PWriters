package com.theark.p2pwriters.Admin;

// Your imports here

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.theark.p2pwriters.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSalesActivity extends AppCompatActivity {

    private Spinner spinnerSendTo, spinnerBookCategory, spinnerBooks;
    private EditText etAmount, etDateSent;
    private Button btnSendOut;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    private FirebaseFirestore firestore;
    private CollectionReference usersCollection, bookCategoryCollection, booksCollection, sendoutCollection;

    private List<String> usersList, bookCategoryList, booksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);

        firestore = FirebaseFirestore.getInstance();
        usersCollection = firestore.collection("users");
        bookCategoryCollection = firestore.collection("bookCategory");
        booksCollection = firestore.collection("books");
        sendoutCollection = firestore.collection("sendout");

        spinnerSendTo = findViewById(R.id.spinnerSendTo);
        spinnerBookCategory = findViewById(R.id.spinnerBookCategory);
        spinnerBooks = findViewById(R.id.spinnerBooks);
        etAmount = findViewById(R.id.etAmount);
        etDateSent = findViewById(R.id.etDateSent);
        btnSendOut = findViewById(R.id.btnSendOut);

        usersList = new ArrayList<>();
        bookCategoryList = new ArrayList<>();
        booksList = new ArrayList<>();
etDateSent.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Calendar calendar=Calendar.getInstance();
        int y=calendar.get(Calendar.YEAR);
        int m=calendar.get(Calendar.MONTH);
        int d=calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog=new DatePickerDialog(AddSalesActivity.this,onDateSetListener,y,m,d);
        datePickerDialog.show();
    }
});
        onDateSetListener=new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=dayOfMonth+"/"+month+"/"+year;
                etDateSent.setText(date);
            }
        };
        // Fetch data for spinners from Firestore
        fetchUsers();
        fetchBookCategories();

        // Set up event listeners
        btnSendOut.setOnClickListener(v -> {
            // Handle the "Send Out" button click
            saveSalesData();
        });

        // Set up the book name spinner based on the selected category
        spinnerBookCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = (String) parentView.getSelectedItem();
                fetchBooksForCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where nothing is selected
            }
        });
    }

    private void fetchUsers() {
        usersCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            usersList.add(username);
                        }

                        // Set up the spinner adapter
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                AddSalesActivity.this,
                                android.R.layout.simple_spinner_item,
                                usersList
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // Set the adapter to the spinner
                        spinnerSendTo.setAdapter(adapter);
                    } else {
                        // Handle failure to fetch users
                    }
                });
    }

    private void fetchBookCategories() {
        bookCategoryCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String category = document.getString("categoryName");
                                bookCategoryList.add(category);
                            }

                            // Set up the spinner adapter
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    AddSalesActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    bookCategoryList
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // Set the adapter to the spinner
                            spinnerBookCategory.setAdapter(adapter);
                        } else {
                            // Handle failure to fetch book categories
                        }
                    }
                });
    }

    private void fetchBooksForCategory(String selectedCategory) {
        booksCollection.whereEqualTo("bookCategory", selectedCategory)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> bookNames = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String bookName = document.getString("bookName");
                                bookNames.add(bookName);
                            }

                            // Set up the book name spinner adapter
                            ArrayAdapter<String> bookNameAdapter = new ArrayAdapter<>(
                                    AddSalesActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    bookNames
                            );
                            bookNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // Set the adapter to the book name spinner
                            spinnerBooks.setAdapter(bookNameAdapter);

                        } else {
                            // Handle failure to fetch books for the selected category
                            Log.e("FetchBooks", "Error fetching books: " + task.getException());
                        }
                    }
                });
    }

    private void saveSalesData() {
        // Retrieve values from UI components
        String sendTo = spinnerSendTo.getSelectedItem().toString();
        String bookCategory = spinnerBookCategory.getSelectedItem().toString();
        String book = spinnerBooks.getSelectedItem().toString();
        String amount = etAmount.getText().toString();
        String dateSent = etDateSent.getText().toString();

        // Create a Sales object or use a Map to store data

        Map<String, Object> salesData = new HashMap<>();
       // salesData.put("documentID",)
        salesData.put("sendTo", sendTo);
        salesData.put("bookCategory", bookCategory);
        salesData.put("book", book);
        salesData.put("amount", amount);
        salesData.put("dateSent", dateSent);

        // Add the data to the "sendout" collection in Firestore
        sendoutCollection.add(salesData)
                .addOnSuccessListener(documentReference -> {
                    // Handle success
                    // Show a dialog to inform the user that the data has been saved successfully
                    showSuccessDialog();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    // Show a dialog to inform the user that there was an error
                    showErrorDialog();
                });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success")
                .setMessage("Sales data saved successfully")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                    // Finish the activity to go back to the BooksFragment
                    finish();
                })
                .show();
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage("Failed to save sales data")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                })
                .show();
    }

}
