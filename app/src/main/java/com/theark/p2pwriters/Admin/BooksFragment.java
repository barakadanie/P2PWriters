package com.theark.p2pwriters.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.theark.p2pwriters.Adapters.BooksAdapter;
import com.theark.p2pwriters.Model.Book;
import com.theark.p2pwriters.R;

import java.util.ArrayList;
import java.util.List;

public class BooksFragment extends Fragment {

    private RecyclerView recyclerViewBooks;
    private FloatingActionButton fabAddBook;

    private List<Book> booksList;
    private BooksAdapter booksAdapter;

    private FirebaseFirestore firestore;
    private CollectionReference booksCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        booksCollection = firestore.collection("books");

        recyclerViewBooks = view.findViewById(R.id.recyclerViewBooks);
        fabAddBook = view.findViewById(R.id.fabAddBook);

        // Initialize the list of books and the RecyclerView adapter
        booksList = new ArrayList<>();
        booksAdapter = new BooksAdapter(booksList);

        // Set up RecyclerView
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBooks.setAdapter(booksAdapter);

        // Fetch and display books data
        fetchBooksData();

        // Set up click listener for the floating action button
        fabAddBook.setOnClickListener(v -> {
            // Open the activity for adding new books
            startActivity(new Intent(getContext(), AddBookActivity.class));
        });
    }

    private void fetchBooksData() {
        booksCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    booksList.clear(); // Clear existing data
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert Firestore document to Book object
                        Book book = document.toObject(Book.class);
                        booksList.add(book);
                    }

                    // Notify the adapter that the data set has changed
                    booksAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle failure to fetch data from Firestore
                    Toast.makeText(requireContext(), "Failed to fetch books data", Toast.LENGTH_SHORT).show();
                });
    }
}
