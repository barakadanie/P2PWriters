package com.theark.p2pwriters.Admin;
// Imports and package declaration here

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.theark.p2pwriters.Adapters.CategoryAdapter;
import com.theark.p2pwriters.Model.Category;
import com.theark.p2pwriters.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;

    private FirebaseFirestore firestore;
    private CollectionReference categoryCollection;
    private FloatingActionButton extendedFabAddCat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        firestore = FirebaseFirestore.getInstance();
        categoryCollection = firestore.collection("bookCategory");
        extendedFabAddCat=view.findViewById(R.id.fabAddCat);
        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);

        fetchCategories();
        extendedFabAddCat.setOnClickListener(view1 -> {
            // Open the activity for adding new salespersons
            startActivity(new Intent(getContext(), AddCategoryActivity.class));
        });
        return view;


    }

    private void fetchCategories() {
        categoryCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Category> categories = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Category category = document.toObject(Category.class);
                        categories.add(category);
                    }

                    categoryAdapter.setCategories(categories);
                })
                .addOnFailureListener(e -> {
                    // Handle failure to fetch data from Firestore
                });
    }
}
