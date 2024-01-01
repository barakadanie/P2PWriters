package com.theark.p2pwriters.Adapters;// Your package declaration here

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theark.p2pwriters.Model.Book;
import com.theark.p2pwriters.R;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    private List<Book> booksList;

    public BooksAdapter(List<Book> booksList) {
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBookName, tvBookCategory, tvAmount, tvDateAdded;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBookName = itemView.findViewById(R.id.bookNameTextView);
            tvBookCategory = itemView.findViewById(R.id.bookCategoryTextView);
            tvAmount = itemView.findViewById(R.id.bookTotalsTextView);
            tvDateAdded = itemView.findViewById(R.id.dateAddedTextView);
        }

        public void bind(Book book) {
            tvBookName.setText(book.getBookName());
            tvBookCategory.setText(book.getBookCategory());
            tvAmount.setText(book.getAmount());
            tvDateAdded.setText(book.getDateAdded());
        }
    }
}
