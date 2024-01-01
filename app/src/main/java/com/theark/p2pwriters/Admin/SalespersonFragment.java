package com.theark.p2pwriters.Admin;
// Your package declaration here

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theark.p2pwriters.Adapters.SalespersonsAdapter;
import com.theark.p2pwriters.Model.Salesperson;
import com.theark.p2pwriters.R;

import java.util.ArrayList;
import java.util.List;

public class SalespersonFragment extends Fragment {

    private RecyclerView recyclerViewSalespersons;
    private FloatingActionButton fabAddSalesperson;

    private FirebaseFirestore firestore;
    private CollectionReference salespersonsCollection;

    private List<Salesperson> salespersonsList;
    private SalespersonsAdapter salespersonsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_salesperson, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        salespersonsCollection = firestore.collection("salespersons");

        recyclerViewSalespersons = view.findViewById(R.id.recyclerViewSalespersons);
        fabAddSalesperson = view.findViewById(R.id.fabAddSalesperson);

        // Initialize the list of salespersons and the RecyclerView adapter
        salespersonsList = new ArrayList<>();
        salespersonsAdapter = new SalespersonsAdapter(requireContext(), salespersonsList);

        // Set up RecyclerView
        recyclerViewSalespersons.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSalespersons.setAdapter(salespersonsAdapter);

        // Set up FloatingActionButton click listener
        fabAddSalesperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the activity for adding new salespersons
                startActivity(new Intent(getContext(), AddSalespersonActivity.class));
            }
        });

        // Load salespersons from Firestore
        loadSalespersons();
    }

    private void loadSalespersons() {
        // Fetch the list of salespersons from Firestore and update the adapter
        // You need to implement the logic to retrieve data from Firestore
        // This is a simplified example
        salespersonsCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    salespersonsList.clear();
                    for (Salesperson salesperson : queryDocumentSnapshots.toObjects(Salesperson.class)) {
                        salespersonsList.add(salesperson);
                    }
                    salespersonsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to fetch salespersons from Firestore
                });
    }
}
