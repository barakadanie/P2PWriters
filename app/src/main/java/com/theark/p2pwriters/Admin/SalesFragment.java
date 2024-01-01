package com.theark.p2pwriters.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
import com.theark.p2pwriters.Adapters.SalesAdapter;
import com.theark.p2pwriters.Admin.AddSalesActivity;
import com.theark.p2pwriters.Model.Sale;
import com.theark.p2pwriters.R;

import java.util.ArrayList;
import java.util.List;

public class SalesFragment extends Fragment {

    private TableLayout tableLayoutSales;
    private RecyclerView recyclerViewSales;

    private List<Sale> salesList;
    private SalesAdapter salesAdapter;

    private FirebaseFirestore firestore;
    private CollectionReference salesCollection;

    private FloatingActionButton extendedFabAddSales;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sales, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        salesCollection = firestore.collection("sendout");

        extendedFabAddSales = view.findViewById(R.id.fabAddSales);
        tableLayoutSales = view.findViewById(R.id.tableLayoutSales);
        recyclerViewSales = view.findViewById(R.id.recyclerViewSales);

        // Initialize the list of sales and the RecyclerView adapter
        salesList = new ArrayList<>();
        salesAdapter = new SalesAdapter(salesList);

        // Set up RecyclerView
        recyclerViewSales.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSales.setAdapter(salesAdapter);

        // Set up the click listener for RecyclerView items
        salesAdapter.setOnItemClickListener(new SalesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Sale sale) {
                // Handle item click, you can open a dialog or perform other actions
                // For example, open a dialog to edit the sale details
                showEditSaleDialog(sale);
            }
        });

        // Fetch and sort sales data from Firestore
        fetchAndSortSalesData();
        updateTableHeaders();

        extendedFabAddSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the activity for adding new salespersons
                startActivity(new Intent(getContext(), AddSalesActivity.class));
            }
        });
    }

    private void showEditSaleDialog(Sale sale) {
        EditSalesDialogFragment editSaleDialogFragment = EditSalesDialogFragment.create(sale);
        editSaleDialogFragment.show(getParentFragmentManager(), "EditSaleDialogFragment");
    }

    private void fetchAndSortSalesData() {
        salesCollection.orderBy("sendTo") // Sort by the "sendTo" field
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    salesList.clear(); // Clear existing data
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert Firestore document to Sale object
                        Sale sale = document.toObject(Sale.class);
                        salesList.add(sale);
                    }

                    // Notify the adapter that the data set has changed
                    salesAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle failure to fetch data from Firestore
                });
    }

    private void updateTableHeaders() {
        // Create a TableRow for the header
        TableRow headerRow = new TableRow(requireContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);

        // Set layout parameters for the header row
        headerRow.setLayoutParams(layoutParams);

        // Add TextViews for each column
        TextView tvBookCategory = createTableHeaderTextView("Book Category");
        TextView tvBookName = createTableHeaderTextView("Book Name");
        TextView tvDate = createTableHeaderTextView("Date Sent");

        // Add TextViews to the header row
        headerRow.addView(tvBookCategory);
        headerRow.addView(tvBookName);
        headerRow.addView(tvDate);

        // Add the header row to the table layout
        tableLayoutSales.addView(headerRow);
    }

    private TextView createTableHeaderTextView(String text) {
        TextView textView = new TextView(requireContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f);

        // Set layout parameters for each TextView
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        textView.setPadding(4, 4, 4, 4);

        return textView;
    }
}
