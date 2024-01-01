package com.theark.p2pwriters.Model;// Your package declaration here

public class Book {

    private String bookName;
    private String bookCategory;
    private String amount;
    private String dateAdded;

    // Required default constructor for Firestore
    public Book() {
    }

    public Book(String bookName, String bookCategory, String amount, String dateAdded) {
        this.bookName = bookName;
        this.bookCategory = bookCategory;
        this.amount = amount;
        this.dateAdded = dateAdded;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
