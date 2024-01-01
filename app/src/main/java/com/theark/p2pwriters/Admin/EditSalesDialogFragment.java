package com.theark.p2pwriters.Admin;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theark.p2pwriters.Model.Sale;
import com.theark.p2pwriters.R;

public class EditSalesDialogFragment extends DialogFragment {

    private Sale sale;

    private EditText etEditBookCategory, etEditBookName, etEditDate, etAmount, etSendTo;

    public EditSalesDialogFragment() {
        // Required empty public constructor
    }

    public static EditSalesDialogFragment create(Sale sale) {
        EditSalesDialogFragment fragment = new EditSalesDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("sale", sale);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_edit_sales_dialog, null);

        // Initialize dialog views
        etEditBookCategory = view.findViewById(R.id.etBookCategory);
        etEditBookName = view.findViewById(R.id.etBookName);
        etEditDate = view.findViewById(R.id.etDateSent);
        etAmount = view.findViewById(R.id.etAmount);
        etSendTo = view.findViewById(R.id.etSendTo);
        Button btnSaveChanges = view.findViewById(R.id.btnSaveChanges);

        // Retrieve sale object from arguments
        Bundle args = getArguments();
        if (args != null) {
            sale = (Sale) args.getSerializable("sale");
        }

        // Set initial values
        if (sale != null) {
            etSendTo.setText(sale.getSendTo());
            etAmount.setText(sale.getAmount());
            etEditBookCategory.setText(sale.getBookCategory());
            etEditBookName.setText(sale.getBook());
            etEditDate.setText(sale.getDateSent());
        }

        // Set up button click listener
        btnSaveChanges.setOnClickListener(v -> {
            // Update the sale in Firestore
            updateSaleInFirestore();
        });

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();
    }

    private void updateSaleInFirestore() {
        // Retrieve updated values from dialog views
        String sendTo = etSendTo.getText().toString();
        String amount = etAmount.getText().toString();
        String bookCategory = etEditBookCategory.getText().toString();
        String book = etEditBookName.getText().toString();
        String dateSent = etEditDate.getText().toString();

        // Check if the sale object and document ID are not null
        if (sale != null && sale.getDocumentId() != null) {
            // Get the reference to the sale document in Firestore
            DocumentReference saleRef = FirebaseFirestore.getInstance().collection("sendout").document(sale.getDocumentId());

            // Update the sale in Firestore
            saleRef.update(
                    "sendTo", sendTo,
                    "amount", amount,
                    "bookCategory", bookCategory,
                    "book", book,
                    "dateSent", dateSent
            ).addOnSuccessListener(aVoid -> {
                // Document updated successfully
                dismiss(); // Close the dialog
                Log.d("FirestoreUpdate", "Sale document updated successfully");
            }).addOnFailureListener(e -> {
                // Handle failure to update document
                // You can show an error message or log the error
                Log.e("FirestoreUpdate", "Error updating sale document", e);
            });
        } else {
            // Handle the case where the sale or document ID is null
            // You can show an error message or log the error
            Log.e("FirestoreUpdate", "Sale or document ID is null");
        }
    }

}
