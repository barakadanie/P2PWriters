package com.theark.p2pwriters.Adapters;
// Your package declaration here

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.theark.p2pwriters.Model.Salesperson;
import com.theark.p2pwriters.R;

import java.util.List;

public class SalespersonsAdapter extends RecyclerView.Adapter<SalespersonsAdapter.SalespersonViewHolder> {

    private List<Salesperson> salespersonsList;
    private Context context;

    public SalespersonsAdapter(Context context, List<Salesperson> salespersonsList) {
        this.context = context;
        this.salespersonsList = salespersonsList;
    }

    @NonNull
    @Override
    public SalespersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salesperson, parent, false);
        return new SalespersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalespersonViewHolder holder, int position) {
        Salesperson salesperson = salespersonsList.get(position);
        holder.bind(salesperson);
    }

    @Override
    public int getItemCount() {
        return salespersonsList.size();
    }

    static class SalespersonViewHolder extends RecyclerView.ViewHolder {

        private ImageView salespersonImageView;
        private TextView nameTextView;
        private TextView phoneNumberTextView;
        private TextView emailAddressTextView;

        public SalespersonViewHolder(@NonNull View itemView) {
            super(itemView);

            salespersonImageView = itemView.findViewById(R.id.salespersonImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            emailAddressTextView = itemView.findViewById(R.id.emailAddressTextView);
        }

        public void bind(Salesperson salesperson) {
            // Load the image using Picasso
            Picasso.get().load(salesperson.getImageUrl()).placeholder(R.drawable.baseline_add_a_photo_24).into(salespersonImageView);

            nameTextView.setText(salesperson.getName());
            phoneNumberTextView.setText(salesperson.getPhoneNumber());
            emailAddressTextView.setText(salesperson.getEmailAddress());
        }
    }
}

