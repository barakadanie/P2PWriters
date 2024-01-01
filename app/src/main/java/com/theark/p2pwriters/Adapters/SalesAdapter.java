package com.theark.p2pwriters.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theark.p2pwriters.Model.Sale;
import com.theark.p2pwriters.R;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SaleViewHolder> {

    private List<Sale> salesList;
    private OnItemClickListener onItemClickListener;

    public SalesAdapter(List<Sale> salesList) {
        this.salesList = salesList;
    }

    @NonNull
    @Override
    public SaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
        return new SaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleViewHolder holder, int position) {
        Sale sale = salesList.get(position);
        holder.bind(sale);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(sale);
            }
        });
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // ViewHolder class
    static class SaleViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBookCategory, tvBookName, tvDate, tvSentTo, tvAmount;

        public SaleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSentTo = itemView.findViewById(R.id.tvSendTo);
            tvBookCategory = itemView.findViewById(R.id.tvBookCategory);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        public void bind(Sale sale) {
            tvSentTo.setText(sale.getSendTo());
            tvBookCategory.setText(sale.getBookCategory());
            tvBookName.setText(sale.getBook());
            tvAmount.setText(sale.getAmount());
            tvDate.setText(sale.getDateSent());
        }
    }

    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(Sale sale);
    }
}
